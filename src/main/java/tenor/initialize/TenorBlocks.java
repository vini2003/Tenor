package tenor.initialize;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import tenor.TenorCommon;
import tenor.block.WireNodeBlock;

public class TenorBlocks {
	public static final Block CONNECTOR_LOW_VOLTAGE = register("connector_low_voltage", new WireNodeBlock(0, FabricBlockSettings.copy(Blocks.QUARTZ_BLOCK).nonOpaque().build()), new Item.Settings().group(TenorItemGroups.TENOR));
	public static final Block CONNECTOR_MEDIUM_VOLTAGE = register("connector_medium_voltage", new WireNodeBlock(1, FabricBlockSettings.copy(Blocks.TERRACOTTA).nonOpaque().build()), new Item.Settings().group(TenorItemGroups.TENOR));
	public static final Block CONNECTOR_HIGH_VOLTAGE = register("connector_high_voltage", new WireNodeBlock(2, FabricBlockSettings.copy(Blocks.GREEN_CONCRETE).nonOpaque().build()), new Item.Settings().group(TenorItemGroups.TENOR));

	public static final Block COPPER_ORE = register("copper_ore", new Block(FabricBlockSettings.copy(Blocks.IRON_ORE).build()), new Item.Settings().group(TenorItemGroups.TENOR));
	public static final Block COPPER_BLOCK = register("copper_block", new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK).build()), new Item.Settings().group(TenorItemGroups.TENOR));

	public static void initialize() {

	}

	/**
	 * @param name     Name of block instance to be registered
	 * @param block    Block instance to be registered
	 * @param settings Item.Settings of BlockItem of Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new BlockItem(block, settings));
	}

	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @param item  BlockItem instance of Block to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(String name, T block, BlockItem item) {
		T b = register(new Identifier(TenorCommon.MOD_ID, name), block);
		if (item != null) {
			TenorItems.register(name, item);
		}
		return b;
	}

	/**
	 * @param name  Name of block instance to be registered
	 * @param block Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(String name, T block) {
		return register(new Identifier(TenorCommon.MOD_ID, name), block);
	}

	/**
	 * @param name  Identifier of block instance to be registered
	 * @param block Block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(Identifier name, T block) {
		return Registry.register(Registry.BLOCK, name, block);
	}
}
