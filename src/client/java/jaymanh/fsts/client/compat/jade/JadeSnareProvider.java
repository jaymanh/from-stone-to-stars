package jaymanh.fsts.client.compat.jade;

import jaymanh.fsts.FromStoneToStars;
import jaymanh.fsts.block.entity.SnareBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeSnareProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(this)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int bait = data.getIntOr("FstsBait", 0);
		tooltip.add(Component.translatable(
				"tooltip.from-stone-to-stars.snare.bait",
				bait,
				SnareBlockEntity.BAIT_CAPACITY
		));
		data.getList("FstsLoot").ifPresent(list -> {
			for (int i = 0; i < list.size(); i++) {
				list.getString(i).ifPresent(line -> tooltip.add(Component.literal(line)));
			}
		});
	}

	@Override
	public Identifier getUid() {
		return FromStoneToStars.id("snare");
	}

	public enum Data implements IServerDataProvider<BlockAccessor> {
		INSTANCE;

		@Override
		public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
			if (accessor.getBlockEntity() instanceof SnareBlockEntity snare) {
				tag.putInt("FstsBait", snare.getItem(SnareBlockEntity.BAIT_SLOT).getCount());
				ListTag loot = new ListTag();
				for (int slot = 1; slot < SnareBlockEntity.SLOT_COUNT; slot++) {
					ItemStack stack = snare.getItem(slot);
					if (!stack.isEmpty()) {
						loot.add(StringTag.valueOf(stack.getCount() + " " + stack.getHoverName().getString()));
					}
				}
				tag.put("FstsLoot", loot);
			}
		}

		@Override
		public Identifier getUid() {
			return JadeSnareProvider.INSTANCE.getUid();
		}
	}
}
