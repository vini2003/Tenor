package tenor.block.entity;

import io.github.cottonmc.component.api.ActionType;
import io.github.cottonmc.component.energy.CapacitorComponent;
import io.github.cottonmc.component.energy.CapacitorComponentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import tenor.block.WireNodeBlock;
import tenor.initialize.TenorBlockEntities;
import tenor.initialize.TenorEnergies;
import tenor.initialize.TenorItems;

import java.util.*;

public class WireNodeBlockEntity extends BlockEntityEnergized implements Tickable, CapacitorComponent {
	public int tier = -1;

	public static WireNodeBlockEntity[] first = new WireNodeBlockEntity[2];

	public HashSet<WireNodeBlockEntity> children = new HashSet<>();
	public HashSet<WireNodeBlockEntity> parents = new HashSet<>();

	public WireNodeBlockEntity() {
		super(TenorBlockEntities.CONNECTOR_ENTITY_TYPE);
	}

	public static WireNodeBlockEntity getSelected(World world) {
		return world.isClient ? first[1] : first[0];
	}

	public static void setSelected(World world, WireNodeBlockEntity wireNodeBlockEntity) {
		first[world.isClient ? 1 : 0] = wireNodeBlockEntity;
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

		/**
		 * Firstly, obtain two things:
		 * - Attached connectors;
		 * - Attached entity;
		 *
		 * Afterwards, verify:
		 * - Can entity receive?
		 * - Can entity emit?
		 *
		 * If it can receive, but not emit, prioritize inserting into it.
		 * If it can emit, but not receive, prioritize extracting from it.
		 *
		 * Afterwards, balance the attached connectors - both children and parents.
		 */

		BlockState connectorState = getWorld().getBlockState(getPos());

		BlockPos attachedPos = getPos().offset(connectorState.get(WireNodeBlock.FACING).getOpposite());
		BlockState attachedState = getWorld().getBlockState(attachedPos);
		Block attachedBlock = attachedState.getBlock();

		int available = Math.min(getMaxTransfer(), getMaxEnergy() - getCurrentEnergy());

		if (CapacitorComponentHelper.hasCapacitorComponent(world, attachedPos, connectorState.get(WireNodeBlock.FACING).getOpposite())) {
			CapacitorComponent component = CapacitorComponentHelper.getCapacitorComponent(world, attachedPos, connectorState.get(WireNodeBlock.FACING).getOpposite());

			if (component.canInsertEnergy() && !component.canExtractEnergy()) {
				available -= (available - component.insertEnergy(TenorEnergies.ENERGY, component.getMaxEnergy(), ActionType.PERFORM));
			} else if (!component.canInsertEnergy() && component.canExtractEnergy()) {
				available += component.extractEnergy(TenorEnergies.ENERGY, component.getCurrentEnergy(), ActionType.PERFORM);
			}
		}

		if (!children.isEmpty()) {
			for (BlockEntityEnergized child : this.children) {
				available -= (available - child.insertEnergy(TenorEnergies.ENERGY, Math.min(available, child.getMaxTransfer()), ActionType.PERFORM));
			}
		}

		this.energy = available;

		child_loop: for (WireNodeBlockEntity child : children) {
			for (Vector3f position : WireNodeBlockEntity.getSegments(this, child)) {
				BlockPos blockPosition = new BlockPos(position.getX(), position.getY(), position.getZ());
				if (world.getBlockState(blockPosition).getBlock() != Blocks.AIR && !(world.getBlockState(blockPosition).getBlock() instanceof WireNodeBlock)) {
					if (this.tier == 0) {
						Block.dropStack(world, blockPosition, new ItemStack(TenorItems.COPPER_COIL));
					} else if (this.tier == 1) {
						Block.dropStack(world, blockPosition, new ItemStack(TenorItems.GOLD_COIL));
					} else if (this.tier == 2) {
						Block.dropStack(world, blockPosition, new ItemStack(TenorItems.FIBER_COIl));
					}

					this.children.remove(child);
					break child_loop;
				} else {
					List<Entity> entities = world.getEntities(null, new Box(position.getX(), position.getY(), position.getZ(), position.getX() + 1, position.getY() + 1, position.getZ() + 1));
					for (Entity entity : entities) {
						if (entity instanceof LivingEntity) {
							entity.damage(DamageSource.CACTUS, (getHarm() + 1) * 4);
						}
					}
				}
			}
		}
	}

	public static LinkedHashSet<Vector3f> getSegments(WireNodeBlockEntity be, WireNodeBlockEntity ch) {
		LinkedHashSet<Vector3f> positions = new LinkedHashSet<>();

		double oY = WireNodeBlock.OFFSETS.get(((WireNodeBlock) be.getCachedState().getBlock()).tier).get(0);

		double x1 = be.getPos().getX();
		double y1 = be.getPos().getY();
		double z1 = be.getPos().getZ();

		switch (be.getWorld().getBlockState(be.getPos()).get(WireNodeBlock.FACING)) {
			case NORTH:
				x1 += 0.5f;
				y1 += 0.5f;
				z1 += 1f - oY;
				break;
			case SOUTH:
				x1 += 0.5f;
				y1 += 0.5f;
				z1 += oY;
				break;
			case WEST:
				x1 += 1f - oY;
				y1 += 0.5f;
				z1 += 0.5f;
				break;
			case EAST:
				x1 += oY;
				y1 += 0.5f;
				z1 += 0.5f;
				break;
			case UP:
				x1 += 0.5f;
				y1 += oY;
				z1 += 0.5f;
				break;
			case DOWN:
				x1 += 0.5f;
				y1 += 1 - oY;
				z1 += 0.5f;
				break;
		}

		double x3 = ch.getPos().getX();
		double y3 = ch.getPos().getY();
		double z3 = ch.getPos().getZ();

		oY = WireNodeBlock.OFFSETS.get(((WireNodeBlock) ch.getCachedState().getBlock()).tier).get(0);

		if (!(ch.getWorld().getBlockState(ch.getPos()).getBlock() instanceof WireNodeBlock)) {
			return positions;
		}

		switch (ch.getWorld().getBlockState(ch.getPos()).get(WireNodeBlock.FACING)) {
			case NORTH:
				x3 += 0.5f;
				y3 += 0.5f;
				z3 += 1f - oY;
				break;
			case SOUTH:
				x3 += 0.5f;
				y3 += 0.5f;
				z3 += oY;
				break;
			case WEST:
				x3 += 1f - oY;
				y3 += 0.5f;
				z3 += 0.5f;
				break;
			case EAST:
				x3 += oY;
				y3 += 0.5f;
				z3 += 0.5f;
				break;
			case UP:
				x3 += 0.5f;
				y3 += oY;
				z3 += 0.5f;
				break;
			case DOWN:
				x3 += 0.5f;
				y3 += 1 - oY;
				z3 += 0.5f;
				break;
		}

		double x2 = x3 < 0 ? ((x3 - x1) / 2) : ((x3 + x1)) / 2;
		double y2 = ((y1 + y3) / 2) - (0.015 * (y1 + y3));

		double dZ = (z3 - z1) / 100;
		double cZ = 0;


		for (double t = 0; t < 1; t += 0.01) {
			// C(t)=(1−t)2P0+2t(1−t)P1+t2P2

			double p0M = Math.pow(1 - t, 2);
			double p0X = x1 * p0M;
			double p0Y = y1 * p0M;

			double p1M = 2 * t * (1 - t);
			double p1X = p1M * x2;
			double p1Y = p1M * y2;

			double p2M = Math.pow(t, 2);
			double p2X = p2M * x3;
			double p2Y = p2M * y3;

			double pX = p0X + p1X + p2X;
			double pY = p0Y + p1Y + p2Y;

			positions.add(new Vector3f((float) pX, (float) pY, (float) ((float) z1 + cZ)));

			cZ += dZ;
		}

		return positions;
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

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag childrenSubtag = new CompoundTag();

		final int[] i = {0};

		this.children.forEach(child -> {
			childrenSubtag.putIntArray(String.valueOf(i[0]), new int[]{child.getPos().getX(), child.getPos().getY(), child.getPos().getZ()});
			++i[0];
		});

		CompoundTag parentSubtag = new CompoundTag();

		final int[] k = {0};

		this.parents.forEach(parent -> {
			parentSubtag.putIntArray(String.valueOf(k), new int[]{parent.getPos().getX(), parent.getPos().getY(), parent.getPos().getZ()});
			++k[0];
		});

		tag.put("children", childrenSubtag);
		tag.put("parents", parentSubtag);

		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag childrenSubtag = (CompoundTag) tag.get("children");

		for (int i = 0; i < childrenSubtag.getSize(); ++i) {
			int[] position = childrenSubtag.getIntArray(String.valueOf(i));
			this.children.add((WireNodeBlockEntity) this.world.getBlockEntity(new BlockPos(position[0], position[1], position[2])));
		}

		CompoundTag parentSubtag = (CompoundTag) tag.get("parents");

		for (int k = 0; k < parentSubtag.getSize(); ++k) {
			int[] position = parentSubtag.getIntArray(String.valueOf(k));
			this.children.add((WireNodeBlockEntity) this.world.getBlockEntity(new BlockPos(position[0], position[1], position[2])));
		}

		super.fromTag(tag);
	}
}
