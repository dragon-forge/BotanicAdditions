package org.zeith.botanicadds.compat.patchouli.client.processor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.botanicadds.init.RecipeTypesBA;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.patchouli.api.*;

import java.util.List;

public class GaiaPlateProcessor
		implements IComponentProcessor
{
	private RecipeGaiaPlate recipe;
	
	@Override
	public void setup(Level level, IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		this.recipe = PatchouliUtils.getRecipe(level, RecipeTypesBA.GAIA_PLATE, id);
	}
	
	@Override
	public IVariable process(Level level, String key)
	{
		if(recipe == null)
			return null;
		
		if(key.equals("output"))
			return IVariable.from(recipe.getResultItem(level.registryAccess()));
		
		if(key.startsWith("input"))
		{
			int index = Integer.parseInt(key.substring(5)) - 1;
			List<Ingredient> list = recipe.getIngredients();
			if(index >= 0 && index < list.size())
				return IVariable.from(list.get(index).getItems());
		}
		
		return null;
	}
}