package tenor.initialize;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import tenor.TenorCommon;
import tenor.block.entity.WireNodeBlockEntity;

import java.util.function.Supplier;

public class TenorBlockEntities {
	public static final BlockEntityType<WireNodeBlockEntity> CONNECTOR_ENTITY_TYPE = register(
			"connector_entity",
			WireNodeBlockEntity::new,
			TenorBlocks.CONNECTOR_LOW_VOLTAGE,
			TenorBlocks.CONNECTOR_MEDIUM_VOLTAGE,
			TenorBlocks.CONNECTOR_HIGH_VOLTAGE
	);

	public static void initialize() {

	}

	/**
	 * @param name            Name of BlockEntityType instance to be registered
	 * @param supplier        Supplier of BlockEntity to use for BlockEntityType
	 * @param supportedBlocks Blocks the BlockEntity can be attached to
	 * @return Registered BlockEntityType
	 */
	public static <B extends BlockEntity> BlockEntityType<B> register(String name, Supplier<B> supplier, Block... supportedBlocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(TenorCommon.MOD_ID, name), BlockEntityType.Builder.create(supplier, supportedBlocks).build(null));
	}
}
