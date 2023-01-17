package org.zeith.botanicadds.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.core.adapter.TagAdapter;

public interface TagsBA
{
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