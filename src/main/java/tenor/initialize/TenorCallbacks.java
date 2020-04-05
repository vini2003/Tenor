package tenor.initialize;

import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import tenor.network.NetworkManager;

public class TenorCallbacks {
	public static void initialize() {
		ServerTickCallback.EVENT.register(tick -> {
			NetworkManager.INSTANCE.tick();
		});
	}
}
