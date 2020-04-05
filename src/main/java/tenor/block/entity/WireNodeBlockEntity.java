package tenor.block.entity;

import io.github.cottonmc.component.energy.CapacitorComponent;
import net.minecraft.util.Tickable;
import tenor.block.WireNodeBlock;
import tenor.initialize.TenorBlockEntities;
import tenor.initialize.TenorEnergies;
import tenor.network.NetworkComponent;
import tenor.network.NetworkTicker;

import java.util.ArrayList;
import java.util.List;

public class WireNodeBlockEntity extends BlockEntityEnergized implements Tickable, CapacitorComponent, NetworkComponent {
	public int tier = -1;

	public static WireNodeBlockEntity lu;

	public List<WireNodeBlockEntity> children = new ArrayList<>();

	public WireNodeBlockEntity() {
		super(TenorBlockEntities.CONNECTOR_ENTITY_TYPE);
	}

	@Override
	public double getSquaredRenderDistance() {
		return Math.pow(2, 15);
	}

	@Override
	public void tick() {
		if (tier == -1) {
			tier = ((WireNodeBlock) this.getWorld().getBlockState(this.getPos()).getBlock()).tier;
		}
	}

	/**
	 * Implement UniversalComponents APIs.
	 */
	@Override
	public int getHarm() {
		return tier;
	}

	@Override
	public int getMaxEnergy() {
		return 4096 * (tier + 1);
	}

	/**
	 * Implement Network API.
	 */
	@Override
	public NetworkTicker getNetworkType() {
		return TenorEnergies.ENERGY_TYPE;
	}

	@Override
	public boolean accepts(Object... objects) {
		if (objects[0] != getNetworkType()) return false;

		for (int i = 1; i < objects.length; ++i) {
			if (objects[i] instanceof WireNodeBlock) {
				WireNodeBlock block = (WireNodeBlock) objects[i];
				if (tier != block.tier) return false;
			}
		}

		return true;
	}

	@Override
	public boolean isBuffer() {
		return true;
	}
}
