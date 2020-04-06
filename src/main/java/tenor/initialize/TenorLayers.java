package tenor.initialize;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class TenorLayers {
	public static void initialize() {
		register(TenorBlocks.CONNECTOR_LOW_VOLTAGE, RenderLayer.getCutout());
		register(TenorBlocks.CONNECTOR_MEDIUM_VOLTAGE, RenderLayer.getCutout());
		register(TenorBlocks.CONNECTOR_HIGH_VOLTAGE, RenderLayer.getCutout());
	}

	/**
	 * @param block       Block instance to be registered
	 * @param renderLayer RenderLayer of block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(T block, RenderLayer renderLayer) {
		BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
		return block;
	}
}
