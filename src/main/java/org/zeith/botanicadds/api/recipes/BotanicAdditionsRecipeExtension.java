package org.zeith.botanicadds.api.recipes;

import org.zeith.botanicadds.api.recipes.botania.*;
import org.zeith.botanicadds.api.recipes.botanicadds.GaiaPlateRecipeBuilder;
import org.zeith.hammerlib.api.recipes.RecipeBuilderExtension;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@RecipeBuilderExtension.RegisterExt
public class BotanicAdditionsRecipeExtension
		extends RecipeBuilderExtension
{
	public BotanicAdditionsRecipeExtension(RegisterRecipesEvent event)
	{
		super(event);
	}
	
	public GaiaPlateRecipeBuilder gaiaPlate()
	{
		return new GaiaPlateRecipeBuilder(event);
	}
	
	public PetalApothecaryRecipeBuilder petalApothecary()
	{
		return new PetalApothecaryRecipeBuilder(event);
	}
	
	public RunicAltarRecipeBuilder runicAltar()
	{
		return new RunicAltarRecipeBuilder(event);
	}
	
	public ManaPoolRecipeBuilder manaPool()
	{
		return new ManaPoolRecipeBuilder(event);
	}
	
	public PureDaisyRecipeBuilder pureDaisy()
	{
		return new PureDaisyRecipeBuilder(event);
	}
	
	public ElvenTradeRecipeBuilder elvenTrade()
	{
		return new ElvenTradeRecipeBuilder(event);
	}
}