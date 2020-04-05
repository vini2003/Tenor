package tenor.initialize;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.registry.Registry;

public class TenorCallbacks {
	public static void initialize() {
		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> TenorFeatures.addCopper(biome));
	}
}
