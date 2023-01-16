package org.zeith.botanicadds.blocks.flowers.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.blocks.ForgeSpecialFlowerBlockBA;
import org.zeith.botanicadds.blocks.flowers.Vibrantia;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.SculkSensorBlock.PHASE;

public class VibrantiaBlock
		extends ForgeSpecialFlowerBlockBA
{
	public VibrantiaBlock(MobEffect stewEffect, int stewDuration, Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType)
	{
		super(stewEffect, stewDuration, props, blockEntityType);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(SculkSensorBlock.PHASE);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource rng)
	{
		if(SculkSensorBlock.getPhase(state) != SculkSensorPhase.ACTIVE)
		{
			if(SculkSensorBlock.getPhase(state) == SculkSensorPhase.COOLDOWN)
				lvl.setBlock(pos, state.setValue(SculkSensorBlock.PHASE, SculkSensorPhase.INACTIVE), 3);
		} else
			deactivate(lvl, pos, state);
	}
	
	public static void deactivate(Level level, BlockPos pos, BlockState state)
	{
		level.setBlock(pos, state.setValue(PHASE, SculkSensorPhase.COOLDOWN), 3);
		level.scheduleTick(pos, state.getBlock(), 1);
		level.playSound(null, pos, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
		updateNeighbours(level, pos);
	}
	
	public static void activate(@Nullable Entity entity, Level level, BlockPos pos, BlockState state)
	{
		level.setBlock(pos, state.setValue(PHASE, SculkSensorPhase.ACTIVE), 3);
		level.scheduleTick(pos, state.getBlock(), 40);
		updateNeighbours(level, pos);
		level.gameEvent(entity, GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos);
		level.playSound(null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
	}
	
	private static void updateNeighbours(Level level, BlockPos pos)
	{
		level.updateNeighborsAt(pos, Blocks.SCULK_SENSOR);
		level.updateNeighborsAt(pos.relative(Direction.UP.getOpposite()), Blocks.SCULK_SENSOR);
	}
	
	
	@Nullable
	@Override
	public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T be)
	{
		return be instanceof Vibrantia vib ? vib.getListener() : null;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		return state.is(Blocks.SCULK)
				|| super.mayPlaceOn(state, worldIn, pos);
	}
}