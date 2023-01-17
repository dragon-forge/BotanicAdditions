package org.zeith.botanicadds.init;

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
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Arrays;

@ProvideRecipes
public class RecipesBA
		implements IRecipeProvider
{
	@Override
	public void provideRecipes(RegisterRecipesEvent e)
	{
		var ba = e.extension(BotanicAdditionsRecipeExtension.class);
		if(ba == null)
		{
			BotanicAdditions.LOG.error("Failed to grab BotanicAdditionsRecipeExtension while reloading recipes.");
			return;
		}
		
		pureDaisy(ba);
		manaInfusionRecipes(ba);
		petalApothecary(ba);
		altar(ba);
		elvenTrade(ba);
		gaiaPlate(ba);
		
		e.shaped().result(BlocksBA.TERRA_CATALYST)
				.shape("sgs", "tct", "sts")
				.map('s', BotaniaBlocks.shimmerrock)
				.map('t', BotaniaTags.Items.INGOTS_TERRASTEEL)
				.map('g', BotaniaItems.lifeEssence)
				.map('c', BotaniaBlocks.alchemyCatalyst)
				.register();
		
		e.shaped().result(BlocksBA.MANA_LAPIS_BLOCK)
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.MANA_LAPIS.getTag())
				.register();
		
		e.shaped().result(BlocksBA.ELVEN_LAPIS_BLOCK)
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.ELVEN_LAPIS.getTag())
				.register();
		
		e.shaped().result(BlocksBA.GAIASTEEL_BLOCK)
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.GAIASTEEL_INGOT.getTag())
				.register();
		
		e.shaped().result(ItemsBA.GAIASTEEL_INGOT).id(BotanicAdditions.id("gaiasteel_ingot_from_nuggets"))
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.GAIASTEEL_NUGGET.getTag())
				.register();
		
		e.shapeless().add(BlocksBA.MANA_LAPIS_BLOCK.itemTag).result(new ItemStack(ItemsBA.MANA_LAPIS, 9)).register();
		e.shapeless().add(BlocksBA.ELVEN_LAPIS_BLOCK.itemTag).result(new ItemStack(ItemsBA.ELVEN_LAPIS, 9)).register();
		e.shapeless().add(BlocksBA.GAIASTEEL_BLOCK.itemTag).id(BotanicAdditions.id("gaiasteel_ingots_from_block")).result(new ItemStack(ItemsBA.GAIASTEEL_INGOT, 9)).register();
		e.shapeless().add(ItemsBA.GAIASTEEL_INGOT.getTag()).result(new ItemStack(ItemsBA.GAIASTEEL_NUGGET, 9)).register();
		
		e.shaped().result(BlocksBA.GAIA_PLATE)
				.shape("lll", "etp", "ggg")
				.map('l', BlocksBA.ELVEN_LAPIS_BLOCK.itemTag)
				.map('e', ItemsBA.RUNE_ENERGY)
				.map('t', BotaniaBlocks.terraPlate)
				.map('p', ItemsBA.RUNE_TP)
				.map('g', ItemsBA.GAIA_SHARD)
				.register();
		
		e.shaped().result(new ItemStack(BlocksBA.ELVENWOOD, 3))
				.shape("ll", "ll")
				.map('l', BlocksBA.ELVENWOOD_LOG)
				.register();
		
		e.shapeless().result(ItemsBA.AURA_RING_GAIA)
				.add(ItemsBA.GAIASTEEL_INGOT.getTag())
				.add(BotaniaItems.auraRingGreater)
				.register();
		
		e.shapeless().result(ItemsBA.MANA_RING_GAIA)
				.add(ItemsBA.GAIASTEEL_INGOT.getTag())
				.add(BotaniaItems.manaRingGreater)
				.register();
		
		e.shaped().result(ItemsBA.MANA_STEALER_SWORD)
				.shape("g", "g", "s")
				.map('g', ItemsBA.GAIASTEEL_INGOT.getTag())
				.map('s', BotaniaItems.terraSword)
				.register();
		
		e.shaped().result(BlocksBA.ELVEN_ALTAR)
				.shape("rrr", "rdr", "rar")
				.map('r', BlocksBA.DREAMROCK)
				.map('d', BotaniaItems.dragonstone)
				.map('a', BotaniaBlocks.runeAltar)
				.register();
		
		e.shaped().result(BlocksBA.ELVEN_BREWERY)
				.shape("rrr", "rdr", "rar")
				.map('r', BlocksBA.DREAMROCK)
				.map('d', BotaniaItems.dragonstone)
				.map('a', BotaniaBlocks.brewery)
				.register();
		
		e.shaped().result(BlocksBA.DREAMING_POOL)
				.shape("ttt", "dpd", "ddd")
				.map('d', BlocksBA.DREAMROCK)
				.map('p', BotaniaBlocks.manaPool)
				.map('t', BotaniaTags.Items.NUGGETS_TERRASTEEL)
				.register();
		
		e.shaped().result(Blocks.SCULK_SENSOR).id(BotanicAdditions.id("sculk_sensor"))
				.shape("p p", "prp")
				.map('p', ItemsBA.SCULK_PETAL)
				.map('r', BlocksBA.REDUCED_SCULK_SENSOR)
				.register();
		
		e.add(new RecipeAttuneTesseract(BotanicAdditions.id("tesseract_attune")));
	}
	
	public void pureDaisy(BotanicAdditionsRecipeExtension e)
	{
		e.pureDaisy().result(BotaniaBlocks.dreamwood)
				.input(BlocksBA.ELVENWOOD_LOG, BlocksBA.ELVENWOOD)
				.register();
	}
	
	public void manaInfusionRecipes(BotanicAdditionsRecipeExtension e)
	{
		int lapisInfusion = 5000;
		
		e.manaPool().result(ItemsBA.MANA_LAPIS)
				.input(Tags.Items.GEMS_LAPIS)
				.mana(lapisInfusion)
				.register();
		
		e.manaPool().result(BlocksBA.MANA_LAPIS_BLOCK)
				.input(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.mana(lapisInfusion * 9)
				.register();
		
		e.manaPool().result(ItemsBA.GAIA_SHARD, 8)
				.input(BotaniaItems.lifeEssence)
				.mana(10_000)
				.catalyst(BlocksBA.TERRA_CATALYST)
				.register();
	}
	
	public void petalApothecary(BotanicAdditionsRecipeExtension e)
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
		e.petalApothecary().result(zeithHead)
				.addAll(lightBluePetals)
				.register();
		
		e.petalApothecary().result(FlowersBA.NECROIDUS)
				.addAll(petalsBlack, petalsBlack, petalsBlack, petalsBlack, petalsGray, petalsGray, petalsGray, petalsGray)
				.addAll(Items.WITHER_SKELETON_SKULL, BotaniaItems.redstoneRoot, BotaniaItems.runeGluttony, BotaniaItems.runeWrath)
				.register();
		
		e.petalApothecary().result(FlowersBA.RAINUTE)
				.addAll(petalsBlue, petalsBlue, petalsBlue, petalsBlue, petalsLightBlue, petalsLightBlue, petalsYellow)
				.register();
		
		e.petalApothecary().result(FlowersBA.GLACIFLORA)
				.addAll(petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsWhite, petalsWhite, petalsWhite)
				.register();
		
		e.petalApothecary().result(FlowersBA.TEMPESTEA)
				.addAll(petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsLightBlue, petalsBlue)
				.add(ItemsBA.RUNE_ENERGY)
				.register();
		
		e.petalApothecary().result(FlowersBA.VIBRANTIA)
				.addAll(petalsSculk, petalsSculk, petalsGray, petalsBlack, petalsBlack)
				.add(BotaniaItems.runeMana)
				.register();
		
		e.petalApothecary().result(FlowersBA.APICARIA)
				.add(Items.HONEYCOMB)
				.addAll(petalsYellow, petalsYellow, petalsOrange, petalsOrange)
				.add(BotaniaItems.runeSpring)
				.register();
	}
	
	public void altar(BotanicAdditionsRecipeExtension e)
	{
		e.runicAltar().result(ItemsBA.RUNE_TP)
				.addAll(Tags.Items.ENDER_PEARLS, BotaniaItems.runeMana, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond)
				.mana(18_000)
				.register();
		
		e.runicAltar().result(ItemsBA.RUNE_ENERGY)
				.addAll(BotaniaItems.runeFire, BotaniaItems.runeAir, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond)
				.addAll(Tags.Items.DUSTS_REDSTONE, Tags.Items.DUSTS_REDSTONE)
				.mana(18_000)
				.register();
		
		e.runicAltar().result(BlocksBA.MANA_TESSERACT)
				.addAll(ItemsBA.RUNE_TP, BlocksBA.DREAMROCK, BotaniaTags.Items.INGOTS_TERRASTEEL, BotaniaItems.redString)
				.mana(50_000)
				.register();
		
		e.runicAltar().result(ItemsBA.TESSERACT_ATTUNER)
				.addAll(BotaniaItems.dreamwoodTwig, BotaniaItems.runeMana, BotaniaItems.redString, Items.NAME_TAG)
				.mana(10_000)
				.register();
	}
	
	public void elvenTrade(BotanicAdditionsRecipeExtension e)
	{
		e.elvenTrade().result(ItemsBA.ELVEN_LAPIS)
				.input(ItemsBA.MANA_LAPIS.getTag())
				.register();
		
		e.elvenTrade().result(BlocksBA.ELVEN_LAPIS_BLOCK)
				.input(BlocksBA.MANA_LAPIS_BLOCK.itemTag)
				.register();
		
		e.elvenTrade().result(BlocksBA.ELVENWOOD_LOG)
				.input(ItemTags.OVERWORLD_NATURAL_LOGS)
				.register();
		
		e.elvenTrade().result(BlocksBA.DREAMROCK)
				.input(BotaniaBlocks.livingrock)
				.register();
	}
	
	public void gaiaPlate(BotanicAdditionsRecipeExtension e)
	{
		e.gaiaPlate().result(BotaniaItems.terrasteel).id(BotanicAdditions.id("terrasteel"))
				.addAll(BotaniaItems.manaSteel, BotaniaItems.manaDiamond, BotaniaItems.manaPearl)
				.mana(300_000)
				.register();
		
		e.gaiaPlate().result(ItemsBA.GAIASTEEL_INGOT)
				.addAll(BotaniaItems.dragonstone, BotaniaItems.pixieDust, BotaniaItems.gaiaIngot)
				.mana(1_000_000)
				.register();
	}
}