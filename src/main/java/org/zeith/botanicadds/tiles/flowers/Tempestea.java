package org.zeith.botanicadds.tiles.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.GenerationalFlowerHUD;
import org.zeith.botanicadds.init.FlowersBA;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

@GenerationalFlowerHUD
public class Tempestea
		extends GeneratingFlowerBlockEntity
{
	public int cooldown;
	protected boolean hasOvergrownSoil;
	
	public Tempestea(BlockPos pos, BlockState state)
	{
		super(FlowersBA.TEMPESTEA_TYPE, pos, state);
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
		hasOvergrownSoil = overgrowth;
		
		if(cooldown > 0)
		{
			--cooldown;
			return;
		}
		
		if(level.isThundering() && !level.isClientSide && getMana() < getMaxMana())
		{
			var lightnings = level.getEntitiesOfClass(LightningBolt.class, new AABB(worldPosition).inflate(getRange()));
			
			for(var e : lightnings)
			{
				if(worldPosition.closerToCenterThan(e.position(), getRange()))
				{
					boolean cap = e.getPersistentData().getBoolean("BA_Tempestea");
					if(!cap)
					{
						addMana(overgrowth ? getMaxMana() : getMaxMana() / 2);
						sync();
						
						cooldown = hasOvergrownSoil ? 240 : 1000;
						
						Vec3 p = getBlockState().getShape(level, worldPosition).bounds().getCenter();
						e.teleportTo(p.x, p.y, p.z);
						
						e.getPersistentData().putBoolean("BA_Tempestea", true);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public boolean isOvergrowthAffected()
	{
		return false;
	}
	
	public int getRange()
	{
		return hasOvergrownSoil ? 10 : 3;
	}
	
	@Override
	public int getMaxMana()
	{
		return hasOvergrownSoil ? 40000 : 20000;
	}
	
	@Override
	public int getColor()
	{
		return 0x53DFDF;
	}
	
	@Override
	public @Nullable RadiusDescriptor getRadius()
	{
		return new RadiusDescriptor.Circle(worldPosition, getRange());
	}
}