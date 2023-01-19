package org.zeith.botanicadds.tiles.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.GenerationalFlowerHUD;
import org.zeith.botanicadds.init.FlowersBA;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

@GenerationalFlowerHUD
public class Rainute
		extends GeneratingFlowerBlockEntity
{
	public Rainute(BlockPos pos, BlockState state)
	{
		super(FlowersBA.RAINUTE_TYPE, pos, state);
	}
	
	protected boolean hasOvergrownSoil, generatesEnergy;
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
		hasOvergrownSoil = overgrowth;
		
		if(ticksExisted <= 1 || ticksExisted % 10 == 0)
			generatesEnergy = level.canSeeSky(worldPosition)
					&& level.isRaining()
					&& level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, worldPosition).getY() <= worldPosition.getY()
					&& level.getBiome(worldPosition).value().getPrecipitation() == Biome.Precipitation.RAIN;
		
		if(generatesEnergy)
		{
			int delay = getDelayBetweenPassiveGeneration();
			if(delay > 0 && ticksExisted % delay == 0 && !level.isClientSide && getMana() < getMaxMana())
			{
				addMana(getValueForPassiveGeneration());
			}
		}
	}
	
	@Override
	public boolean isOvergrowthAffected()
	{
		return false;
	}
	
	public int getDelayBetweenPassiveGeneration()
	{
		return hasOvergrownSoil ? 1 : 4;
	}
	
	public int getValueForPassiveGeneration()
	{
		return hasOvergrownSoil ? 2 : 1;
	}
	
	@Override
	public int getMaxMana()
	{
		return hasOvergrownSoil ? 400 : 200;
	}
	
	@Override
	public int getColor()
	{
		return 0x059CFF;
	}
	
	@Override
	public @Nullable RadiusDescriptor getRadius()
	{
		return null;
	}
}