package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.tiles.TileDreamingPool;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.decor.BotaniaMushroomBlock;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.item.material.MysticalPetalItem;

import java.util.List;
import java.util.Optional;

import static vazkii.botania.api.state.BotaniaStateProperties.OPTIONAL_DYE_COLOR;

public class BlockDreamingPool
		extends BotaniaWaterloggedBlock
		implements EntityBlock
{
	private static final VoxelShape NORMAL_SHAPE;
	private static final VoxelShape NORMAL_SHAPE_BURST;
	
	static
	{
		NORMAL_SHAPE_BURST = box(0, 0, 0, 16, 10, 16);
		VoxelShape cutout = box(2, 2, 2, 14, 16, 14);
		NORMAL_SHAPE = Shapes.join(NORMAL_SHAPE_BURST, cutout, BooleanOp.ONLY_FIRST);
	}
	
	public BlockDreamingPool(Properties builder)
	{
		super(builder);
		registerDefaultState(defaultBlockState().setValue(OPTIONAL_DYE_COLOR, BotaniaStateProperties.OptionalDyeColor.NONE));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flags)
	{
		tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".dreaming_pool")
				.withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
		);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(OPTIONAL_DYE_COLOR);
	}
	
	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
	{
		return NORMAL_SHAPE;
	}
	
	@NotNull
	@Override
	public InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit)
	{
		BlockEntity be = world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		Optional<DyeColor> itemColor = Optional.empty();
		if(stack.getItem() instanceof MysticalPetalItem petalItem)
			itemColor = Optional.of(petalItem.color);
		if(Block.byItem(stack.getItem()) instanceof BotaniaMushroomBlock mushroomBlock)
			itemColor = Optional.of(mushroomBlock.color);
		if(itemColor.isPresent() && be instanceof ManaPoolBlockEntity pool)
		{
			if(!itemColor.equals(pool.getColor()))
			{
				pool.setColor(itemColor);
				if(!player.getAbilities().instabuild) stack.shrink(1);
				return InteractionResult.sidedSuccess(world.isClientSide());
			}
		}
		if(stack.is(Items.CLAY_BALL) && be instanceof ManaPoolBlockEntity pool && pool.getColor().isPresent())
		{
			pool.setColor(Optional.empty());
			if(!player.getAbilities().instabuild) stack.shrink(1);
			return InteractionResult.sidedSuccess(world.isClientSide());
		}
		return super.use(state, world, pos, player, hand, hit);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		if(context instanceof EntityCollisionContext econtext
				&& econtext.getEntity() instanceof ManaBurstEntity)
		{
			// Sometimes the pool's collision box is too thin for bursts shot straight up.
			return NORMAL_SHAPE_BURST;
		} else
		{
			return super.getCollisionShape(state, world, pos, context);
		}
	}
	
	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return new TileDreamingPool(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, TilesBA.DREAMING_POOL, level.isClientSide ? ManaPoolBlockEntity::clientTick : ManaPoolBlockEntity::serverTick);
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity)
	{
		if(entity instanceof ItemEntity item)
		{
			ManaPoolBlockEntity tile = (ManaPoolBlockEntity) world.getBlockEntity(pos);
			tile.collideEntityItem(item);
		}
	}
	
	@NotNull
	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos)
	{
		ManaPoolBlockEntity pool = (ManaPoolBlockEntity) world.getBlockEntity(pos);
		return ManaPoolBlockEntity.calculateComparatorLevel(pool.getCurrentMana(), pool.getMaxMana());
	}
}