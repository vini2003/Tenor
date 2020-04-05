package tenor.network;

import com.google.common.collect.Sets;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class NetworkManager implements Tickable {
    public static final NetworkManager INSTANCE = new NetworkManager();

    private final Set<NetworkController> controllers = Sets.newConcurrentHashSet();

    private NetworkManager() {
    }

    public void add(NetworkController controller) {
        controllers.add(controller);
    }

    public void remove(NetworkController controller) {
        controllers.remove(controller);
    }

    public NetworkController get(NetworkTicker type, BlockPos position) {
        for (NetworkController controller : controllers) {
            if (controller.getType() == type && controller.nodes.contains(NetworkNode.of(position))) return controller;
        }

        return NetworkController.EMPTY;
    }

    @Override
    public void tick() {
        controllers.forEach(NetworkController::tick);
    }
}
