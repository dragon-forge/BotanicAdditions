package org.zeith.botanicadds.items;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.util.MaterialType;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.core.adapter.TagAdapter;

import java.util.Arrays;
import java.util.List;

public class ItemMaterial
		extends Item
		implements IRegisterListener
{
	public final List<MaterialType.Material> materials;
	
	public ItemMaterial(MaterialType type, String... metals)
	{
		this(new Properties().tab(BotanicAdditions.TAB), type, metals);
	}
	
	public ItemMaterial(Properties props, MaterialType type, String... metals)
	{
		super(props);
		materials = Arrays.stream(metals).map(type::of).toList();
	}
	
	@Override
	public void onPostRegistered()
	{
		for(var mat : materials) TagAdapter.bind(ItemTags.create(mat.createId()), this);
	}
}