package tenor.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import tenor.block.entity.WireNodeBlockEntity;
import tenor.initialize.TenorItems;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class WireNodeBlock extends Block implements BlockEntityProvider {
	public static final Map<Integer, Integer[]> COLORS = new HashMap<Integer, Integer[]>() {{
		put(0, new Integer[]{229, 130, 18, 255});
		put(1, new Integer[]{229, 190, 18, 255});
		put(2, new Integer[]{185, 239, 247, 255});
	}};

	public static final Map<Integer, Double> OFFSETS = new HashMap<Integer, Double>() {{
		put(0, 0.360);
		put(1, 0.600);
		put(2, 0.725);
	}};

	public static final Map<Integer, Item> COILS = new HashMap<Integer, Item>() {{
		put(0, TenorItems.COPPER_COIL);
		put(1, TenorItems.GOLD_COIL);
		put(2, TenorItems.FIBER_COIl);
	}};

	public static final Map<Integer, Map<Direction, VoxelShape>> SHAPES = new HashMap<Integer, Map<Direction, VoxelShape>>() {{
		put(0, new HashMap<Direction, VoxelShape>() {{
			VoxelShape DOWN = Block.createCuboidShape(6, 0, 6, 10, 3, 10);
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 4, 6, 10, 5, 10));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 4, 6, 10, 5, 10));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(7, 3, 7, 9, 4, 9));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(7, 5, 7, 9, 6, 9));


			VoxelShape UP = Block.createCuboidShape(6, 13, 6, 10, 16, 10);
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 11, 6, 10, 12, 10));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 11, 6, 10, 12, 10));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(7, 12, 7, 9, 13, 9));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(7, 10, 7, 9, 11, 9));


			VoxelShape EAST = Block.createCuboidShape(13, 6, 6, 16, 10, 10);
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(11, 6, 6, 12, 10, 10));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(11, 6, 6, 12, 10, 10));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(12, 7, 7, 13, 9, 9));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(10, 7, 7, 11, 9, 9));


			VoxelShape WEST = Block.createCuboidShape(0, 6, 6, 3, 10, 10);
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(4, 6, 6, 5, 10, 10));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(4, 6, 6, 5, 10, 10));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(3, 7, 7, 4, 9, 9));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(5, 7, 7, 6, 9, 9));


			VoxelShape SOUTH = Block.createCuboidShape(6, 6, 13, 10, 10, 16);
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 11, 10, 10, 12));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 11, 10, 10, 12));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(7, 7, 12, 9, 9, 13));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(7, 7, 10, 9, 9, 11));


			VoxelShape NORTH = Block.createCuboidShape(6, 6, 0, 10, 10, 3);
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 4, 10, 10, 5));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 4, 10, 10, 5));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(7, 7, 3, 9, 9, 4));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(7, 7, 5, 9, 9, 6));

			put(Direction.DOWN, DOWN);
			put(Direction.UP, UP);
			put(Direction.EAST, EAST);
			put(Direction.WEST, WEST);
			put(Direction.SOUTH, SOUTH);
			put(Direction.NORTH, NORTH);
		}});

		put(1, new HashMap<Direction, VoxelShape>() {{
			VoxelShape DOWN = Block.createCuboidShape(6, 0, 6, 10, 2, 10);
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(5, 0, 5, 11, 5, 11));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(5, 6, 5, 11, 7, 11));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(5, 8, 5, 11, 9, 11));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 5, 6, 10, 6, 10));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 7, 6, 10, 8, 10));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 9, 6, 10, 10, 10));


			VoxelShape UP = Block.createCuboidShape(6, 14, 6, 10, 16, 10);
			UP = VoxelShapes.union(UP, Block.createCuboidShape(5, 11, 5, 11, 16, 11));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(5, 9, 5, 11, 10, 11));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(5, 7, 5, 11, 8, 11));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 10, 6, 10, 11, 10));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 8, 6, 10, 9, 10));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 6, 6, 10, 7, 10));


			VoxelShape EAST = Block.createCuboidShape(14, 6, 6, 16, 10, 10);
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(11, 5, 5, 16, 11, 11));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(9, 5, 5, 10, 11, 11));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(7, 5, 5, 8, 11, 11));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(10, 6, 6, 11, 10, 10));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(8, 6, 6, 9, 10, 10));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(6, 6, 6, 7, 10, 10));


			VoxelShape WEST = Block.createCuboidShape(0, 6, 6, 2, 10, 10);
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(0, 5, 5, 5, 11, 11));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(6, 5, 5, 7, 11, 11));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(8, 5, 5, 9, 11, 11));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(5, 6, 6, 6, 10, 10));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(7, 6, 6, 8, 10, 10));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(9, 6, 6, 10, 10, 10));


			VoxelShape SOUTH = Block.createCuboidShape(6, 6, 14, 10, 10, 16);
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(5, 5, 11, 11, 11, 16));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(5, 5, 9, 11, 11, 10));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(5, 5, 7, 11, 11, 8));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 10, 10, 10, 11));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 8, 10, 10, 9));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 6, 10, 10, 7));


			VoxelShape NORTH = Block.createCuboidShape(6, 6, 0, 10, 10, 2);
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(5, 5, 0, 11, 11, 5));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(5, 5, 6, 11, 11, 7));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(5, 5, 8, 11, 11, 9));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 5, 10, 10, 6));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 7, 10, 10, 8));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 9, 10, 10, 10));

			put(Direction.DOWN, DOWN);
			put(Direction.UP, UP);
			put(Direction.EAST, EAST);
			put(Direction.WEST, WEST);
			put(Direction.SOUTH, SOUTH);
			put(Direction.NORTH, NORTH);
		}});

		put(2, new HashMap<Direction, VoxelShape>() {{
			VoxelShape DOWN = Block.createCuboidShape(5, 0, 5, 11, 5, 11);
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(5, 6, 5, 11, 7, 11));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(5, 8, 5, 11, 9, 11));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(5, 10, 5, 11, 11, 11));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 5, 6, 10, 6, 10));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 7, 6, 10, 8, 10));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 9, 6, 10, 10, 10));
			DOWN = VoxelShapes.union(DOWN, Block.createCuboidShape(6, 11, 6, 10, 12, 10));


			VoxelShape UP = Block.createCuboidShape(5, 11, 5, 11, 16, 11);
			UP = VoxelShapes.union(UP, Block.createCuboidShape(5, 9, 5, 11, 10, 11));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(5, 7, 5, 11, 8, 11));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(5, 5, 5, 11, 6, 11));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 10, 6, 10, 11, 10));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 8, 6, 10, 9, 10));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 6, 6, 10, 7, 10));
			UP = VoxelShapes.union(UP, Block.createCuboidShape(6, 4, 6, 10, 5, 10));


			VoxelShape EAST = Block.createCuboidShape(11, 5, 5, 16, 11, 11);
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(9, 5, 5, 10, 11, 11));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(7, 5, 5, 8, 11, 11));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(5, 5, 5, 6, 11, 11));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(10, 6, 6, 11, 10, 10));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(8, 6, 6, 9, 10, 10));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(6, 6, 6, 7, 10, 10));
			EAST = VoxelShapes.union(EAST, Block.createCuboidShape(4, 6, 6, 5, 10, 10));


			VoxelShape WEST = Block.createCuboidShape(0, 5, 5, 5, 11, 11);
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(6, 5, 5, 7, 11, 11));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(8, 5, 5, 9, 11, 11));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(10, 5, 5, 11, 11, 11));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(5, 6, 6, 6, 10, 10));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(7, 6, 6, 8, 10, 10));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(9, 6, 6, 10, 10, 10));
			WEST = VoxelShapes.union(WEST, Block.createCuboidShape(11, 6, 6, 12, 10, 10));


			VoxelShape SOUTH = Block.createCuboidShape(5, 5, 11, 11, 11, 16);
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(5, 5, 9, 11, 11, 10));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(5, 5, 7, 11, 11, 8));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(5, 5, 5, 11, 11, 6));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 10, 10, 10, 11));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 8, 10, 10, 9));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 6, 10, 10, 7));
			SOUTH = VoxelShapes.union(SOUTH, Block.createCuboidShape(6, 6, 4, 10, 10, 5));


			VoxelShape NORTH = Block.createCuboidShape(5, 5, 0, 11, 11, 5);
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(5, 5, 6, 11, 11, 7));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(5, 5, 8, 11, 11, 9));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(5, 5, 10, 11, 11, 11));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 5, 10, 10, 6));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 7, 10, 10, 8));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 9, 10, 10, 10));
			NORTH = VoxelShapes.union(NORTH, Block.createCuboidShape(6, 6, 11, 10, 10, 12));

			put(Direction.DOWN, DOWN);
			put(Direction.UP, UP);
			put(Direction.EAST, EAST);
			put(Direction.WEST, WEST);
			put(Direction.SOUTH, SOUTH);
			put(Direction.NORTH, NORTH);
		}});
	}};

	public static final DirectionProperty FACING = FacingBlock.FACING;

	public Integer tier;

	public WireNodeBlock(Integer tier, Settings settings) {
		super(settings);
		this.tier = tier;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new WireNodeBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (WireNodeBlockEntity.getSelected(world) != null) {
			if (WireNodeBlock.COILS.get(tier) == player.getStackInHand(hand).getItem()) {
				WireNodeBlockEntity second = (WireNodeBlockEntity) world.getBlockEntity(pos);

				if (second == null) return ActionResult.FAIL;

				LinkedHashSet<Vector3f> positions = WireNodeBlockEntity.getSegments(WireNodeBlockEntity.getSelected(world), second);

				for (Vector3f position : positions) {
					BlockPos blockPosition = new BlockPos(position.getX(), position.getY(), position.getZ());

					if (world.getBlockState(blockPosition).getBlock() != Blocks.AIR && !(world.getBlockState(blockPosition).getBlock() instanceof WireNodeBlock)) {
						player.addChatMessage(new TranslatableText("text.tenor.connection_failed"), true);
						world.addParticle(ParticleTypes.BARRIER, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), 0, 0, 0);
						WireNodeBlockEntity.setSelected(world, null);
						return ActionResult.FAIL;
					}
				}

				player.addChatMessage(new TranslatableText("text.tenor.connected_connectors_one")
						.append(WireNodeBlockEntity.getSelected(world).getPos().toString())
						.append(new TranslatableText("text.tenor.connected_connectors_two"))
						.append(pos.toString())
						.append(new TranslatableText("text.tenor.connected_connectors_three")), true);

				second.parents.add(WireNodeBlockEntity.getSelected(world).getPos());
				WireNodeBlockEntity.getSelected(world).children.add(second.getPos());

				WireNodeBlockEntity.getSelected(world).markDirty();
				second.markDirty();

				WireNodeBlockEntity.setSelected(world, null);

				player.getStackInHand(hand).decrement(1);
				return ActionResult.SUCCESS;
			} else {
				WireNodeBlockEntity.setSelected(world, null);
				return ActionResult.FAIL;
			}

		} else {
			if (WireNodeBlock.COILS.get(tier) == player.getStackInHand(hand).getItem()) {
				player.addChatMessage(new TranslatableText("text.tenor.selected_connectors_one").append(pos.toString()).append(new TranslatableText("text.tenor.selected_connectors_two")), true);
				WireNodeBlockEntity.setSelected(world, (WireNodeBlockEntity) world.getBlockEntity(pos));
				return ActionResult.SUCCESS;
			} else {
				WireNodeBlockEntity.setSelected(world, null);
				return ActionResult.FAIL;
			}
		}
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		WireNodeBlockEntity be = (WireNodeBlockEntity) blockEntity;

		be.parents.forEach(parentPosition -> {
			WireNodeBlockEntity parent = (WireNodeBlockEntity) world.getBlockEntity(parentPosition);

			if (parent != null) parent.children.remove(blockEntity.getPos());
		});

		be.children.forEach(childPosition -> {
			WireNodeBlockEntity child = (WireNodeBlockEntity) world.getBlockEntity(childPosition);

			if (child != null) child.parents.remove(blockEntity.getPos());
		});

		super.afterBreak(world, player, pos, state, blockEntity, stack);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos) {
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
		return SHAPES.get(tier).get(state.get(FACING).getOpposite());
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockView blockView, BlockPos blockPosition, EntityContext entityContext) {
		return this.collidable ? blockState.getOutlineShape(blockView, blockPosition) : VoxelShapes.empty();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction direction = context.getSide();
		BlockState blockState = context.getWorld().getBlockState(context.getBlockPos().offset(direction.getOpposite()));
		return blockState.getBlock() == this && blockState.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite()) : this.getDefaultState().with(FACING, direction);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING);
	}
}
