package org.zeith.botanicadds.api.recipes.botania;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.*;

import java.util.*;

public class ManaPoolRecipeBuilder
		extends RecipeBuilder<ManaPoolRecipeBuilder>
{
	private int mana;
	private Ingredient ingredient = Ingredient.EMPTY;
	private StateIngredient catalyst;
	
	public ManaPoolRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public ManaPoolRecipeBuilder mana(int mana)
	{
		this.mana = mana;
		return this;
	}
	
	public ManaPoolRecipeBuilder input(Object ingredient)
	{
		this.ingredient = RecipeHelper.fromComponent(ingredient);
		return this;
	}
	
	public ManaPoolRecipeBuilder catalyst(Block block)
	{
		this.catalyst = new BlockStateIngredient(block);
		return this;
	}
	
	public ManaPoolRecipeBuilder catalyst(Block... block)
	{
		this.catalyst = new BlocksStateIngredient(List.of(block));
		return this;
	}
	
	public ManaPoolRecipeBuilder catalyst(Collection<Block> block)
	{
		this.catalyst = new BlocksStateIngredient(block);
		return this;
	}
	
	public ManaPoolRecipeBuilder catalyst(TagKey<Block> tag)
	{
		this.catalyst = new TagStateIngredient(tag.location());
		return this;
	}
	
	public ManaPoolRecipeBuilder catalyst(StateIngredient custom)
	{
		this.catalyst = custom;
		return this;
	}
	
	@Override
	protected void validate()
	{
		if(ingredient.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined ingredient!");
		super.validate();
	}
	
	@Override
	protected Recipe<?> createRecipe()
	{
		return new ManaInfusionRecipe(getIdentifier(), result, ingredient, mana, group, catalyst);
	}
}