package tenor.initialize;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import tenor.TenorCommon;

public class TenorItems {
	public static void initialize() {

	}

	/**
	 * @param name Name of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instanced registered
	 */
	public static <T extends Item> T register(String name, T item) {
		return register(new Identifier(TenorCommon.MOD_ID, name), item);
	}

	/**
	 * @param name Identifier of item instance to be registered
	 * @param item Item instance to be registered
	 * @return Item instance registered
	 */
	public static <T extends Item> T register(Identifier name, T item) {
		return Registry.register(Registry.ITEM, name, item);
	}
}
