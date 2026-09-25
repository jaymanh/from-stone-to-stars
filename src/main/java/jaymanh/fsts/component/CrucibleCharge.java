package jaymanh.fsts.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

/**
 * Metals packed into a crucible, ready to fire in a basic smelter.
 */
public record CrucibleCharge(ItemStackTemplate result, int cookingTime, float experience) {
	public static final Codec<CrucibleCharge> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ItemStackTemplate.CODEC.fieldOf("result").forGetter(CrucibleCharge::result),
			ExtraCodecs.POSITIVE_INT.fieldOf("cookingtime").forGetter(CrucibleCharge::cookingTime),
			Codec.FLOAT.fieldOf("experience").forGetter(CrucibleCharge::experience)
	).apply(instance, CrucibleCharge::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleCharge> STREAM_CODEC = StreamCodec.composite(
			ItemStackTemplate.STREAM_CODEC,
			CrucibleCharge::result,
			ByteBufCodecs.VAR_INT,
			CrucibleCharge::cookingTime,
			ByteBufCodecs.FLOAT,
			CrucibleCharge::experience,
			CrucibleCharge::new
	);

	public ItemStack createResult() {
		return this.result.create();
	}
}
