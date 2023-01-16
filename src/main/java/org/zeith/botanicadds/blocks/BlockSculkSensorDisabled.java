package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.core.adapter.TagAdapter;

import javax.annotation.Nullable;

public class BlockSculkSensorDisabled
		extends Block
		implements SimpleWaterloggedBlock, ICreativeTabBlock
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	
	public BlockSculkSensorDisabled(Properties props)
	{
		super(props.randomTicks());
		TagAdapter.bind(BlockTags.MINEABLE_WITH_HOE, this);
	}
	
	public static int generateRegrowDelay(RandomSource rng)
	{
		return 6000 + rng.nextInt(6000);
	}
	
	public boolean reduce(Level level, BlockPos pos)
	{
		var state = level.getBlockState(pos);
		if(state.is(Blocks.SCULK_SENSOR))
		{
			var hasWater = state.getValue(SculkSensorBlock.WATERLOGGED);
			var replaceState = defaultBlockState().setValue(WATERLOGGED, hasWater);
			level.setBlockAndUpdate(pos, replaceState);
			level.scheduleTick(pos, this, generateRegrowDelay(level.random));
			level.gameEvent(GameEvent.SHEAR, pos, new GameEvent.Context(null, state));
			return true;
		}
		return false;
	}
	
	public boolean regrow(Level level, BlockPos pos)
	{
		var state = level.getBlockState(pos);
		if(state.is(this))
		{
			var hasWater = state.getValue(WATERLOGGED);
			var replaceState = Blocks.SCULK_SENSOR.defaultBlockState().setValue(SculkSensorBlock.WATERLOGGED, hasWater);
			level.setBlockAndUpdate(pos, replaceState);
			level.playSound(null, pos, SoundEvents.SCULK_SENSOR_PLACE, SoundSource.BLOCKS, 1F, 1.6F);
			return true;
		}
		return false;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rng)
	{
		if(rng.nextInt(5) == 0)
			onTick(state, level, pos, rng, true);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rng)
	{
		onTick(state, level, pos, rng, false);
	}
	
	public void onTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rng, boolean wasRandom)
	{
		if(rng.nextInt(15) == 0)
		{
			regrow(level, pos);
		} else if(!wasRandom)
			level.scheduleTick(pos, state.getBlock(), generateRegrowDelay(rng));
	}
	
	@Override
	public CreativeModeTab getCreativeTab()
	{
		return BotanicAdditions.TAB;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext p_154396_)
	{
		BlockPos blockpos = p_154396_.getClickedPos();
		FluidState fluidstate = p_154396_.getLevel().getFluidState(blockpos);
		return this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}
	
	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState updateShape(BlockState p_154457_, Direction p_154458_, BlockState p_154459_, LevelAccessor p_154460_, BlockPos p_154461_, BlockPos p_154462_)
	{
		if(p_154457_.getValue(WATERLOGGED))
			p_154460_.scheduleTick(p_154461_, Fluids.WATER, Fluids.WATER.getTickDelay(p_154460_));
		return super.updateShape(p_154457_, p_154458_, p_154459_, p_154460_, p_154461_, p_154462_);
	}
	
	@Override
	public VoxelShape getShape(BlockState p_154432_, BlockGetter p_154433_, BlockPos p_154434_, CollisionContext p_154435_)
	{
		return SHAPE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_154464_)
	{
		p_154464_.add(WATERLOGGED);
	}
	
	@Override
	public boolean isPathfindable(BlockState p_154427_, BlockGetter p_154428_, BlockPos p_154429_, PathComputationType p_154430_)
	{
		return false;
	}
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState p_154486_)
	{
		return true;
	}
	
	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel)
	{
		return silkTouchLevel == 0 ? 5 : 0;
	}
}