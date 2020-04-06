package tk.zeitheron.botanicadds.compat.crafttweaker.core;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class InputHelper
{
	public static Ingredient toIngredient(IIngredient iIng)
	{
		if(iIng == null)
			return Ingredient.EMPTY;
		
		Object internal = iIng.getInternal();
		
		if(iIng instanceof IItemStack)
			return Ingredient.fromStacks(toStack((IItemStack) iIng));
		
		if(!(internal instanceof Ingredient))
		{
			return Ingredient.EMPTY;
		}
		
		return (Ingredient) internal;
	}
	
	public static ItemStack toStack(IItemStack iStack)
	{
		if(iStack == null)
			return ItemStack.EMPTY;
		
		Object internal = iStack.getInternal();
		
		if(!(internal instanceof ItemStack))
		{
			return ItemStack.EMPTY;
		}
		
		return (ItemStack) internal;
	}
}