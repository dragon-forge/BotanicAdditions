package org.zeith.botanicadds.util;

import net.minecraft.world.level.block.entity.BlockEntity;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkHelper;

public class SparkUtil
{
	public static void startRequestingMana(BlockEntity be, ManaSpark spark)
	{
		if(spark != null)
		{
			var level = be.getLevel();
			var worldPosition = be.getBlockPos();
			var otherSparks = SparkHelper.getSparksAround(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, spark.getNetwork());
			for(var otherSpark : otherSparks)
				if(spark != otherSpark && otherSpark.getAttachedManaReceiver() instanceof ManaPool)
					otherSpark.registerTransfer(spark);
		}
	}
}