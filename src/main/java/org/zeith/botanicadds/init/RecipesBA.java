package org.zeith.botanicadds.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.api.recipes.BotanicAdditionsRecipeExtension;
import org.zeith.botanicadds.crafting.RecipeAttuneTesseract;
import org.zeith.botanicadds.items.ItemSculkPetal;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.event.recipe.SpoofRecipesEvent;
import org.zeith.hammerlib.util.mcf.RecipeRegistrationContext;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Arrays;

@ProvideRecipes
public class RecipesBA
		implements IRecipeProvider
{
	public static final ResourceLocation OLD_GAIA_MANA_SPREADER_ID = new ResourceLocation("botania", "gaia_spreader");
	public static final ResourceLocation OLD_MANA_FLUXFIELD_ID = new ResourceLocation("botania", "mana_fluxfield");
	
	@Override
	public void provideRecipes(RegisterRecipesEvent e)
	{
		RecipeRegistrationContext ctx = e.getContext(BotanicAdditions.MOD_ID);
		
		var ba = e.extension(BotanicAdditionsRecipeExtension.class);
		if(ba == null)
		{
			BotanicAdditions.LOG.error("Failed to grab BotanicAdditionsRecipeExtension while reloading recipes.");
			return;
		}
		
		pureDaisy(ctx, ba);
		manaInfusionRecipes(ctx, ba);
		petalApothecary(ctx, ba);
		altar(ctx, ba);
		elvenTrade(ctx, ba);
		gaiaPlate(ctx, ba);
		
		ctx.register(e.shaped().result(BlocksBA.TERRA_CATALYST)
				.shape("sgs", "tct", "sts")
				.map('s', BotaniaBlocks.shimmerrock)
				.map('t', BotaniaTags.Items.INGOTS_TERRASTEEL)
				.map('g', BotaniaItems.lifeEssence)
				.map('c', BotaniaBlocks.alchemyCatalyst));
		
		ctx.register(e.shaped().result(BlocksBA.MANA_LAPIS_BLOCK)
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.MANA_LAPIS.getTag()));
		
		ctx.register(e.shaped().result(BlocksBA.ELVEN_LAPIS_BLOCK)
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.ELVEN_LAPIS.getTag()));
		
		ctx.register(e.shaped().result(BlocksBA.GAIASTEEL_BLOCK)
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.GAIASTEEL_INGOT.getTag()));
		
		ctx.register(e.shaped().result(ItemsBA.GAIASTEEL_INGOT).id(BotanicAdditions.id("gaiasteel_ingot_from_nuggets"))
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.GAIASTEEL_NUGGET.getTag()));
		
		ctx.register(e.shapeless().add(BlocksBA.MANA_LAPIS_BLOCK.itemTag).result(new ItemStack(ItemsBA.MANA_LAPIS, 9)));
		ctx.register(e.shapeless().add(BlocksBA.ELVEN_LAPIS_BLOCK.itemTag).result(new ItemStack(ItemsBA.ELVEN_LAPIS, 9)));
		ctx.register(e.shapeless().add(BlocksBA.GAIASTEEL_BLOCK.itemTag).id(BotanicAdditions.id("gaiasteel_ingots_from_block")).result(new ItemStack(ItemsBA.GAIASTEEL_INGOT, 9)));
		ctx.register(e.shapeless().add(ItemsBA.GAIASTEEL_INGOT.getTag()).result(new ItemStack(ItemsBA.GAIASTEEL_NUGGET, 9)));
		
		ctx.register(e.shaped().result(BlocksBA.GAIA_PLATE)
				.shape("lll", "etp", "ggg")
				.map('l', BlocksBA.ELVEN_LAPIS_BLOCK.itemTag)
				.map('e', ItemsBA.RUNE_ENERGY)
				.map('t', BotaniaBlocks.terraPlate)
				.map('p', ItemsBA.RUNE_TP)
				.map('g', ItemsBA.GAIA_SHARD));
		
		ctx.register(e.shaped().result(BlocksBA.GAIASTEEL_PYLON)
				.shape(" g ", "xpx", " g ")
				.map('g', ItemsBA.GAIASTEEL_NUGGET.getTag())
				.map('p', BotaniaBlocks.naturaPylon)
				.map('x', BotaniaBlocks.dreamwoodGlimmering));
		
		ctx.register(e.shaped().result(new ItemStack(BlocksBA.ELVENWOOD, 3))
				.shape("ll", "ll")
				.map('l', BlocksBA.ELVENWOOD_LOG));
		
		ctx.register(e.shapeless().result(ItemsBA.AURA_RING_GAIA)
				.add(ItemsBA.GAIASTEEL_INGOT.getTag())
				.add(BotaniaItems.auraRingGreater));
		
		ctx.register(e.shapeless().result(ItemsBA.MANA_RING_GAIA)
				.add(ItemsBA.GAIASTEEL_INGOT.getTag())
				.add(BotaniaItems.manaRingGreater));
		
		ctx.register(e.shaped().result(ItemsBA.MANA_STEALER_SWORD)
				.shape("g", "g", "s")
				.map('g', ItemsBA.GAIASTEEL_INGOT.getTag())
				.map('s', BotaniaItems.terraSword));
		
		ctx.register(e.shaped().result(BlocksBA.ELVEN_ALTAR)
				.shape("rrr", "rdr", "rar")
				.map('r', BlocksBA.DREAMROCK)
				.map('d', BotaniaItems.dragonstone)
				.map('a', BotaniaBlocks.runeAltar));
		
		ctx.register(e.shaped().result(BlocksBA.ELVEN_BREWERY)
				.shape("rrr", "rdr", "rar")
				.map('r', BlocksBA.DREAMROCK)
				.map('d', BotaniaItems.dragonstone)
				.map('a', BotaniaBlocks.brewery));
		
		ctx.register(e.shaped().result(BlocksBA.DREAMING_POOL)
				.shape("ttt", "dpd", "ddd")
				.map('d', BlocksBA.DREAMROCK)
				.map('p', BotaniaBlocks.manaPool)
				.map('t', BotaniaTags.Items.NUGGETS_TERRASTEEL));
		
		ctx.register(e.shaped().result(Blocks.SCULK_SENSOR).id(BotanicAdditions.id("sculk_sensor"))
				.shape("p p", "prp")
				.map('p', ItemsBA.SCULK_PETAL)
				.map('r', BlocksBA.REDUCED_SCULK_SENSOR));
		
		var tessAttuneId = BotanicAdditions.id("tesseract_attune");
		if(ctx.enableRecipe(tessAttuneId)) e.add(new RecipeAttuneTesseract(tessAttuneId));
		
		ctx.register(e.shaped().id(BotanicAdditions.id("recipe_tweaks/mana_fluxfield"))
				.shape("lrl", "rer", "lrl")
				.map('l', BotaniaBlocks.livingrock)
				.map('r', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.map('e', ItemsBA.RUNE_ENERGY)
				.result(BotaniaBlocks.rfGenerator)
		).ifPresent(recipe -> e.removeRecipe(OLD_MANA_FLUXFIELD_ID));
		
		ctx.register(e.shapeless().id(BotanicAdditions.id("recipe_tweaks/gaia_spreader"))
				.result(BotaniaBlocks.gaiaSpreader)
				.addAll(BotaniaBlocks.elvenSpreader, BotaniaItems.dragonstone, ItemsBA.GAIA_SHARD)
		).ifPresent(recipe -> e.removeRecipe(OLD_GAIA_MANA_SPREADER_ID));
		
		ctx.register(e.shaped().result(BlocksBA.ELVEN_FLUX_FIELD)
				.shape("lrl", "rer", "lrl")
				.map('l', BlocksBA.DREAMROCK)
				.map('r', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.map('e', BotaniaBlocks.rfGenerator));
		
		ctx.save();
	}
	
	@Override
	public void spoofRecipes(SpoofRecipesEvent e)
	{
		e.spoofRecipe(OLD_MANA_FLUXFIELD_ID, BotanicAdditions.id("mana_fluxfield"));
		e.spoofRecipe(OLD_GAIA_MANA_SPREADER_ID, BotanicAdditions.id("gaia_spreader"));
	}
	
	public void pureDaisy(RecipeRegistrationContext ctx, BotanicAdditionsRecipeExtension e)
	{
		e.pureDaisy().result(BotaniaBlocks.dreamwood).id(BotanicAdditions.id("dreamwood_from_elvenwood"))
				.input(BlocksBA.ELVENWOOD_LOG, BlocksBA.ELVENWOOD)
				.register(ctx);
	}
	
	public void manaInfusionRecipes(RecipeRegistrationContext ctx, BotanicAdditionsRecipeExtension e)
	{
		int lapisInfusion = 5000;
		
		ctx.register(e.manaPool().result(ItemsBA.MANA_LAPIS)
				.input(Tags.Items.GEMS_LAPIS)
				.mana(lapisInfusion));
		
		ctx.register(e.manaPool().result(BlocksBA.MANA_LAPIS_BLOCK)
				.input(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.mana(lapisInfusion * 9));
		
		ctx.register(e.manaPool().result(ItemsBA.GAIA_SHARD, 8)
				.input(BotaniaItems.lifeEssence)
				.mana(10_000)
				.catalyst(BlocksBA.TERRA_CATALYST));
	}
	
	public void petalApothecary(RecipeRegistrationContext ctx, BotanicAdditionsRecipeExtension e)
	{
		var petalsBlack = BotaniaTags.Items.PETALS_BLACK;
		var petalsBlue = BotaniaTags.Items.PETALS_BLUE;
		var petalsBrown = BotaniaTags.Items.PETALS_BROWN;
		var petalsCyan = BotaniaTags.Items.PETALS_CYAN;
		var petalsGray = BotaniaTags.Items.PETALS_GRAY;
		var petalsGreen = BotaniaTags.Items.PETALS_GREEN;
		var petalsLightBlue = BotaniaTags.Items.PETALS_LIGHT_BLUE;
		var petalsLightGray = BotaniaTags.Items.PETALS_LIGHT_GRAY;
		var petalsLime = BotaniaTags.Items.PETALS_LIME;
		var petalsMagenta = BotaniaTags.Items.PETALS_MAGENTA;
		var petalsOrange = BotaniaTags.Items.PETALS_ORANGE;
		var petalsPink = BotaniaTags.Items.PETALS_PINK;
		var petalsPurple = BotaniaTags.Items.PETALS_PURPLE;
		var petalsRed = BotaniaTags.Items.PETALS_RED;
		var petalsWhite = BotaniaTags.Items.PETALS_WHITE;
		var petalsYellow = BotaniaTags.Items.PETALS_YELLOW;
		var petalsSculk = ItemSculkPetal.PETALS_SCULK;
		
		var zeithHead = new ItemStack(Items.PLAYER_HEAD);
		ItemNBTHelper.setString(zeithHead, "SkullOwner", "Zeitheron");
		Object[] lightBluePetals = new Object[16];
		Arrays.fill(lightBluePetals, petalsLightBlue);
		e.petalApothecary().result(zeithHead).id(BotanicAdditions.id("zeitheron_head"))
				.addAll(lightBluePetals)
				.register();
		
		ctx.register(e.petalApothecary().result(FlowersBA.NECROIDUS)
				.addAll(petalsBlack, petalsBlack, petalsBlack, petalsBlack, petalsGray, petalsGray, petalsGray, petalsGray)
				.addAll(Items.WITHER_SKELETON_SKULL, BotaniaItems.redstoneRoot, BotaniaItems.runeGluttony, BotaniaItems.runeWrath));
		
		ctx.register(e.petalApothecary().result(FlowersBA.RAINUTE)
				.addAll(petalsBlue, petalsBlue, petalsBlue, petalsBlue, petalsLightBlue, petalsLightBlue, petalsYellow));
		
		ctx.register(e.petalApothecary().result(FlowersBA.GLACIFLORA)
				.addAll(petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsWhite, petalsWhite, petalsWhite));
		
		ctx.register(e.petalApothecary().result(FlowersBA.TEMPESTEA)
				.addAll(petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsBlue)
				.add(ItemsBA.RUNE_ENERGY));
		
		ctx.register(e.petalApothecary().result(FlowersBA.VIBRANTIA)
				.addAll(petalsSculk, petalsSculk, petalsGray, petalsBlack, petalsBlack)
				.add(BotaniaItems.runeMana));
		
		ctx.register(e.petalApothecary().result(FlowersBA.APICARIA)
				.add(Items.HONEYCOMB)
				.addAll(petalsYellow, petalsYellow, petalsOrange, petalsOrange)
				.add(BotaniaItems.runeSpring));
		
		ctx.register(e.petalApothecary().result(FlowersBA.ENERGIZERA)
				.add(ItemsBA.RUNE_ENERGY)
				.addAll(petalsRed, petalsRed, petalsRed, petalsGreen));
	}
	
	public void altar(RecipeRegistrationContext ctx, BotanicAdditionsRecipeExtension e)
	{
		ctx.register(e.runicAltar().result(ItemsBA.RUNE_TP)
				.addAll(Tags.Items.ENDER_PEARLS, BotaniaItems.runeMana, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond)
				.mana(18_000));
		
		ctx.register(e.runicAltar().result(ItemsBA.RUNE_ENERGY)
				.addAll(BotaniaItems.runeFire, BotaniaItems.runeAir, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond)
				.addAll(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE)
				.mana(18_000));
		
		ctx.register(e.runicAltar().result(BlocksBA.MANA_TESSERACT)
				.addAll(ItemsBA.RUNE_TP, BlocksBA.DREAMROCK, BotaniaTags.Items.INGOTS_TERRASTEEL, BotaniaItems.redString)
				.mana(50_000));
		
		ctx.register(e.runicAltar().result(ItemsBA.TESSERACT_ATTUNER)
				.addAll(BotaniaItems.dreamwoodTwig, BotaniaItems.runeMana, BotaniaItems.redString, Items.NAME_TAG)
				.mana(10_000));
	}
	
	public void elvenTrade(RecipeRegistrationContext ctx, BotanicAdditionsRecipeExtension e)
	{
		e.elvenTrade().result(ItemsBA.ELVEN_LAPIS)
				.input(ItemsBA.MANA_LAPIS.getTag())
				.register(ctx);
		
		e.elvenTrade().result(BlocksBA.ELVEN_LAPIS_BLOCK)
				.input(BlocksBA.MANA_LAPIS_BLOCK.itemTag)
				.register(ctx);
		
		e.elvenTrade().result(BlocksBA.ELVENWOOD_LOG)
				.input(ItemTags.OVERWORLD_NATURAL_LOGS)
				.register(ctx);
		
		e.elvenTrade().result(BlocksBA.DREAMROCK)
				.input(BotaniaBlocks.livingrock)
				.register(ctx);
	}
	
	public void gaiaPlate(RecipeRegistrationContext ctx, BotanicAdditionsRecipeExtension e)
	{
		ctx.register(e.gaiaPlate().result(BotaniaItems.terrasteel).id(BotanicAdditions.id("terrasteel"))
				.addAll(BotaniaItems.manaSteel, BotaniaItems.manaDiamond, BotaniaItems.manaPearl)
				.mana(300_000));
		
		ctx.register(e.gaiaPlate().result(ItemsBA.GAIASTEEL_INGOT)
				.addAll(BotaniaItems.dragonstone, BotaniaItems.pixieDust, BotaniaItems.gaiaIngot)
				.mana(1_000_000));
		
		ctx.register(e.gaiaPlate().result(BotaniaItems.overgrowthSeed).id(BotanicAdditions.id("overgrowth_seed"))
				.addAll(BotaniaTags.Items.NUGGETS_TERRASTEEL, BotaniaItems.grassSeeds, ItemsBA.GAIA_SHARD)
				.mana(250_000));
	}
}