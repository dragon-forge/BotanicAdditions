package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
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
		super(props);
		TagAdapter.bind(BlockTags.MINEABLE_WITH_HOE, this);
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