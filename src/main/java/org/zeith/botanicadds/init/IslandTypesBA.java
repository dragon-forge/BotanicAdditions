package org.zeith.botanicadds.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.FloatingFlower;

public interface IslandTypesBA
{
	FloatingFlower.IslandType SCULK = getOrCreate("SCULK");
	FloatingFlower.IslandType OVERGROWTH = getOrCreate("OVERGROWTH");
	
	@ClientSetup
	@OnlyIn(Dist.CLIENT)
	static void setup()
	{
		BotaniaAPIClient.instance()
				.registerIslandTypeModel(SCULK, BotanicAdditions.id("block/islands/sculk"));
		
		BotaniaAPIClient.instance()
				.registerIslandTypeModel(OVERGROWTH, BotanicAdditions.id("block/islands/enchanted"));
	}
	
	static FloatingFlower.IslandType getOrCreate(String name)
	{
		var t = FloatingFlower.IslandType.ofType(name);
		
		// We requested something else, but got grass -- create a new type.
		if(!"GRASS".equals(name) && t == FloatingFlower.IslandType.GRASS) return new FloatingFlower.IslandType(name);
		
		return t;
	}
}