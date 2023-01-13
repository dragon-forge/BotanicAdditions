package org.zeith.botanicadds.blocks.flowers;

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
public class LightningFlower
		extends GeneratingFlowerBlockEntity
{
	public int cooldown;
	
	public LightningFlower(BlockPos pos, BlockState state)
	{
		super(FlowersBA.LIGHTNING_FLOWER_TYPE, pos, state);
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
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
					boolean cap = e.getPersistentData().getBoolean("BA_LightningFlower");
					if(!cap)
					{
						addMana(overgrowth ? getMaxMana() : getMaxMana() / 2);
						sync();
						
						cooldown = 1000;
						
						Vec3 p = getBlockState().getShape(level, worldPosition).bounds().getCenter();
						e.teleportTo(p.x, p.y, p.z);
						
						e.getPersistentData().putBoolean("BA_LightningFlower", true);
						break;
					}
				}
			}
		}
	}
	
	public int getRange()
	{
		return overgrowth ? 10 : 3;
	}
	
	@Override
	public int getMaxMana()
	{
		return 20000;
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