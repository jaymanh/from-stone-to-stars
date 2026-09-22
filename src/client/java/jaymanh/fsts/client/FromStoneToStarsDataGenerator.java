package jaymanh.fsts.client;

import jaymanh.fsts.client.ModTagGenerators.BlockTagGenerator;
import jaymanh.fsts.client.ModTagGenerators.EntityTypeTagGenerator;
import jaymanh.fsts.client.ModTagGenerators.ItemTagGeneration;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

public class FromStoneToStarsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack packGenerator = fabricDataGenerator.createPack();
		packGenerator.addProvider(ItemTagGeneration::new);
		packGenerator.addProvider(BlockTagGenerator::new);
		packGenerator.addProvider(EntityTypeTagGenerator::new);
	}
}
