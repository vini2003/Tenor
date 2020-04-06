package tenor.initialize;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class TenorFeatures {
	public static void initialize() {
		Registry.BIOME.forEach(TenorFeatures::addCopper);
	}

	public static void addCopper(Biome biome) {
		if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
			biome.addFeature(
					GenerationStep.Feature.UNDERGROUND_ORES,
					Feature.ORE.configure(
							new OreFeatureConfig(
									OreFeatureConfig.Target.NATURAL_STONE,
									TenorBlocks.COPPER_ORE.getDefaultState(),
									8 //Ore vein size
							)).createDecoratedFeature(
							Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
									12, //Number of veins per chunk
									0, //Bottom Offset
									0, //Min y level
									64 //Max y level
							))));
		}
	}
}
