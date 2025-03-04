package org.zeith.botanicadds.api.recipes.botania;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.*;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import vazkii.botania.common.crafting.PetalsRecipe;
import vazkii.botania.common.lib.BotaniaTags;

public class PetalApothecaryRecipeBuilder
		extends RecipeBuilder<PetalApothecaryRecipeBuilder>
{
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private Ingredient reagent = Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);
	
	public PetalApothecaryRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public PetalApothecaryRecipeBuilder reagent(Object reagent)
	{
		this.reagent = RecipeHelper.fromComponent(reagent);
		return this;
	}
	
	public PetalApothecaryRecipeBuilder add(Object ingredient)
	{
		this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}
	
	public PetalApothecaryRecipeBuilder addAll(Object... ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}
	
	public PetalApothecaryRecipeBuilder addAll(Iterable<Object> ingredients)
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
	protected Recipe<?> createRecipe()
	{
		return new PetalsRecipe(getIdentifier(), result, reagent, ingredients.toArray(Ingredient[]::new));
	}
}