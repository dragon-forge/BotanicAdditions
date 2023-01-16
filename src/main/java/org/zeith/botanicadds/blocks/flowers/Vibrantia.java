package org.zeith.botanicadds.blocks.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.GenerationalFlowerHUD;
import org.zeith.botanicadds.blocks.flowers.base.VibrantiaBlock;
import org.zeith.botanicadds.init.FlowersBA;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.internal.ManaBurst;

@GenerationalFlowerHUD
public class Vibrantia
		extends GeneratingFlowerBlockEntity
		implements VibrationListener.VibrationListenerConfig
{
	protected int lastVibrationFrequency;
	
	protected boolean hasOvergrownSoil;
	protected int activeTicks;
	
	protected VibrationListener listener;
	
	public Vibrantia(BlockPos pos, BlockState state)
	{
		super(FlowersBA.VIBRANTIA_TYPE, pos, state);
		
		this.listener = new VibrationListener(new BlockPositionSource(this.worldPosition), 8, this, null, 0.0F, 0);
	}
	
	public VibrationListener getListener()
	{
		return listener;
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
		if(!level.isClientSide)
			listener.tick(level);
		
		hasOvergrownSoil = overgrowth;
		
		if(activeTicks > 0 && !overgrowthBoost)
		{
			var ticksExisted = level.getGameTime();
			if((hasOvergrownSoil && ticksExisted % 3 != 0) || ticksExisted % 4 == 0)
			{
				addMana(1);
				sync();
			}
			--activeTicks;
		}
	}
	
	@Override
	public int getMaxMana()
	{
		return hasOvergrownSoil ? 2000 : 1000;
	}
	
	@Override
	public int getColor()
	{
		return 0x1D7589;
	}
	
	@Override
	public @Nullable RadiusDescriptor getRadius()
	{
		return new RadiusDescriptor.Circle(worldPosition, 8);
	}
	
	@Override
	public boolean canTriggerAvoidVibration()
	{
		return true;
	}
	
	@Override
	public boolean shouldListen(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable GameEvent.Context context)
	{
		if(context != null && context.sourceEntity() instanceof ManaBurst) return false;
		if(event == GameEvent.PROJECTILE_SHOOT && (context == null || context.sourceEntity() == null)) return false;
		
		return !this.isRemoved() && (!pos.equals(this.getBlockPos()) || event != GameEvent.BLOCK_DESTROY && event != GameEvent.BLOCK_PLACE)
				&& SculkSensorBlock.getPhase(getBlockState()) == SculkSensorPhase.INACTIVE;
	}
	
	@Override
	public void onSignalReceive(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity ent, @Nullable Entity ent2, float dist)
	{
		// Ignore mana-burst events
		if(ent instanceof ManaBurst) return;
		if(event == GameEvent.PROJECTILE_SHOOT && ent == null && ent2 == null) return;
		
		BlockState blockstate = this.getBlockState();
		if(SculkSensorBlock.canActivate(blockstate))
		{
			this.lastVibrationFrequency = SculkSensorBlock.VIBRATION_FREQUENCY_FOR_EVENT.getInt(event);
			this.activeTicks = 40;
			VibrantiaBlock.activate(ent, level, this.worldPosition, blockstate);
		}
	}
	
	@Override
	public void onSignalSchedule()
	{
		this.setChanged();
	}
}