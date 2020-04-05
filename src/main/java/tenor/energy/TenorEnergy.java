package tenor.energy;

import io.github.cottonmc.component.energy.impl.ElectricalEnergyType;

public class TenorEnergy extends ElectricalEnergyType {
	@Override
	public float getEnergyPerFuelTick() {
		return 1.5f;
	}

	@Override
	public String getDisplaySubkey() {
		return "energy";
	}

	@Override
	public int getMaximumTransferSize() {
		return 0xffffffff;
	}

	@Override
	public int getMinimumTransferSize() {
		return 0x00000000;
	}
}
