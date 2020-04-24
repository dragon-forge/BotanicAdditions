package tk.zeitheron.botanicadds.flowers;

import tk.zeitheron.botanicadds.flowers.base.Flower;
import tk.zeitheron.botanicadds.flowers.base.SubTilePassiveGen;
import tk.zeitheron.botanicadds.init.LexiconBA;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.Vec3d;
import vazkii.botania.api.lexicon.LexiconEntry;

@Flower("lightning_flower")
public class LightningFlower extends SubTilePassiveGen
{
	public int cooldown;
	
	@Override
	public void generateMana()
	{
		if(cooldown > 0)
			--cooldown;
		if(getWorld().isRainingAt(getPos()) && getWorld().isThundering() && !supertile.getWorld().isRemote && cooldown <= 0 && mana < getMaxMana())
		{
			for(int i = 0; i < getWorld().weatherEffects.size(); ++i)
			{
				Entity e = getWorld().weatherEffects.get(i);
				
				if(e instanceof EntityLightningBolt && e.getDistance(getPos().getX(), getPos().getY(), getPos().getZ()) < 128)
				{
					boolean cap = e.getEntityData().getBoolean("BA_LightningFlower");
					if(!cap)
					{
						Vec3d p = getCenter(getWorld().getBlockState(getPos()).getBoundingBox(getWorld(), getPos())).add(getPos().getX(), getPos().getY(), getPos().getZ());
						
						if(shouldSyncPassiveGeneration())
							sync();
						addMana(isOnSpecialSoil() ? 4000 : 2000);
						
						cooldown = 1000;
						
						e.setDead();
						
						EntityLightningBolt ne = new EntityLightningBolt(getWorld(), p.x, p.y, p.z, false);
						getWorld().addWeatherEffect(ne);
						ne.getEntityData().setBoolean("BA_LightningFlower", true);
						
						break;
					}
				}
			}
			
			if(Math.random() <= .0001)
			{
				Vec3d p = getCenter(getWorld().getBlockState(getPos()).getBoundingBox(getWorld(), getPos())).add(getPos().getX(), getPos().getY(), getPos().getZ());
				getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), p.x, p.y, p.z, true));
			}
		}
	}
	
	@Override
	public int getDelayBetweenPassiveGeneration()
	{
		return 1;
	}
	
	@Override
	public int getValueForPassiveGeneration()
	{
		return 0;
	}
	
	@Override
	public int getMaxMana()
	{
		return isOnSpecialSoil() ? 4000 : 2000;
	}
	
	@Override
	public int getColor()
	{
		return 0x53DFDF;
	}
	
	@Override
	public LexiconEntry getEntry()
	{
		return LexiconBA.lightning_flower;
	}
}