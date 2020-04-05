package tenor.network;

import io.github.cottonmc.component.api.ActionType;
import net.minecraft.block.entity.BlockEntity;
import tenor.block.entity.BlockEntityEnergized;
import tenor.initialize.TenorEnergies;

import java.util.*;

public class NetworkTickerEnergy extends NetworkTicker {
    @Override
    public void tick(NetworkController controller) {
        // TODO: Implement Provider Requester cycle

        Map<NetworkComponent, BlockEntityEnergized> bufferMap = new HashMap<>();

        Map<NetworkComponent, Double> bufferInsertions = new HashMap<>();
        Map<NetworkComponent, Double> bufferExtractions = new HashMap<>();

        /**
         * Iterate through raw members and build Buffers, Providers and Storages.
         */
        for (NetworkNode memberNode : controller.memberNodes) {
            BlockEntity blockEntity = controller.world.getBlockEntity(memberNode.position);
            if (blockEntity != null) {
                NetworkComponent member = (NetworkComponent) blockEntity;
                if (member.isBuffer()) {
                    bufferMap.put(member, (BlockEntityEnergized) blockEntity);
                    bufferInsertions.put(member, (double) ((BlockEntityEnergized) blockEntity).getMaxTransfer());
                    bufferExtractions.put(member, (double) ((BlockEntityEnergized) blockEntity).getMaxTransfer());
                }
            }
        }

        /*
          Shuffle Buffers, to approximate relatively equal distribution.
         */
        List<NetworkComponent> shuffledBuffers = new ArrayList<>(bufferMap.keySet());

        Collections.shuffle(shuffledBuffers);

        /**
         * Iterate through Buffers, and balance them.
         */
        for (NetworkComponent bufferA : shuffledBuffers) {
            for (NetworkComponent bufferB : shuffledBuffers) {
                if (bufferA == bufferB) continue;

                if (bufferMap.get(bufferA).getCurrentEnergy() <= bufferMap.get(bufferB).getCurrentEnergy()) continue;

                if (bufferInsertions.get(bufferB) <= 0) continue;
                if (bufferExtractions.get(bufferA) <= 0) break;

                double previousEnergy = bufferMap.get(bufferB).getCurrentEnergy();
                bufferMap.get(bufferB).energy = bufferMap.get(bufferB).insertEnergy(TenorEnergies.ENERGY, bufferMap.get(bufferA).getCurrentEnergy(), ActionType.PERFORM);
                double currentEnergy = previousEnergy - bufferMap.get(bufferB).getCurrentEnergy();

                bufferInsertions.put(bufferB, bufferInsertions.get(bufferB) - currentEnergy - previousEnergy);
                bufferExtractions.put(bufferA, bufferExtractions.get(bufferA) - currentEnergy - currentEnergy);
            }
        }
    }
}
