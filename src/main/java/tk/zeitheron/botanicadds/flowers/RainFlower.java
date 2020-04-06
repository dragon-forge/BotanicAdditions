package tk.zeitheron.botanicadds.flowers;

import tk.zeitheron.botanicadds.flowers.base.Flower;
import tk.zeitheron.botanicadds.flowers.base.SubTilePassiveGen;
import tk.zeitheron.botanicadds.init.LexiconBA;

import vazkii.botania.api.lexicon.LexiconEntry;

@Flower("rain_flower")
public class RainFlower extends SubTilePassiveGen
{
	@Override
	public void generateMana()
	{
		if(getWorld().isRainingAt(getPos()))
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
		return 0x059CFF;
	}
	
	@Override
	public LexiconEntry getEntry()
	{
		return LexiconBA.rain_flower;
	}
}