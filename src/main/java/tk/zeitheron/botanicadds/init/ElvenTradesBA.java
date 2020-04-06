package tk.zeitheron.botanicadds.init;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.lib.LibOreDict;

public class ElvenTradesBA
{
	public static RecipeElvenTrade dreamrock;
	public static RecipeElvenTrade elvenwood_log;
	public static final NonNullList<RecipeElvenTrade> elven_lapis = NonNullList.withSize(2, new RecipeElvenTrade(new ItemStack[0]));
	
	public static void init()
	{
		dreamrock = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(BlocksBA.DREAMROCK), LibOreDict.LIVING_ROCK);
		elvenwood_log = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(BlocksBA.ELVENWOOD_LOG), "logWood");
		elven_lapis.set(0, BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ItemsBA.ELVEN_LAPIS), new ItemStack(ItemsBA.MANA_LAPIS), new ItemStack(ItemsBA.MANA_LAPIS)));
		elven_lapis.set(1, BotaniaAPI.registerElvenTradeRecipe(new ItemStack(BlocksBA.ELVEN_LAPIS_BLOCK), new ItemStack(BlocksBA.MANA_LAPIS_BLOCK), new ItemStack(BlocksBA.MANA_LAPIS_BLOCK)));
	}
}