package org.zeith.botanicadds.api.recipes.botania;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import vazkii.botania.common.crafting.ElvenTradeRecipe;

import java.util.List;
import java.util.stream.Stream;

public class ElvenTradeRecipeBuilder
		extends RecipeBuilder<ElvenTradeRecipeBuilder>
{
	private NonNullList<Ingredient> inputs = NonNullList.create();
	private NonNullList<ItemStack> outputs = NonNullList.create();
	
	public ElvenTradeRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	@Override
	protected ResourceLocation getResultIdentifier()
	{
		return BuiltInRegistries.ITEM.getKey(outputs.get(0).getItem());
	}
	
	public ElvenTradeRecipeBuilder input(Object... inputs)
	{
		this.inputs.addAll(Stream.of(inputs).map(RecipeHelper::fromComponent).toList());
		return this;
	}
	
	public ElvenTradeRecipeBuilder result(ItemStack... outputs)
	{
		this.outputs.addAll(List.of(outputs));
		return this;
	}
	
	public ElvenTradeRecipeBuilder result(ItemLike output)
	{
		this.outputs.add(new ItemStack(output));
		return this;
	}
	
	public ElvenTradeRecipeBuilder result(ItemLike output, int count)
	{
		this.outputs.add(new ItemStack(output, count));
		return this;
	}
	
	@Override
	protected void validate()
	{
		if(inputs.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined inputs!");
		if(outputs.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined outputs!");
	}
	
	@Override
	protected Recipe<?> createRecipe()
	{
		var id = getIdentifier();
		return new ElvenTradeRecipe(id, outputs.toArray(ItemStack[]::new), inputs.toArray(Ingredient[]::new));
	}
}