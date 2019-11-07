package com.zeitheron.botanicadds.init;

import com.zeitheron.botanicadds.api.GaiaPlateRecipes;
import com.zeitheron.botanicadds.api.GaiaPlateRecipes.RecipeGaiaPlate;

import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class GaiaPlateRecipesBA
{
	public static RecipeGaiaPlate TERRA_STEEL;
	public static RecipeGaiaPlate GAIA_STEEL;
	
	public static void init()
	{
		TERRA_STEEL = GaiaPlateRecipes.registerPetalRecipe(new ItemStack(ModItems.manaResource, 1, 4), 300_000, LibOreDict.MANA_DIAMOND, LibOreDict.MANA_PEARL, LibOreDict.MANA_STEEL);
		GAIA_STEEL = GaiaPlateRecipes.registerPetalRecipe(new ItemStack(ItemsBA.GAIASTEEL_INGOT), 1_000_000, LibOreDict.DRAGONSTONE, LibOreDict.PIXIE_DUST, LibOreDict.GAIA_INGOT);
	}
}