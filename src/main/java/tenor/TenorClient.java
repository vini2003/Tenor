package tenor;

import net.fabricmc.api.ClientModInitializer;
import tenor.initialize.TenorKeybinds;
import tenor.initialize.TenorLayers;
import tenor.initialize.TenorRenderers;
import tenor.initialize.TenorScreens;

public class TenorClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		TenorKeybinds.initialize();
		TenorScreens.initialize();
		TenorRenderers.initialize();
		TenorLayers.initialize();
	}
}
