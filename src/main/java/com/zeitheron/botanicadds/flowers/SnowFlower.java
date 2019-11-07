package com.zeitheron.botanicadds.flowers;

import com.zeitheron.botanicadds.flowers.base.Flower;
import com.zeitheron.botanicadds.flowers.base.SubTilePassiveGen;
import com.zeitheron.botanicadds.init.LexiconBA;
import com.zeitheron.botanicadds.net.PacketFXLine;
import com.zeitheron.botanicadds.utils.WorldUtil;
import com.zeitheron.hammercore.net.HCNet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.ModBlocks;

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