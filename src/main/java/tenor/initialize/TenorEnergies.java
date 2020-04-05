package tenor.initialize;

import net.minecraft.util.Identifier;
import tenor.energy.TenorEnergy;
import tenor.network.NetworkTicker;
import tenor.network.NetworkTickerEnergy;
import tenor.network.NetworkTickerRegistry;

public class TenorEnergies {
	public static final TenorEnergy ENERGY = new TenorEnergy();

	public static final NetworkTicker ENERGY_TYPE = NetworkTickerRegistry.INSTANCE.register(new Identifier("arno", "energy_network"), NetworkTickerEnergy::new);

	public static void initialize() {

	}
}
