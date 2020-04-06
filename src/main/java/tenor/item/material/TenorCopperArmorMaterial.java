package tenor.item.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import tenor.initialize.TenorItems;

public class TenorCopperArmorMaterial implements ArmorMaterial {
	public static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
	public static final int[] BASE_PROTECTION = new int[]{2, 4, 5, 2};

	public static final int durabilityMultiplier = 12;

	@Override
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * durabilityMultiplier;
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return BASE_PROTECTION[slot.getEntitySlotId()];
	}

	@Override
	public int getEnchantability() {
		return 9;
	}

	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(TenorItems.COPPER_INGOT);
	}

	@Override
	public String getName() {
		return "copper";
	}

	@Override
	public float getToughness() {
		return 0;
	}
}
