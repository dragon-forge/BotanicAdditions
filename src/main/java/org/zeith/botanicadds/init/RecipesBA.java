package org.zeith.botanicadds.init;

import net.minecraft.commands.CommandFunction;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.*;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Set;

@ProvideRecipes
public class RecipesBA
		implements IRecipeProvider
{
	@Override
	public void provideRecipes(RegisterRecipesEvent e)
	{
		pureDaisy(e);
		manaInfusionRecipes(e);
		petalApothecary(e);
		altar(e);
		elvenTrade(e);
		gaiaPlate(e);
		
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
		
		e.shaped().result(ItemsBA.GAIASTEEL_INGOT)
				.shape("lll", "lll", "lll")
				.map('l', ItemsBA.GAIASTEEL_NUGGET.getTag())
				.register();
		
		e.shapeless().add(BlocksBA.MANA_LAPIS_BLOCK.itemTag).result(new ItemStack(ItemsBA.MANA_LAPIS, 9)).register();
		e.shapeless().add(BlocksBA.ELVEN_LAPIS_BLOCK.itemTag).result(new ItemStack(ItemsBA.ELVEN_LAPIS, 9)).register();
		e.shapeless().add(BlocksBA.GAIASTEEL_BLOCK.itemTag).result(new ItemStack(ItemsBA.GAIASTEEL_INGOT, 9)).register();
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
	}
	
	public void pureDaisy(RegisterRecipesEvent e)
	{
		e.add(new PureDaisyRecipe(e.nextId(BotaniaBlocks.dreamwood.asItem()),
				new BlocksStateIngredient(Set.of(BlocksBA.ELVENWOOD_LOG, BlocksBA.ELVENWOOD)),
				BotaniaBlocks.dreamwood.defaultBlockState(),
				60 * 20,
				CommandFunction.CacheableFunction.NONE
		));
	}
	
	public void manaInfusionRecipes(RegisterRecipesEvent e)
	{
		int lapisInfusion = 5000;
		
		e.add(new ManaInfusionRecipe(e.nextId(ItemsBA.MANA_LAPIS),
				new ItemStack(ItemsBA.MANA_LAPIS),
				RecipeHelper.fromTag(Tags.Items.GEMS_LAPIS),
				lapisInfusion,
				null, null
		));
		
		e.add(new ManaInfusionRecipe(e.nextId(BlocksBA.MANA_LAPIS_BLOCK.asItem()),
				new ItemStack(BlocksBA.MANA_LAPIS_BLOCK),
				RecipeHelper.fromTag(Tags.Items.STORAGE_BLOCKS_LAPIS),
				lapisInfusion * 9,
				null, null
		));
		
		e.add(new ManaInfusionRecipe(e.nextId(ItemsBA.GAIA_SHARD),
				new ItemStack(ItemsBA.GAIA_SHARD, 8),
				Ingredient.of(BotaniaItems.lifeEssence),
				10000,
				null, new BlocksStateIngredient(Set.of(BlocksBA.TERRA_CATALYST))
		));
	}
	
	public void petalApothecary(RegisterRecipesEvent e)
	{
	
	}
	
	public void altar(RegisterRecipesEvent e)
	{
		e.add(new RunicAltarRecipe(e.nextId(ItemsBA.RUNE_TP), ItemsBA.RUNE_TP.getDefaultInstance(), 18_000,
				Ingredient.of(BotaniaItems.runeMana),
				Ingredient.of(Tags.Items.ENDER_PEARLS),
				Ingredient.of(BotaniaItems.manaDiamond),
				Ingredient.of(BotaniaItems.manaDiamond)
		));
		
		e.add(new RunicAltarRecipe(e.nextId(ItemsBA.RUNE_ENERGY), ItemsBA.RUNE_ENERGY.getDefaultInstance(), 18_000,
				Ingredient.of(BotaniaItems.runeFire),
				Ingredient.of(BotaniaItems.runeAir),
				Ingredient.of(BotaniaItems.manaDiamond),
				Ingredient.of(BotaniaItems.manaDiamond),
				Ingredient.of(Tags.Items.DUSTS_REDSTONE),
				Ingredient.of(Tags.Items.DUSTS_REDSTONE)
		));
	}
	
	public void elvenTrade(RegisterRecipesEvent e)
	{
		e.add(new ElvenTradeRecipe(e.nextId(ItemsBA.ELVEN_LAPIS),
				new ItemStack[] { ItemsBA.ELVEN_LAPIS.getDefaultInstance() },
				RecipeHelper.fromComponent(ItemsBA.MANA_LAPIS.getTag())
		));
		
		e.add(new ElvenTradeRecipe(e.nextId(BlocksBA.ELVEN_LAPIS_BLOCK.asItem()),
				new ItemStack[] { new ItemStack(BlocksBA.ELVEN_LAPIS_BLOCK) },
				RecipeHelper.fromComponent(BlocksBA.MANA_LAPIS_BLOCK.itemTag)
		));
		
		e.add(new ElvenTradeRecipe(e.nextId(BlocksBA.ELVENWOOD_LOG.asItem()),
				new ItemStack[] { new ItemStack(BlocksBA.ELVENWOOD_LOG) },
				RecipeHelper.fromComponent(ItemTags.OVERWORLD_NATURAL_LOGS)
		));
		
		e.add(new ElvenTradeRecipe(e.nextId(BlocksBA.DREAMROCK.asItem()),
				new ItemStack[] { new ItemStack(BlocksBA.DREAMROCK) },
				RecipeHelper.fromComponent(BotaniaBlocks.livingrock)
		));
	}
	
	public void gaiaPlate(RegisterRecipesEvent e)
	{
		e.add(new RecipeGaiaPlate(e.nextId(BotaniaItems.terrasteel),
				300_000,
				NonNullList.of(Ingredient.EMPTY,
						RecipeHelper.fromComponent(BotaniaItems.manaSteel),
						RecipeHelper.fromComponent(BotaniaItems.manaDiamond),
						RecipeHelper.fromComponent(BotaniaItems.manaPearl)
				),
				new ItemStack(BotaniaItems.terrasteel)
		));
		
		e.add(new RecipeGaiaPlate(e.nextId(ItemsBA.GAIASTEEL_INGOT),
				1_000_000,
				NonNullList.of(Ingredient.EMPTY,
						RecipeHelper.fromComponent(BotaniaItems.dragonstone),
						RecipeHelper.fromComponent(BotaniaItems.pixieDust),
						RecipeHelper.fromComponent(BotaniaItems.gaiaIngot)
				),
				new ItemStack(ItemsBA.GAIASTEEL_INGOT)
		));
	}
}