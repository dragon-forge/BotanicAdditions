package org.zeith.botanicadds.api.tile;

public interface ICustomCapacityManaPool
{
	int getMaxCustomMana();
	
	default int getPoolInset()
	{
		return 2;
	}
	
	default float getPoolBottom()
	{
		return getPoolInset() / 16F + 0.001F;
	}
	
	float getPoolTop();
}