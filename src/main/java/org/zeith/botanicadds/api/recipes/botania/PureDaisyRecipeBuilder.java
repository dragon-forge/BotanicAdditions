package org.zeith.botanicadds.api.recipes.botania;

import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.botanicadds.util.RecipeRegistrationContext;
import org.zeith.hammerlib.core.adapter.recipe.RecipeGroup;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.*;

import java.util.Collection;
import java.util.List;

public class PureDaisyRecipeBuilder
{
	protected final IRecipeRegistrationEvent<Recipe<?>> event;
	protected ResourceLocation identifier;
	protected String group = "";
	
	private int time = 60 * 20;
	private StateIngredient input;
	private BlockState output;
	private CommandFunction.CacheableFunction function = CommandFunction.CacheableFunction.NONE;
	
	public PureDaisyRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		this.event = event;
	}
	
	public PureDaisyRecipeBuilder id(ResourceLocation identifier)
	{
		this.identifier = identifier;
		return this;
	}
	
	protected ResourceLocation getIdentifier()
	{
		if(this.identifier != null) return this.identifier;
		return this.identifier = event.nextId(output.getBlock().asItem());
	}
	
	public PureDaisyRecipeBuilder group(String group)
	{
		this.group = group;
		return this;
	}
	
	public PureDaisyRecipeBuilder group(RecipeGroup group)
	{
		this.group = group.toString();
		return this;
	}
	
	public PureDaisyRecipeBuilder time(int time)
	{
		this.time = time;
		return this;
	}
	
	public PureDaisyRecipeBuilder input(Block block)
	{
		this.input = new BlockStateIngredient(block);
		return this;
	}
	
	public PureDaisyRecipeBuilder input(Block... block)
	{
		this.input = new BlocksStateIngredient(List.of(block));
		return this;
	}
	
	public PureDaisyRecipeBuilder input(Collection<Block> block)
	{
		this.input = new BlocksStateIngredient(block);
		return this;
	}
	
	public PureDaisyRecipeBuilder input(TagKey<Block> tag)
	{
		this.input = new TagStateIngredient(tag.location());
		return this;
	}
	
	public PureDaisyRecipeBuilder input(StateIngredient custom)
	{
		this.input = custom;
		return this;
	}
	
	public PureDaisyRecipeBuilder result(Block block)
	{
		this.output = block.defaultBlockState();
		return this;
	}
	
	public PureDaisyRecipeBuilder result(BlockState state)
	{
		this.output = state;
		return this;
	}
	
	public PureDaisyRecipeBuilder function(CommandFunction.CacheableFunction function)
	{
		this.function = function;
		return this;
	}
	
	protected void validate()
	{
		if(input == null)
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined input!");
		if(output == null)
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined output!");
	}
	
	public void register(RecipeRegistrationContext ctx)
	{
		validate();
		var id = getIdentifier();
		if(ctx.enableRecipe(id))
			event.register(id, new PureDaisyRecipe(id, input, output, time, function));
	}
}