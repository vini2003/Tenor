package tenor.initialize;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import tenor.TenorCommon;
import tenor.item.ExposedAxeItem;
import tenor.item.ExposedPickaxeItem;

public class TenorItems {
	public static final Item COPPER_COIL = register("copper_coil", new Item(new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item GOLD_COIL = register("gold_coil", new Item(new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item FIBER_COIl = register("fiber_coil", new Item(new Item.Settings().group(TenorItemGroups.TENOR)));

	public static final Item COPPER_INGOT = register("copper_ingot", new Item(new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_NUGGET = register("copper_nugget", new Item(new Item.Settings().group(TenorItemGroups.TENOR)));

	public static final Item COPPER_PICKAXE = register("copper_pickaxe", new ExposedPickaxeItem(TenorMaterials.TOOL_COPPER, 1, -2.8F, new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_SWORD = register("copper_sword", new SwordItem(TenorMaterials.TOOL_COPPER, 3, -2.4F, new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_AXE = register("copper_axe", new ExposedAxeItem(TenorMaterials.TOOL_COPPER, 5F, -3F, new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_SHOVEL = register("copper_shovel", new ShovelItem(TenorMaterials.TOOL_COPPER, 1.35F, -3F, new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_HOE = register("copper_hoe", new HoeItem(TenorMaterials.TOOL_COPPER, -1.7F, new Item.Settings().group(TenorItemGroups.TENOR)));

	public static final Item COPPER_HELMET = register("copper_helmet", new ArmorItem(TenorMaterials.ARMOR_COPPER, EquipmentSlot.HEAD, new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_CHESTPLATE= register("copper_chestplate", new ArmorItem(TenorMaterials.ARMOR_COPPER, EquipmentSlot.CHEST, new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_LEGGINGS = register("copper_leggings", new ArmorItem(TenorMaterials.ARMOR_COPPER, EquipmentSlot.LEGS, new Item.Settings().group(TenorItemGroups.TENOR)));
	public static final Item COPPER_BOOTS = register("copper_boots", new ArmorItem(TenorMaterials.ARMOR_COPPER, EquipmentSlot.FEET, new Item.Settings().group(TenorItemGroups.TENOR)));

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
