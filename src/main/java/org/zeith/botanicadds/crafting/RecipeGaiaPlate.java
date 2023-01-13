package org.zeith.botanicadds.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.zeith.botanicadds.init.RecipeSerializersBA;
import org.zeith.botanicadds.init.RecipeTypesBA;
import vazkii.botania.api.recipe.TerrestrialAgglomerationRecipe;
import vazkii.botania.common.crafting.RecipeSerializerBase;
import vazkii.botania.common.crafting.recipe.RecipeUtils;

public class RecipeGaiaPlate
		implements TerrestrialAgglomerationRecipe
{
	private final ResourceLocation id;
	private final int mana;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	
	public RecipeGaiaPlate(ResourceLocation id, int mana, NonNullList<Ingredient> inputs, ItemStack output)
	{
		this.id = id;
		this.mana = mana;
		this.inputs = inputs;
		this.output = output;
	}
	
	@Override
	public int getMana()
	{
		return this.mana;
	}
	
	@Override
	public boolean matches(Container inv, @NotNull Level world)
	{
		int nonEmptySlots = 0;
		
		for(int i = 0; i < inv.getContainerSize(); ++i)
		{
			if(!inv.getItem(i).isEmpty())
			{
				if(inv.getItem(i).getCount() > 1)
				{
					return false;
				}
				
				++nonEmptySlots;
			}
		}
		
		IntOpenHashSet usedSlots = new IntOpenHashSet(inv.getContainerSize());
		return RecipeUtils.matches(this.inputs, inv, usedSlots) && usedSlots.size() == nonEmptySlots;
	}
	
	@Override
	public @NotNull ItemStack assemble(@NotNull Container inv)
	{
		return this.output.copy();
	}
	
	@Override
	public @NotNull ItemStack getResultItem()
	{
		return this.output;
	}
	
	@Override
	public @NotNull NonNullList<Ingredient> getIngredients()
	{
		return this.inputs;
	}
	
	@Override
	public @NotNull ResourceLocation getId()
	{
		return this.id;
	}
	
	@Override
	public @NotNull RecipeSerializer<RecipeGaiaPlate> getSerializer()
	{
		return RecipeSerializersBA.GAIA_PLATE;
	}
	
	@Override
	public RecipeType<?> getType()
	{
		return RecipeTypesBA.GAIA_PLATE;
	}
	
	public static class Serializer
			extends RecipeSerializerBase<RecipeGaiaPlate>
	{
		public Serializer()
		{
		}
		
		@Override
		public @NotNull RecipeGaiaPlate fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json)
		{
			int mana = GsonHelper.getAsInt(json, "mana");
			JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
			Ingredient[] ingredients = new Ingredient[ingrs.size()];
			for(int i = 0; i < ingrs.size(); ++i) ingredients[i] = Ingredient.fromJson(ingrs.get(i));
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			return new RecipeGaiaPlate(recipeId, mana, NonNullList.of(Ingredient.EMPTY, ingredients), output);
		}
		
		@Override
		public RecipeGaiaPlate fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			int mana = buffer.readVarInt();
			Ingredient[] ingredients = new Ingredient[buffer.readVarInt()];
			for(int i = 0; i < ingredients.length; ++i) ingredients[i] = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();
			return new RecipeGaiaPlate(recipeId, mana, NonNullList.of(Ingredient.EMPTY, ingredients), output);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, RecipeGaiaPlate recipe)
		{
			buffer.writeVarInt(recipe.mana);
			buffer.writeVarInt(recipe.getIngredients().size());
			for(var ingr : recipe.getIngredients()) ingr.toNetwork(buffer);
			buffer.writeItem(recipe.output);
		}
	}
}
