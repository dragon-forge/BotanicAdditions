package org.zeith.botanicadds.api.recipes.botanicadds;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public class GaiaPlateRecipeBuilder
		extends RecipeBuilder<GaiaPlateRecipeBuilder, Recipe<?>>
{
	private int mana;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	
	public GaiaPlateRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public GaiaPlateRecipeBuilder mana(int mana)
	{
		this.mana = mana;
		return this;
	}
	
	public GaiaPlateRecipeBuilder add(Object ingredient)
	{
		this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}
	
	public GaiaPlateRecipeBuilder addAll(Object... ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}
	
	public GaiaPlateRecipeBuilder addAll(Iterable<Object> ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}
	
	@Override
	protected void validate()
	{
		if(ingredients.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined ingredients!");
		super.validate();
	}
	
	@Override
	public void register()
	{
		validate();
		var id = getIdentifier();
		event.register(id, new RecipeGaiaPlate(id, group, mana, ingredients, result));
	}
}