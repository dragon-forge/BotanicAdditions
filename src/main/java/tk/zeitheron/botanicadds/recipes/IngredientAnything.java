package tk.zeitheron.botanicadds.recipes;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class IngredientAnything extends Ingredient
{
	@Override
	public boolean apply(ItemStack p_apply_1_)
	{
		return true;
	}
	
	ItemStack[] items;
	
	@Override
	public ItemStack[] getMatchingStacks()
	{
		if(items == null)
			items = index().toArray(new ItemStack[0]);
		return items;
	}
	
	public static NonNullList<ItemStack> index()
	{
		NonNullList<ItemStack> items = NonNullList.create();
		
		ForgeRegistries.ITEMS.forEach(item ->
		{
			NonNullList<ItemStack> sb = NonNullList.create();
			try
			{
				item.getSubItems(CreativeTabs.SEARCH, sb);
			} catch(Throwable err)
			{
			}
			items.addAll(sb);
		});
		
		return items;
	}
}