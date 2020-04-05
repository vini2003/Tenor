package tenor.network;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableBoolean;
import tenor.block.entity.BlockEntityEnergized;
import tenor.block.entity.WireNodeBlockEntity;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class NetworkTracer {
    public static Object getObjectAt(World world, BlockPos position) {
        Block newBlock = world.getBlockState(position).getBlock();
        BlockEntity newEntity = world.getBlockEntity(position);

        Object newObject;

        if (newEntity != null) {
            newObject = newEntity;
        } else {
            newObject = newBlock;
        }

        return newObject;
    }

    public static class Tracer {
        private NetworkController controller;

        public void trace(NetworkTicker type, BlockPos initialPosition, World world) {
            this.controller = new NetworkController(world, type);

            Block block = world.getBlockState(initialPosition).getBlock();

            if (!(block instanceof NetworkComponent) || !((NetworkComponent) block).isNode()) {
                return;
            }

            Set<BlockPos> cache = new HashSet<>();
            cache.add(initialPosition);

            ArrayDeque<BlockPos> positions = new ArrayDeque<>();
            positions.add(initialPosition);

            if (!NetworkManager.INSTANCE.get(type, initialPosition).isNullOrEmpty()) {
                return;
            } else {
                controller.addPosition(initialPosition);
            }

            while (!positions.isEmpty()) {
                BlockPos position = positions.getLast();
                positions.removeLast();
                MutableBoolean joined = new MutableBoolean(false);
                Object initialObject = getObjectAt(world, position);

                for (BlockEntityEnergized child : ((WireNodeBlockEntity) initialObject).children) {
                    BlockPos offsetPosition = child.getPos();

                    if (cache.contains(offsetPosition)) continue;

                    Object offsetObject = getObjectAt(world, offsetPosition);

                    NetworkController existingController = NetworkManager.INSTANCE.get(type, offsetPosition);

                    if (!existingController.isNullOrEmpty()) {
                        controller = existingController.join(controller);
                        joined.setTrue();
                    } else if (offsetObject instanceof NetworkComponent) {
                        NetworkComponent offsetMember = (NetworkComponent) offsetObject;

                        if ((offsetMember.isRequester() || offsetMember.isProvider() || offsetMember.isBuffer()) && offsetMember.accepts(type)) {
                            controller.addMember(NetworkNode.of(offsetPosition));
                        }
                        if (offsetMember.isNode()) {
                            if (offsetMember.accepts(type, initialObject)) {
                                positions.addLast(offsetPosition);
                                controller.addNode(NetworkNode.of(offsetPosition));
                            }
                        }
                    }

                    cache.add(offsetPosition);
                }

                if (joined.isTrue()) {
                    return;
                }
            }

            NetworkManager.INSTANCE.add(controller);
        }
    }
}
