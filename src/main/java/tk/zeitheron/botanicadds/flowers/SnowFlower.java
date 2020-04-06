package tk.zeitheron.botanicadds.flowers;

import tk.zeitheron.botanicadds.flowers.base.Flower;
import tk.zeitheron.botanicadds.flowers.base.SubTilePassiveGen;
import tk.zeitheron.botanicadds.init.LexiconBA;
import tk.zeitheron.botanicadds.utils.WorldUtil;

import vazkii.botania.api.lexicon.LexiconEntry;

@Flower("snow_flower")
public class SnowFlower extends SubTilePassiveGen
{
	@Override
	public void generateMana()
	{
		if(WorldUtil.isSnowingAt(getWorld(), getPos()))
		{
			int delay = getDelayBetweenPassiveGeneration();
			if(delay > 0 && ticksExisted % delay == 0 && !supertile.getWorld().isRemote)
			{
				if(shouldSyncPassiveGeneration())
					sync();
				addMana(getValueForPassiveGeneration());
			}
		}
	}
	
	@Override
	public int getDelayBetweenPassiveGeneration()
	{
		return isOnSpecialSoil() ? 1 : 2;
	}
	
	@Override
	public int getValueForPassiveGeneration()
	{
		return isOnSpecialSoil() ? 2 : 1;
	}
	
	@Override
	public int getMaxMana()
	{
		return isOnSpecialSoil() ? 400 : 200;
	}
	
	@Override
	public int getColor()
	{
		return 0x9CFFFF;
	}
	
	@Override
	public LexiconEntry getEntry()
	{
		return LexiconBA.snow_flower;
	}
}