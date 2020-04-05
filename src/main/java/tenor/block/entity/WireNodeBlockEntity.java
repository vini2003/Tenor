package tenor.block.entity;


import net.minecraft.block.entity.BlockEntity;
import org.apache.commons.lang3.tuple.MutablePair;
import tenor.initialize.TenorBlockEntities;

import java.util.ArrayList;
import java.util.List;

public class WireNodeBlockEntity extends BlockEntity {
	public static WireNodeBlockEntity lu;

	public List<MutablePair<Double, WireNodeBlockEntity>> children = new ArrayList<>();

	public WireNodeBlockEntity() {
		super(TenorBlockEntities.CONNECTOR_ENTITY_TYPE);
	}

	@Override
	public double getSquaredRenderDistance() {
		return Math.pow(2, 15);
	}
}
