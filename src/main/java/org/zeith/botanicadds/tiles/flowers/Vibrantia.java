package org.zeith.botanicadds.tiles.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.FlowerHUD;
import org.zeith.botanicadds.blocks.flowers.VibrantiaBlock;
import org.zeith.botanicadds.init.FlowersBA;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.internal.ManaBurst;

@FlowerHUD
public class Vibrantia
		extends GeneratingFlowerBlockEntity
		implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem
{
	protected int lastVibrationFrequency;
	
	protected boolean hasOvergrownSoil;
	protected int activeTicks;
	
	private VibrationSystem.Data vibrationData;
	private final VibrationSystem.Listener vibrationListener;
	private final VibrationSystem.User vibrationUser = this.createVibrationUser();
	
	public Vibrantia(BlockPos pos, BlockState state)
	{
		super(FlowersBA.VIBRANTIA_TYPE, pos, state);
		
		this.vibrationData = new VibrationSystem.Data();
		this.vibrationListener = new VibrationSystem.Listener(this);
	}
	
	public VibrationSystem.User createVibrationUser()
	{
		return new VibrationUser(this.getBlockPos());
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
		VibrationSystem.Ticker.tick(level, getVibrationData(), getVibrationUser());
		
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
	public boolean isOvergrowthAffected()
	{
		return false;
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
	
	public VibrationSystem.Data getVibrationData() {
		return this.vibrationData;
	}
	
	public VibrationSystem.User getVibrationUser() {
		return this.vibrationUser;
	}
	
	public int getLastVibrationFrequency() {
		return this.lastVibrationFrequency;
	}
	
	public void setLastVibrationFrequency(int p_222801_) {
		this.lastVibrationFrequency = p_222801_;
	}
	
	public VibrationSystem.Listener getListener() {
		return this.vibrationListener;
	}
	
	protected class VibrationUser
			implements VibrationSystem.User
	{
		public static final int LISTENER_RANGE = 8;
		protected final BlockPos blockPos;
		private final PositionSource positionSource;
		
		public VibrationUser(BlockPos p_283482_)
		{
			this.blockPos = p_283482_;
			this.positionSource = new BlockPositionSource(p_283482_);
		}
		
		@Override
		public int getListenerRadius()
		{
			return 8;
		}
		
		@Override
		public PositionSource getPositionSource()
		{
			return this.positionSource;
		}
		
		@Override
		public boolean canTriggerAvoidVibration()
		{
			return true;
		}
		
		@Override
		public boolean canReceiveVibration(ServerLevel level, BlockPos pos, GameEvent event, @Nullable GameEvent.Context context)
		{
			if(context != null && context.sourceEntity() instanceof ManaBurst) return false;
			if(event == GameEvent.PROJECTILE_SHOOT && (context == null || context.sourceEntity() == null)) return false;
			
			return !isRemoved() && (!pos.equals(getBlockPos()) || event != GameEvent.BLOCK_DESTROY && event != GameEvent.BLOCK_PLACE)
				   && SculkSensorBlock.getPhase(getBlockState()) == SculkSensorPhase.INACTIVE;
		}
		
		@Override
		public void onReceiveVibration(ServerLevel p_282851_, BlockPos p_281608_, GameEvent event, @Nullable Entity ent, @Nullable Entity ent2, float p_283130_)
		{
			// Ignore mana-burst events
			if(ent instanceof ManaBurst) return;
			if(event == GameEvent.PROJECTILE_SHOOT && ent == null && ent2 == null) return;
			
			BlockState blockstate = getBlockState();
			if(SculkSensorBlock.canActivate(blockstate))
			{
				lastVibrationFrequency = VibrationSystem.getGameEventFrequency(event);
				activeTicks = 40;
				VibrantiaBlock.activate(ent, level, worldPosition, blockstate);
			}
		}
		
		@Override
		public void onDataChanged()
		{
			setChanged();
		}
		
		@Override
		public boolean requiresAdjacentChunksToBeTicking()
		{
			return true;
		}
	}
}