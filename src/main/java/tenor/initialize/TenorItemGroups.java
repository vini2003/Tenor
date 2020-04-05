package tenor.initialize;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import tenor.TenorCommon;

public class TenorItemGroups {
	public static ItemGroup TENOR = FabricItemGroupBuilder.build(new Identifier(TenorCommon.MOD_ID, "tenor"), () -> new ItemStack(TenorBlocks.CONNECTOR_MEDIUM_VOLTAGE));
	
	public static void initialize() {

	}
}
