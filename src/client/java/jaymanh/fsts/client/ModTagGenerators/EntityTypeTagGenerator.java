package jaymanh.fsts.client.ModTagGenerators;

import jaymanh.fsts.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends FabricTagsProvider.EntityTypeTagsProvider {
	public EntityTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		super(output, registryLookupFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		builder(ModTags.DROPS_BONES)
				.add(entity("zombie"))
				.add(entity("husk"))
				.add(entity("drowned"))
				.add(entity("cow"))
				.add(entity("sheep"))
				.add(entity("chicken"))
				.add(entity("horse"))
				.add(entity("pig"))
				.add(entity("wolf"));
	}

	private static ResourceKey<EntityType<?>> entity(String path) {
		return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("minecraft", path));
	}
}
