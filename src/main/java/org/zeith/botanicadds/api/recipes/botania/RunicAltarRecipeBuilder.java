package org.zeith.botanicadds.api.recipes.botania;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import vazkii.botania.common.crafting.RunicAltarRecipe;

public class RunicAltarRecipeBuilder
		extends RecipeBuilder<RunicAltarRecipeBuilder, Recipe<?>>
{
	private int mana;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	
	public RunicAltarRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public RunicAltarRecipeBuilder mana(int mana)
	{
		this.mana = mana;
		return this;
	}
	
	public RunicAltarRecipeBuilder add(Object ingredient)
	{
		this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}
	
	public RunicAltarRecipeBuilder addAll(Object... ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}
	
	public RunicAltarRecipeBuilder addAll(Iterable<Object> ingredients)
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
		event.register(id, new RunicAltarRecipe(id, result, mana, ingredients.toArray(Ingredient[]::new)));
	}
}