package org.zeith.botanicadds.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.compat.jei.cats.GaiaPlateRecipeCategory;
import org.zeith.botanicadds.init.RecipeTypesBA;

import java.util.*;

@JeiPlugin
public class JeiBA
		implements IModPlugin
{
	public static final ResourceLocation ID = BotanicAdditions.id("jei");
	private static final Comparator<Recipe<?>> BY_ID = Comparator.comparing(Recipe::getId);
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return ID;
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		registration.addRecipeCategories(new GaiaPlateRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		registration.addRecipes(GaiaPlateRecipeCategory.TYPE, sortRecipes(RecipeTypesBA.GAIA_PLATE, BY_ID));
	}
	
	private static <T extends Recipe<C>, C extends Container> List<T> sortRecipes(RecipeType<T> type, Comparator<? super T> comparator)
	{
		var lvl = Minecraft.getInstance().level;
		Collection<T> recipes = lvl != null ? lvl.getRecipeManager().byType(type).values() : Collections.emptyList();
		List<T> list = new ArrayList<>(recipes);
		list.sort(comparator);
		return list;
	}
}