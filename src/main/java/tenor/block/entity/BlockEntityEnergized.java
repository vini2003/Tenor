package tenor.block.entity;

import io.github.cottonmc.component.api.ActionType;
import io.github.cottonmc.component.energy.CapacitorComponent;
import io.github.cottonmc.component.energy.type.EnergyType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import tenor.initialize.TenorEnergies;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockEntityEnergized extends BlockEntity implements CapacitorComponent {
	public int energy = 0;
	
	public int minEnergy = 0;
	public int maxEnergy = Integer.MAX_VALUE;

	public int minTransfer = 0;
	public int maxTransfer = Integer.MAX_VALUE;

	public List<Runnable> listeners = new ArrayList<>();

	public BlockEntityEnergized(BlockEntityType<?> type) {
		super(type);
	}

	public int increment(int amount) {
		int stored = energy;
		int max = maxEnergy;

		int available = max - stored;

		if (amount <= available) {
			this.energy = stored + amount;
			return 0;
		} else {
			int overflow = amount - available;
			this.energy = energy + amount - overflow;
			return overflow;
		}
	}

	public int decrement(int amount) {
		int stored = energy;
		int max = maxEnergy;

		int available = energy;

		if (available >= amount) {
			this.energy = stored - amount;
			return 0;
		} else {
			int underflow = available - amount;
			this.energy = energy - amount - underflow;
			return -underflow;
		}
	}

	// getMaxEnergy()

	@Override
	public int getCurrentEnergy() {
		return energy;
	}

	@Override
	public boolean canInsertEnergy() {
		return energy < maxEnergy;
	}

	@Override
	public int insertEnergy(EnergyType type, int amount, ActionType action) {
		return increment(type.convertTo(TenorEnergies.ENERGY, amount));
	}

	@Override
	public boolean canExtractEnergy() {
		return energy > minEnergy;
	}

	@Override
	public int extractEnergy(EnergyType type, int amount, ActionType action) {
		return decrement(type.convertTo(TenorEnergies.ENERGY, amount));
	}

	@Override
	public EnergyType getPreferredType() {
		return TenorEnergies.ENERGY;
	}

	// getHarm()

	@Override
	public List<Runnable> getListeners() {
		return listeners;
	}

	public void addListener(Runnable listener) {
		this.listeners.add(listener);
	}

	public void removeListener(Runnable listener) {
		this.listeners.remove(listener);
	}

	public int getMaxTransfer() {
		return maxTransfer;
	}

	public int getMinTransfer() {
		return minTransfer;
	}
}
