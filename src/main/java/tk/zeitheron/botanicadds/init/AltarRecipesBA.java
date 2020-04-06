package tk.zeitheron.botanicadds.init;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.lib.LibOreDict;

public class AltarRecipesBA
{
	public static RecipeRuneAltar rune_tp, rune_energy;
	public static RecipeRuneAltar mana_tesseract;
	
	public static void init()
	{
		rune_tp = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ItemsBA.RUNE_TP), 18000, LibOreDict.RUNE[8], "enderpearl", LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND);
		rune_energy = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ItemsBA.RUNE_ENERGY), 18000, LibOreDict.RUNE[1], LibOreDict.RUNE[3], LibOreDict.MANA_DIAMOND, LibOreDict.MANA_DIAMOND, "dustRedstone", "dustRedstone");
		mana_tesseract = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(BlocksBA.MANA_TESSERACT), 50000, new ItemStack(ItemsBA.RUNE_TP), new ItemStack(BlocksBA.DREAMROCK), LibOreDict.TERRA_STEEL, LibOreDict.RED_STRING);
	}
}