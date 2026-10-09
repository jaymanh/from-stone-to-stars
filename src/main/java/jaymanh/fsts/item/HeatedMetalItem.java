package jaymanh.fsts.item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;

import jaymanh.fsts.component.ModDataComponents;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

/**
 * Ingot heated in a blast furnace / smelter. Stays hot while in those inventories;
 * cools for {@link #COOL_TICKS} after leaving, then reverts to {@link #cooledItem}.
 */
public class HeatedMetalItem extends Item {
	public static final int COOL_TICKS = 20 * 60;

	/**
	 * Per-player last game tick we synced heat timers (avoids re-scanning every heated stack).
	 * Entries are removed when that player leaves or respawns, and when the server stops.
	 */
	private static final Map<Integer, Long> LAST_HEAT_SYNC_TICK = new HashMap<>();

	private final Supplier<Item> cooledItem;

	public HeatedMetalItem(Properties properties, Supplier<Item> cooledItem) {
		super(properties);
		this.cooledItem = cooledItem;
	}

	public Item cooledItem() {
		return this.cooledItem.get();
	}

	/** Drops heat-sync timestamps so a logged-out or respawned player's entity id does not stay in the map. */
	public static void initialize() {
		ServerPlayerEvents.LEAVE.register(HeatedMetalItem::forgetHeatSync);
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> forgetHeatSync(oldPlayer));
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> LAST_HEAT_SYNC_TICK.clear());
	}

	private static void forgetHeatSync(Player player) {
		LAST_HEAT_SYNC_TICK.remove(player.getId());
	}

	public static boolean isHeated(ItemStack stack) {
		return stack.getItem() instanceof HeatedMetalItem;
	}

	/** Same heated metal type, ignoring different cool timers. */
	public static boolean isSameHeatedMetal(ItemStack a, ItemStack b) {
		return isHeated(a) && ItemStack.isSameItem(a, b);
	}

	/**
	 * Keep the earlier cool deadline when combining stacks so heat time is never extended by merging.
	 */
	public static void keepEarlierHeat(ItemStack destination, ItemStack source) {
		if (!isHeated(destination) || !isHeated(source)) {
			return;
		}
		Long destUntil = destination.get(ModDataComponents.HEAT_UNTIL);
		Long sourceUntil = source.get(ModDataComponents.HEAT_UNTIL);
		if (destUntil == null) {
			if (sourceUntil != null) {
				destination.set(ModDataComponents.HEAT_UNTIL, sourceUntil);
			}
			return;
		}
		if (sourceUntil != null && sourceUntil < destUntil) {
			destination.set(ModDataComponents.HEAT_UNTIL, sourceUntil);
		}
	}

	/**
	 * Applies cooling for stacks sitting in a container. Returns the same stack or a cooled replacement.
	 */
	public static ItemStack coolIfExpired(ItemStack stack, long gameTime) {
		if (!(stack.getItem() instanceof HeatedMetalItem heated) || stack.isEmpty()) {
			return stack;
		}
		Long until = stack.get(ModDataComponents.HEAT_UNTIL);
		if (until == null) {
			stack.set(ModDataComponents.HEAT_UNTIL, gameTime + COOL_TICKS);
			return stack;
		}
		if (gameTime < until) {
			return stack;
		}
		return new ItemStack(heated.cooledItem(), stack.getCount());
	}

	/** Keep stacks in a smelter/forge from starting their cool timer. */
	public static void keepHot(AbstractFurnaceBlockEntity furnace, long gameTime) {
		// SLOT_INPUT = 0, SLOT_RESULT = 2 (protected on AbstractFurnaceBlockEntity)
		keepHot(furnace.getItem(0), gameTime);
		keepHot(furnace.getItem(2), gameTime);
	}

	public static void keepHot(ItemStack stack, long gameTime) {
		if (isHeated(stack)) {
			stack.set(ModDataComponents.HEAT_UNTIL, gameTime + COOL_TICKS);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
		tickCooling(stack, level, entity);
		if (entity instanceof Player player) {
			syncHeatInInventory(player, level.getGameTime());
		}
	}

	/**
	 * Align cool timers for the same heated item so stacks share one {@code heat_until} (the earliest)
	 * and can merge in survival inventories.
	 */
	public static void syncHeatInInventory(Player player, long gameTime) {
		int playerId = player.getId();
		if (player.isRemoved()) {
			LAST_HEAT_SYNC_TICK.remove(playerId);
			return;
		}
		Long last = LAST_HEAT_SYNC_TICK.put(playerId, gameTime);
		if (last != null && last == gameTime) {
			return;
		}

		Map<Item, Long> earliest = new HashMap<>();
		var inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (!isHeated(stack)) {
				continue;
			}
			Long until = stack.get(ModDataComponents.HEAT_UNTIL);
			if (until == null) {
				until = gameTime + COOL_TICKS;
				stack.set(ModDataComponents.HEAT_UNTIL, until);
			}
			earliest.merge(stack.getItem(), until, Math::min);
		}
		if (earliest.isEmpty()) {
			LAST_HEAT_SYNC_TICK.remove(playerId);
			return;
		}
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (!isHeated(stack)) {
				continue;
			}
			Long target = earliest.get(stack.getItem());
			if (target != null) {
				Long current = stack.get(ModDataComponents.HEAT_UNTIL);
				if (!target.equals(current)) {
					stack.set(ModDataComponents.HEAT_UNTIL, target);
				}
			}
		}
	}

	/**
	 * Apply absolute-time cooling. Replaces the stack in the holding entity when expired.
	 */
	public static void tickCooling(ItemStack stack, ServerLevel level, @Nullable Entity holder) {
		if (!(stack.getItem() instanceof HeatedMetalItem heated) || stack.isEmpty()) {
			return;
		}

		long now = level.getGameTime();
		Long until = stack.get(ModDataComponents.HEAT_UNTIL);
		if (until == null) {
			stack.set(ModDataComponents.HEAT_UNTIL, now + COOL_TICKS);
			return;
		}
		if (now < until) {
			return;
		}

		ItemStack cooled = new ItemStack(heated.cooledItem(), stack.getCount());
		replaceStack(holder, stack, cooled);
	}

	private static void replaceStack(@Nullable Entity holder, ItemStack oldStack, ItemStack replacement) {
		if (holder instanceof ItemEntity itemEntity) {
			itemEntity.setItem(replacement);
			return;
		}
		if (holder instanceof Player player) {
			var inventory = player.getInventory();
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				if (inventory.getItem(i) == oldStack) {
					inventory.setItem(i, replacement);
					return;
				}
			}
		}
		// Fallback: mutate count away if we cannot find a slot (should be rare).
		oldStack.setCount(0);
	}

	@Override
	public void appendHoverText(
			ItemStack stack,
			TooltipContext context,
			TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder,
			TooltipFlag flag
	) {
		tooltipAdder.accept(Component.translatable("item.from-stone-to-stars.heated_metal.cool"));
	}
}
