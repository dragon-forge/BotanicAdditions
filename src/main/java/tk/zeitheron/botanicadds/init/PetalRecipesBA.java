package tk.zeitheron.botanicadds.init;

import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class PetalRecipesBA
{
	public static RecipePetals wither_flower;
	public static RecipePetals rain_flower;
	public static RecipePetals snow_flower;
	public static RecipePetals lightning_flower;
	
	public static void init()
	{
		ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
		ItemNBTHelper.setString(stack, "SkullOwner", "Zeitheron");
		Object[] inputs = new Object[16];
		Arrays.fill(inputs, ModPetalRecipes.lightBlue);
		BotaniaAPI.registerPetalRecipe(stack, inputs);
		
		wither_flower = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType("ba_wither_flower"), ModPetalRecipes.black, ModPetalRecipes.black, ModPetalRecipes.black, ModPetalRecipes.black, ModPetalRecipes.gray, ModPetalRecipes.gray, ModPetalRecipes.gray, ModPetalRecipes.gray, new ItemStack(Items.SKULL, 1, 1), new ItemStack(ModItems.manaResource, 1, 6), new ItemStack(ModItems.rune, 1, 10), new ItemStack(ModItems.rune, 1, 13));
		rain_flower = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType("ba_rain_flower"), ModPetalRecipes.blue, ModPetalRecipes.blue, ModPetalRecipes.blue, ModPetalRecipes.blue, ModPetalRecipes.lightBlue, ModPetalRecipes.lightBlue, ModPetalRecipes.yellow);
		snow_flower = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType("ba_snow_flower"), ModPetalRecipes.lightBlue, ModPetalRecipes.lightBlue, ModPetalRecipes.lightBlue, ModPetalRecipes.lightBlue, ModPetalRecipes.white, ModPetalRecipes.white, ModPetalRecipes.white);
		lightning_flower = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType("ba_lightning_flower"), ModPetalRecipes.lightBlue, ModPetalRecipes.lightBlue, ModPetalRecipes.lightBlue, ModPetalRecipes.lightBlue, ModPetalRecipes.blue, new ItemStack(ItemsBA.RUNE_ENERGY));
	}
}