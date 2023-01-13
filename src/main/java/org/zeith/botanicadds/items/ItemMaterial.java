package org.zeith.botanicadds.items;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.util.MaterialType;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.core.adapter.TagAdapter;

public class ItemMaterial
		extends Item
		implements IRegisterListener
{
	public final MaterialType.Material material;
	public final TagKey<Item> materialTag;
	
	public ItemMaterial(MaterialType type, String metal)
	{
		this(new Properties().tab(BotanicAdditions.TAB), type, metal);
	}
	
	public ItemMaterial(Properties props, MaterialType type, String metal)
	{
		super(props);
		material = type.of(metal);
		materialTag = ItemTags.create(material.createId());
	}
	
	public TagKey<Item> getTag()
	{
		return materialTag;
	}
	
	@Override
	public void onPostRegistered()
	{
		TagAdapter.bind(getTag(), this);
	}
}