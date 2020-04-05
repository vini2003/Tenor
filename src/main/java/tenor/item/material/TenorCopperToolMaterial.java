package tenor.item.material;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import tenor.initialize.TenorItems;

public class TenorCopperToolMaterial implements ToolMaterial {
	@Override
	public int getDurability() {
		return 200;
	}

	@Override
	public float getMiningSpeed() {
		return 5F;
	}

	@Override
	public float getAttackDamage() {
		return 2F;
	}

	@Override
	public int getMiningLevel() {
		return 2;
	}

	@Override
	public int getEnchantability() {
		return 12;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(TenorItems.COPPER_INGOT);
	}
}
