package org.zeith.botanicadds.api.recipes.botania;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.zeith.botanicadds.util.RecipeRegistrationContext;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import vazkii.botania.common.crafting.ElvenTradeRecipe;

import java.util.List;
import java.util.stream.Stream;

public class ElvenTradeRecipeBuilder
{
	protected final IRecipeRegistrationEvent<Recipe<?>> event;
	protected ResourceLocation identifier;
	
	private NonNullList<Ingredient> inputs = NonNullList.create();
	private NonNullList<ItemStack> outputs = NonNullList.create();
	
	public ElvenTradeRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		this.event = event;
	}
	
	public ElvenTradeRecipeBuilder id(ResourceLocation identifier)
	{
		this.identifier = identifier;
		return this;
	}
	
	protected ResourceLocation getIdentifier()
	{
		if(this.identifier != null) return this.identifier;
		return this.identifier = event.nextId(outputs.get(0).getItem());
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
	
	protected void validate()
	{
		if(inputs.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined inputs!");
		if(outputs.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined outputs!");
	}
	
	public void register(RecipeRegistrationContext ctx)
	{
		validate();
		var id = getIdentifier();
		if(ctx.enableRecipe(id))
			event.register(id, new ElvenTradeRecipe(id, outputs.toArray(ItemStack[]::new), inputs.toArray(Ingredient[]::new)));
	}
}