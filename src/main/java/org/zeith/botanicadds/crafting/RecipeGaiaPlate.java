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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.zeith.botanicadds.init.BlocksBA;
import org.zeith.botanicadds.init.RecipeTypesBA;
import org.zeith.hammerlib.api.recipes.BaseRecipe;
import org.zeith.hammerlib.api.recipes.SerializableRecipeType;
import vazkii.botania.api.recipe.TerrestrialAgglomerationRecipe;
import vazkii.botania.common.crafting.recipe.RecipeUtils;

public class RecipeGaiaPlate
		extends BaseRecipe<RecipeGaiaPlate>
		implements TerrestrialAgglomerationRecipe
{
	private final int mana;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	
	public RecipeGaiaPlate(ResourceLocation id, String group, int mana, NonNullList<Ingredient> inputs, ItemStack output)
	{
		super(id, group);
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
	protected SerializableRecipeType<RecipeGaiaPlate> getRecipeType()
	{
		return RecipeTypesBA.GAIA_PLATE;
	}
	
	@Override
	public ItemStack getToastSymbol()
	{
		return BlocksBA.GAIA_PLATE.asItem().getDefaultInstance();
	}
	
	public static class GaiaPlateRecipeType
			extends SerializableRecipeType<RecipeGaiaPlate>
	{
		public GaiaPlateRecipeType()
		{
		}
		
		@Override
		public @NotNull RecipeGaiaPlate fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json)
		{
			var group = GsonHelper.getAsString(json, DEFAULT_GROUP_KEY, "");
			int mana = GsonHelper.getAsInt(json, "mana");
			JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
			Ingredient[] ingredients = new Ingredient[ingrs.size()];
			for(int i = 0; i < ingrs.size(); ++i) ingredients[i] = Ingredient.fromJson(ingrs.get(i));
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, DEFAULT_OUTPUT_KEY));
			return new RecipeGaiaPlate(recipeId, group, mana, NonNullList.of(Ingredient.EMPTY, ingredients), output);
		}
		
		@Override
		public RecipeGaiaPlate fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			int mana = buffer.readVarInt();
			var ingredients = new Ingredient[buffer.readVarInt()];
			for(int i = 0; i < ingredients.length; ++i) ingredients[i] = Ingredient.fromNetwork(buffer);
			var output = buffer.readItem();
			var group = buffer.readUtf();
			return new RecipeGaiaPlate(recipeId, group, mana, NonNullList.of(Ingredient.EMPTY, ingredients), output);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, RecipeGaiaPlate recipe)
		{
			buffer.writeVarInt(recipe.mana);
			buffer.writeVarInt(recipe.getIngredients().size());
			for(var ingr : recipe.getIngredients()) ingr.toNetwork(buffer);
			buffer.writeItem(recipe.output);
			buffer.writeUtf(recipe.group);
		}
	}
}
