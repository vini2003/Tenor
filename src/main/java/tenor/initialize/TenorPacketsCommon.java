package tenor.initialize;

import io.github.cottonmc.component.item.InventoryComponent;
import io.netty.buffer.Unpooled;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import tenor.TenorCommon;
import tenor.block.entity.WireNodeBlockEntity;

public class TenorPacketsCommon {
	public static final Identifier CONNECTOR_REMOVAL_PACKET = new Identifier(TenorCommon.MOD_ID, "connector_removal_packet");

	public static void initialize() {
	}

	public static PacketByteBuf createConnectorRemovalPacket(BlockEntity blockEntity) {
		PacketByteBuf bytes = new PacketByteBuf(Unpooled.buffer());

		WireNodeBlockEntity removedEntity = (WireNodeBlockEntity) blockEntity;

		bytes.writeBlockPos(removedEntity.getPos());

		bytes.writeInt(removedEntity.children.size() + removedEntity.parents.size());

		for (BlockPos child : removedEntity.children) {
			bytes.writeBlockPos(child);
		}

		for (BlockPos parent : removedEntity.parents) {
			bytes.writeBlockPos(parent);
		}

		return bytes;
	}
}
