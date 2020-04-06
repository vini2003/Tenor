package tenor.block.entity;

import io.github.cottonmc.component.api.ActionType;
import io.github.cottonmc.component.energy.CapacitorComponent;
import io.github.cottonmc.component.energy.CapacitorComponentHelper;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import tenor.block.WireNodeBlock;
import tenor.initialize.TenorBlockEntities;
import tenor.initialize.TenorEnergies;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public class WireNodeBlockEntity extends BlockEntityEnergized implements BlockEntityClientSerializable, Tickable, CapacitorComponent {
	public static WireNodeBlockEntity[] first = new WireNodeBlockEntity[2];
	public int tier = -1;
	public HashSet<BlockPos> children = new HashSet<>();
	public HashSet<BlockPos> parents = new HashSet<>();

	public WireNodeBlockEntity() {
		super(TenorBlockEntities.CONNECTOR_ENTITY_TYPE);
	}

	public static WireNodeBlockEntity getSelected(World world) {
		return world.isClient ? first[1] : first[0];
	}

	public static void setSelected(World world, WireNodeBlockEntity wireNodeBlockEntity) {
		first[world.isClient ? 1 : 0] = wireNodeBlockEntity;
	}

	public static LinkedHashSet<Vector3f> getSegments(WireNodeBlockEntity be, WireNodeBlockEntity ch) {
		LinkedHashSet<Vector3f> positions = new LinkedHashSet<>();

		if (ch == null || be == null) return positions;
		if (ch.world == null || be.world == null) return positions;

		double oY = WireNodeBlock.OFFSETS.get(((WireNodeBlock) be.getCachedState().getBlock()).tier);

		double x1 = be.getPos().getX();
		double y1 = be.getPos().getY();
		double z1 = be.getPos().getZ();

		switch (be.world.getBlockState(be.getPos()).get(WireNodeBlock.FACING)) {
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

		oY = WireNodeBlock.OFFSETS.get(((WireNodeBlock) ch.getCachedState().getBlock()).tier);

		if (!(ch.world.getBlockState(ch.getPos()).getBlock() instanceof WireNodeBlock)) {
			return positions;
		}

		switch (ch.world.getBlockState(ch.getPos()).get(WireNodeBlock.FACING)) {
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

		double x2 = ((x3 + x1)) / 2;
		double y2 = ((y1 + y3) / 2) - (Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2) + Math.pow(z3 - z1, 2)) * 0.1);

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

	@Override
	public double getSquaredRenderDistance() {
		return Math.pow(2, 15);
	}

	@Override
	public void tick() {
		if (world == null) {
			return;
		}

		if (tier == -1) {
			tier = ((WireNodeBlock) this.world.getBlockState(this.getPos()).getBlock()).tier;
		}

		BlockState connectorState = world.getBlockState(getPos());

		BlockPos attachedPos = getPos().offset(connectorState.get(WireNodeBlock.FACING).getOpposite());

		int available = Math.min(getMaxTransfer(), getCurrentEnergy());

		if (CapacitorComponentHelper.hasCapacitorComponent(world, attachedPos, connectorState.get(WireNodeBlock.FACING).getOpposite())) {
			CapacitorComponent component = CapacitorComponentHelper.getCapacitorComponent(world, attachedPos, connectorState.get(WireNodeBlock.FACING).getOpposite());

			if (component != null) {
				if (component.canInsertEnergy() && !component.canExtractEnergy()) {
					available -= (component.insertEnergy(TenorEnergies.ENERGY, Math.min(available, component.getMaxEnergy()), ActionType.PERFORM));
				} else if (!component.canInsertEnergy() && component.canExtractEnergy()) {
					available += component.extractEnergy(TenorEnergies.ENERGY, component.getCurrentEnergy(), ActionType.PERFORM);
				}
			}
		}

		if (!children.isEmpty()) {
			for (BlockPos position : this.children) {
				BlockEntity blockEntity = world.getBlockEntity(position);
				if (blockEntity != null) {
					BlockEntityEnergized child = (BlockEntityEnergized) blockEntity;
					available -= (available - child.insertEnergy(TenorEnergies.ENERGY, Math.min(available, child.getMaxTransfer()), ActionType.PERFORM));
				}
			}
		}

		if (!parents.isEmpty()) {
			for (BlockPos position : this.parents) {
				BlockEntity blockEntity = world.getBlockEntity(position);
				if (blockEntity != null) {
					BlockEntityEnergized child = (BlockEntityEnergized) blockEntity;
					available -= (available - child.insertEnergy(TenorEnergies.ENERGY, Math.min(available, child.getMaxTransfer()), ActionType.PERFORM));
				}
			}
		}

		this.energy = available;

		child_loop:
		for (BlockPos childPosition : children) {
			BlockEntity blockEntity = world.getBlockEntity(childPosition);

			if (blockEntity != null) {
				WireNodeBlockEntity child = (WireNodeBlockEntity) blockEntity;

				for (Vector3f position : WireNodeBlockEntity.getSegments(this, child)) {
					BlockPos blockPosition = new BlockPos(position.getX(), position.getY(), position.getZ());
					if (world.getBlockState(blockPosition).getBlock() != Blocks.AIR && !(world.getBlockState(blockPosition).getBlock() instanceof WireNodeBlock)) {
						Block.dropStack(world, blockPosition, new ItemStack(WireNodeBlock.COILS.get(tier)));
						this.children.remove(child.getPos());
						break child_loop;
					} else {
						List<Entity> entities = world.getEntities(null, new Box(position.getX(), position.getY(), position.getZ(), position.getX() - 0.5f, position.getY() - 0.5f, position.getZ() - 0.5f));
						for (Entity entity : entities) {
							if (entity instanceof LivingEntity) {
								entity.damage(DamageSource.CACTUS, (getHarm() + 1) * 4);
								entity.setOnFireFor(3);
							} else if (entity instanceof ItemEntity) {
								ItemEntity itemEntity = (ItemEntity) entity;

								BasicInventory temporaryInventory = new BasicInventory(1) {{
									setInvStack(0, itemEntity.getStack());
								}};

								Optional<SmeltingRecipe> matching = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, temporaryInventory, world);

								if (matching.isPresent()) {
									ItemScatterer.spawn(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), matching.get().craft(temporaryInventory));
									itemEntity.getStack().decrement(1);
								}
							}
						}
					}
				}
			}
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
	 * Implement persistent data (de)serialization.
	 */
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("tier", this.tier);

		CompoundTag childrenSubtag = new CompoundTag();

		final int[] i = {0};

		this.children.forEach(child -> {
			childrenSubtag.putIntArray(String.valueOf(i[0]), new int[]{child.getX(), child.getY(), child.getZ()});
			++i[0];
		});

		CompoundTag parentSubtag = new CompoundTag();

		final int[] k = {0};

		this.parents.forEach(parent -> {
			parentSubtag.putIntArray(String.valueOf(k[0]), new int[]{parent.getX(), parent.getY(), parent.getZ()});
			++k[0];
		});

		tag.put("children", childrenSubtag);
		tag.put("parents", parentSubtag);

		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.tier = tag.getInt("tier");

		CompoundTag childrenSubtag = (CompoundTag) tag.get("children");

		if (childrenSubtag != null) {
			for (int i = 0; i < childrenSubtag.getSize(); ++i) {
				int[] position = childrenSubtag.getIntArray(String.valueOf(i));
				this.children.add(new BlockPos(position[0], position[1], position[2]));
			}
		}

		CompoundTag parentSubtag = (CompoundTag) tag.get("parents");

		if (parentSubtag != null) {
			for (int k = 0; k < parentSubtag.getSize(); ++k) {
				int[] position = parentSubtag.getIntArray(String.valueOf(k));
				this.parents.add(new BlockPos(position[0], position[1], position[2]));
			}
		}

		super.fromTag(tag);
	}

	/**
	 * Implement client synchronization data (de)serialization.
	 */
	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		this.fromTag(compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return this.toTag(compoundTag);
	}
}
