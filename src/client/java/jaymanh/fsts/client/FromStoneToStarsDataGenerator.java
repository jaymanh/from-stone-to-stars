package jaymanh.fsts.client;

import jaymanh.fsts.client.datagen.ModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import org.jspecify.annotations.NonNull;

public class FromStoneToStarsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
		DataGenerator.PackGenerator packGenerator = fabricDataGenerator.createPack();
		//packGenerator.addProvider(ModelGenerator::new);
	}
}
