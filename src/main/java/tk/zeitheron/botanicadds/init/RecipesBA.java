package tk.zeitheron.botanicadds.init;

import com.zeitheron.hammercore.internal.SimpleRegistration;
import com.zeitheron.hammercore.utils.recipes.helper.RecipeRegistry;
import com.zeitheron.hammercore.utils.recipes.helper.RegisterRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import tk.zeitheron.botanicadds.InfoBA;
import tk.zeitheron.botanicadds.recipes.RecipeLinkTesseract;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

@RegisterRecipes(modid = InfoBA.MOD_ID)
public class RecipesBA
		extends RecipeRegistry
{
	public static final ResourceLocation terra_catalyst = new ResourceLocation(InfoBA.MOD_ID, "terra_catalyst");
	public static final ResourceLocation mana_lapis_block = new ResourceLocation(InfoBA.MOD_ID, "mana_lapis_block"), mana_lapis = new ResourceLocation(InfoBA.MOD_ID, "mana_lapis");
	public static final ResourceLocation elven_lapis_block = new ResourceLocation(InfoBA.MOD_ID, "elven_lapis_block"), elven_lapis = new ResourceLocation(InfoBA.MOD_ID, "elven_lapis");
	public static final ResourceLocation elven_altar = new ResourceLocation(InfoBA.MOD_ID, "elven_altar");
	public static final ResourceLocation dreaming_pool = new ResourceLocation(InfoBA.MOD_ID, "dreaming_pool");
	public static final ResourceLocation gaia_plate = new ResourceLocation(InfoBA.MOD_ID, "gaia_plate");
	public static final ResourceLocation terra_protector = new ResourceLocation(InfoBA.MOD_ID, "terra_protector");
	public static final ResourceLocation ring_aura_gaia = new ResourceLocation(InfoBA.MOD_ID, "ring_aura_gaia");
	public static final ResourceLocation mana_stealer_sword = new ResourceLocation(InfoBA.MOD_ID, "mana_stealer_sword");
	public static final ResourceLocation gaiasteel_block = new ResourceLocation(InfoBA.MOD_ID, "gaiasteel_block"), gaiasteel_ingot = new ResourceLocation(InfoBA.MOD_ID, "gaiasteel_ingot");

	public static RecipePureDaisy dreamwood;

	@Override
	public void crafting()
	{
		ModContainer prev = Loader.instance().activeModContainer();
		Loader.instance().setActiveModContainer(getOwner());

		recipe(new RecipeLinkTesseract().setRegistryName("mana_tesseract_bind"));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(BlocksBA.TERRA_CATALYST), "sgs", "tct", "sts", 's', new ItemStack(ModBlocks.shimmerrock), 't', LibOreDict.TERRA_STEEL, 'g', LibOreDict.LIFE_ESSENCE, 'c', new ItemStack(ModBlocks.alchemyCatalyst)).setRegistryName(terra_catalyst));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(BlocksBA.MANA_LAPIS_BLOCK), "lll", "lll", "lll", 'l', new ItemStack(ItemsBA.MANA_LAPIS)).setRegistryName(mana_lapis_block));
		recipe(SimpleRegistration.parseShapelessRecipe(new ItemStack(ItemsBA.MANA_LAPIS, 9), new ItemStack(BlocksBA.MANA_LAPIS_BLOCK)).setRegistryName(mana_lapis));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(BlocksBA.ELVEN_LAPIS_BLOCK), "lll", "lll", "lll", 'l', new ItemStack(ItemsBA.ELVEN_LAPIS)).setRegistryName(elven_lapis_block));
		recipe(SimpleRegistration.parseShapelessRecipe(new ItemStack(ItemsBA.ELVEN_LAPIS, 9), new ItemStack(BlocksBA.ELVEN_LAPIS_BLOCK)).setRegistryName(elven_lapis));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(BlocksBA.ELVEN_ALTAR), "ddd", "dsd", "dad", 'd', new ItemStack(BlocksBA.DREAMROCK), 's', LibOreDict.DRAGONSTONE, 'a', new ItemStack(ModBlocks.runeAltar)).setRegistryName(elven_altar));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(BlocksBA.DREAMING_POOL), "ddd", "dpd", "ttt", 'd', new ItemStack(BlocksBA.DREAMROCK), 'p', new ItemStack(ModBlocks.pool), 't', LibOreDict.TERRASTEEL_NUGGET).setRegistryName(dreaming_pool));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(BlocksBA.GAIA_PLATE), "lll", "etp", "ggg", 'l', new ItemStack(BlocksBA.ELVEN_LAPIS_BLOCK), 'e', new ItemStack(ItemsBA.RUNE_ENERGY), 't', new ItemStack(ModBlocks.terraPlate), 'p', new ItemStack(ItemsBA.RUNE_TP), 'g', new ItemStack(ItemsBA.GAIA_SHARD)).setRegistryName(gaia_plate));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(ItemsBA.TERRA_PROTECTOR), "ggg", "tst", " t ", 'g', new ItemStack(ItemsBA.GAIASTEEL_INGOT), 't', LibOreDict.TERRASTEEL_NUGGET, 's', LibOreDict.RUNE[4]).setRegistryName(terra_protector));
		recipe(SimpleRegistration.parseShapelessRecipe(new ItemStack(ItemsBA.RING_AURA_GAIA), new ItemStack(ModItems.auraRingGreater), new ItemStack(ItemsBA.GAIASTEEL_INGOT)).setRegistryName(ring_aura_gaia));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(ItemsBA.MANA_STEALER_SWORD), "g", "g", "s", 'g', new ItemStack(ItemsBA.GAIASTEEL_INGOT), 's', new ItemStack(ModItems.terraSword)).setRegistryName(mana_stealer_sword));
		recipe(SimpleRegistration.parseShapedRecipe(new ItemStack(BlocksBA.GAIASTEEL_BLOCK), "iii", "iii", "iii", 'i', "ingotGaiasteel").setRegistryName(gaiasteel_block));
		recipe(SimpleRegistration.parseShapelessRecipe(new ItemStack(ItemsBA.GAIASTEEL_INGOT, 9), "blockGaiasteel").setRegistryName(gaiasteel_ingot));

		Loader.instance().setActiveModContainer(prev);
	}

	public static void init()
	{
		dreamwood = BotaniaAPI.registerPureDaisyRecipe("logElvenwood", ModBlocks.dreamwood.getDefaultState());
	}
}