package com.zeitheron.botanicadds.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.RecipePetals;

public class GaiaPlateRecipes
{
	public static final List<RecipeGaiaPlate> gaiaRecipes = new ArrayList<>();
	
	/**
	 * Registers a Gaia Plate Recipe.
	 * 
	 * @param output
	 *            The ItemStack to craft.
	 * @param inputs
	 *            The objects for crafting. Can be ItemStack,
	 *            MappableStackWrapper or String (case for Ore Dictionary). The
	 *            array can't be larger than 3.
	 * @return The recipe created.
	 */
	public static RecipeGaiaPlate registerPetalRecipe(ItemStack output, int mana, Object... inputs)
	{
		Preconditions.checkArgument(inputs.length > 0 && inputs.length <= 3);
		RecipeGaiaPlate recipe = new RecipeGaiaPlate(output, mana, inputs);
		gaiaRecipes.add(recipe);
		return recipe;
	}
	
	public static class RecipeGaiaPlate extends RecipePetals
	{
		public int mana;
		
		public RecipeGaiaPlate(ItemStack output, int mana, Object... inputs)
		{
			super(output, inputs);
			Validate.inclusiveBetween(1, 3, inputs.length);
			this.mana = mana;
		}
		
		public int getMana()
		{
			return mana;
		}
	}
}