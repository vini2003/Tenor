package tenor;

import net.fabricmc.api.ModInitializer;
import tenor.initialize.*;

public class TenorCommon implements ModInitializer {
	public static final String MOD_ID = "tenor";

	@Override
	public void onInitialize() {
		TenorItemGroups.initialize();
		TenorTags.initialize();
		TenorItems.initialize();
		TenorBlocks.initialize();
		TenorBlockEntities.initialize();
		TenorContainers.initialize();
		TenorSerializers.initialize();
		TenorEnergies.initialize();
		TenorFeatures.initialize();
		TenorCallbacks.initialize();
	}
}
