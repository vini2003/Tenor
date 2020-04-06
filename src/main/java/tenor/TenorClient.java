package tenor;

import net.fabricmc.api.ClientModInitializer;
import tenor.initialize.*;

public class TenorClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		TenorKeybinds.initialize();
		TenorScreens.initialize();
		TenorRenderers.initialize();
		TenorLayers.initialize();
		TenorPacketsClient.initialize();
	}
}
