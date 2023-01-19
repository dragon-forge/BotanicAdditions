package org.zeith.botanicadds.util;

import net.minecraft.core.BlockPos;
import org.zeith.botanicadds.api.tile.IElvenGatewayPylonTile;
import org.zeith.hammerlib.util.java.DirectStorage;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class AlfheimPortalHelper
{
	public static boolean consumeMana(AlfheimPortalBlockEntity self, List<BlockPos> pylons, int totalCost, boolean close, DirectStorage<Boolean> closeNow)
	{
		List<Tuple3<ManaPoolBlockEntity, Integer, IElvenGatewayPylonTile>> consumePools = new ArrayList<>();
		int consumed = 0;
		
		var level = self.getLevel();
		
		if(pylons.size() < 2)
		{
			closeNow.set(true);
			return false;
		}
		
		int costPer = Math.max(1, totalCost / pylons.size());
		int expectedConsumption = costPer * pylons.size();
		
		for(BlockPos pos : pylons)
		{
			IElvenGatewayPylonTile pylonBA = IElvenGatewayPylonTile.findPylon(level, pos);
			
			if(pylonBA != null) pylonBA.activate(self.getBlockPos());
			else continue;
			
			var tile = level.getBlockEntity(pos.below());
			if(tile instanceof ManaPoolBlockEntity pool)
			{
				float mul = pylonBA.getManaCostMultiplier();
				int costPerThis = Math.round(costPer * mul);
				
				if(pool.getCurrentMana() < costPerThis)
				{
					closeNow.set(closeNow.get() || close);
					return false;
				} else if(!level.isClientSide)
				{
					consumePools.add(Tuples.immutable(pool, costPerThis, pylonBA));
					consumed += costPer;
				}
			}
		}
		
		if(consumePools.size() < 2)
		{
			closeNow.set(true);
			return false;
		}
		
		if(consumed >= expectedConsumption)
		{
			for(var tup : consumePools)
				tup.a().receiveMana(-tup.b());
			return true;
		}
		
		return false;
	}
}