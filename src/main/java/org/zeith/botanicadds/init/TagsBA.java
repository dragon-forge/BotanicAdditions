package org.zeith.botanicadds.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.common.block.BotaniaBlocks;

public interface TagsBA
{
	interface Blocks
	{
		TagKey<Block> ALFHEIM_GATEWAY_PYLONS = BlockTags.create(BotanicAdditions.id("alfheim_gateway_pylons"));
		
		@Setup
		static void setup()
		{
			TagAdapter.bind(ALFHEIM_GATEWAY_PYLONS, BotaniaBlocks.naturaPylon);
		}
	}
	
	interface EntityTypes
	{
		TagKey<EntityType<?>> BEES = tag("bees");
		
		private static TagKey<EntityType<?>> tag(String name)
		{
			return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", name));
		}
		
		@Setup
		static void setup()
		{
			TagAdapter.bind(BEES, EntityType.BEE);
		}
	}
}