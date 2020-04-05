package tenor.initialize;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import tenor.block.entity.renderer.WireNodeBlockEntityRenderer;

import java.util.function.Function;

public class TenorRenderers {
	public static void initialize() {
		register(TenorBlockEntities.CONNECTOR_ENTITY_TYPE, WireNodeBlockEntityRenderer::new);
	}

	public static <B extends BlockEntity, C extends BlockEntityType<B>> void register(C c, Function<BlockEntityRenderDispatcher, BlockEntityRenderer<B>> b) {
		BlockEntityRendererRegistry.INSTANCE.register(c, b);
	}
}
