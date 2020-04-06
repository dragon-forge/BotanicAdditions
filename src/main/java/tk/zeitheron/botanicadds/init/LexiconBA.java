package tk.zeitheron.botanicadds.init;

import tk.zeitheron.botanicadds.blocks.tiles.TileGaiaPlate;
import tk.zeitheron.botanicadds.lexicon.PageGaiaPlate;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.BLexiconCategory;
import vazkii.botania.common.lexicon.BasicLexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class LexiconBA
{
	public static LexiconCategory categoryBotanicAdditions;
	
	public static LexiconEntry wither_flower;
	public static LexiconEntry rain_flower;
	public static LexiconEntry snow_flower;
	public static LexiconEntry lightning_flower;
	public static LexiconEntry mana_tesseract;
	public static LexiconEntry terra_catalyst;
	public static LexiconEntry elven_altar;
	public static LexiconEntry dreaming_pool;
	public static LexiconEntry gaia_plate;
	public static LexiconEntry terra_protector;
	public static LexiconEntry ring_aura_gaia;
	public static LexiconEntry mana_stealer_sword;
	
	public static void init()
	{
		BotaniaAPI.addCategory(categoryBotanicAdditions = new BLexiconCategory("botanicadds", 6));
		
		// Entries
		
		// Additional
		LexiconData.runicAltar.setLexiconPages(new PageRuneRecipe(".ba_tp_rune", AltarRecipesBA.rune_tp), new PageRuneRecipe(".ba_energy_rune", AltarRecipesBA.rune_energy));
		LexiconData.pool.setLexiconPages(new PageManaInfusionRecipe(".ba_mana_lapis", PoolRecipesBA.mana_lapis));
		LexiconData.elvenResources.setLexiconPages(new PageElvenRecipe(".ba_elven_lapis", ElvenTradesBA.elven_lapis), new PageElvenRecipe(".ba_elven_log", ElvenTradesBA.elvenwood_log), new PageElvenRecipe(".ba_dreamrock", ElvenTradesBA.dreamrock));
		
		// Wither Flower
		wither_flower = new BasicLexiconEntry("ba_wither_flower", categoryBotanicAdditions).setPriority().setLexiconPages(new PageText("1"), new PagePetalRecipe("2", PetalRecipesBA.wither_flower));
		wither_flower.setIcon(ItemBlockSpecialFlower.ofType("ba_wither_flower"));
		// Rain Flower
		rain_flower = new BasicLexiconEntry("ba_rain_flower", categoryBotanicAdditions).setPriority().setLexiconPages(new PageText("1"), new PagePetalRecipe("2", PetalRecipesBA.rain_flower));
		rain_flower.setIcon(ItemBlockSpecialFlower.ofType("ba_rain_flower"));
		// Snow Flower
		snow_flower = new BasicLexiconEntry("ba_snow_flower", categoryBotanicAdditions).setPriority().setLexiconPages(new PageText("1"), new PagePetalRecipe("2", PetalRecipesBA.snow_flower));
		snow_flower.setIcon(ItemBlockSpecialFlower.ofType("ba_snow_flower"));
		// Lightning Flower
		lightning_flower = new BasicLexiconEntry("ba_lightning_flower", categoryBotanicAdditions).setPriority().setLexiconPages(new PageText("1"), new PagePetalRecipe("2", PetalRecipesBA.lightning_flower));
		lightning_flower.setIcon(ItemBlockSpecialFlower.ofType("ba_lightning_flower"));
		// Mana Tesseract
		mana_tesseract = new BasicLexiconEntry("ba_mana_tesseract", categoryBotanicAdditions).setPriority().setLexiconPages(new PageText("1"), new PageRuneRecipe("2", AltarRecipesBA.mana_tesseract));
		mana_tesseract.setIcon(new ItemStack(BlocksBA.MANA_TESSERACT));
		// Terra Catalyst
		terra_catalyst = new BasicLexiconEntry("ba_terra_catalyst", categoryBotanicAdditions).setPriority().setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("1"), new PageCraftingRecipe("2", RecipesBA.terra_catalyst), new PageManaInfusionRecipe("3", PoolRecipesBA.gaia_shard));
		terra_catalyst.setIcon(new ItemStack(BlocksBA.TERRA_CATALYST));
		// Elven Altar
		elven_altar = new BasicLexiconEntry("ba_elven_altar", categoryBotanicAdditions).setPriority().setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("1"), new PageCraftingRecipe("2", RecipesBA.elven_altar));
		elven_altar.setIcon(new ItemStack(BlocksBA.ELVEN_ALTAR));
		// Dreaming Mana Pool
		dreaming_pool = new BasicLexiconEntry("ba_dreaming_pool", categoryBotanicAdditions).setPriority().setLexiconPages(new PageText("1"), new PageCraftingRecipe("2", RecipesBA.dreaming_pool));
		dreaming_pool.setIcon(new ItemStack(BlocksBA.DREAMING_POOL));
		// Gaia Agglomeration Plate
		gaia_plate = new BasicLexiconEntry("ba_gaia_plate", categoryBotanicAdditions).setPriority().setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("1"), new PageCraftingRecipe("2", RecipesBA.gaia_plate), new PageMultiblock("3", TileGaiaPlate.makeMultiblockSet()), new PageGaiaPlate("4", GaiaPlateRecipesBA.TERRA_STEEL), new PageGaiaPlate("5", GaiaPlateRecipesBA.GAIA_STEEL));
		gaia_plate.setIcon(new ItemStack(BlocksBA.GAIA_PLATE));
		// Terra Protector
		terra_protector = new BasicLexiconEntry("ba_terra_protector", categoryBotanicAdditions).setPriority().setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("1"), new PageCraftingRecipe("2", RecipesBA.terra_protector));
		terra_protector.setIcon(new ItemStack(ItemsBA.TERRA_PROTECTOR));
		// Gaia Band of Aura
		ring_aura_gaia = new BasicLexiconEntry("ba_ring_aura_gaia", categoryBotanicAdditions).setPriority().setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("1"), new PageCraftingRecipe("2", RecipesBA.ring_aura_gaia));
		ring_aura_gaia.setIcon(new ItemStack(ItemsBA.RING_AURA_GAIA));
		// Mana Stealer Blade
		mana_stealer_sword = new BasicLexiconEntry("ba_mana_stealer_sword", categoryBotanicAdditions).setPriority().setKnowledgeType(BotaniaAPI.elvenKnowledge).setLexiconPages(new PageText("1"), new PageCraftingRecipe("2", RecipesBA.mana_stealer_sword));
		mana_stealer_sword.setIcon(new ItemStack(ItemsBA.MANA_STEALER_SWORD));
	}
}