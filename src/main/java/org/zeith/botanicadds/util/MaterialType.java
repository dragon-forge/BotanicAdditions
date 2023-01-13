package org.zeith.botanicadds.util;

import net.minecraft.resources.ResourceLocation;

public enum MaterialType
{
	STORAGE_BLOCK("storage_blocks"),
	INGOT("ingots"),
	NUGGET("nuggets"),
	GEM("gems");
	
	final String kind;
	
	MaterialType(String kind)
	{
		this.kind = kind;
	}
	
	public ResourceLocation createId(String type)
	{
		return new ResourceLocation("forge", kind + "/" + type);
	}
	
	public Material of(String material)
	{
		return new Material(this, material);
	}
	
	public record Material(MaterialType type, String material)
	{
		public ResourceLocation createId()
		{
			return type.createId(material);
		}
	}
}