package tenor.network;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NetworkTickerRegistry {
    public static final NetworkTickerRegistry INSTANCE = new NetworkTickerRegistry();

    private static final Map<Identifier, NetworkTicker> entries = new HashMap<>();

    private NetworkTickerRegistry() {
    }

    public NetworkTicker register(Identifier identifier, Supplier<NetworkTicker> type) {
        entries.put(identifier, type.get());
        return type.get();
    }

    public NetworkTicker get(Identifier identifier) {
        return entries.get(identifier);
    }
}
