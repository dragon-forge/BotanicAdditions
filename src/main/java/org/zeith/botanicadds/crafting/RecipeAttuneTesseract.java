package org.zeith.botanicadds.crafting;

import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.init.BlocksBA;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public class RecipeAttuneTesseract
		extends ShapelessRecipe
{
	@RegistryName("attune_tesseract")
	public static final SimpleRecipeSerializer<RecipeAttuneTesseract> ATTUNE_TESSERACT = new SimpleRecipeSerializer<>(RecipeAttuneTesseract::new);
	
	public RecipeAttuneTesseract(ResourceLocation id)
	{
		super(id, BotanicAdditions.MOD_ID + "_tesseract_attune", new ItemStack(BlocksBA.MANA_TESSERACT), Util.make(NonNullList.create(), lst ->
		{
			lst.add(Ingredient.of(ItemsBA.TESSERACT_ATTUNER));
			lst.add(Ingredient.of(BlocksBA.MANA_TESSERACT.asItem()));
		}));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return ATTUNE_TESSERACT;
	}
	
	@Override
	public boolean matches(CraftingContainer inv, Level lvl)
	{
		StackedContents stackedcontents = new StackedContents();
		int i = 0;
		
		String channel = null;
		
		for(int j = 0; j < inv.getContainerSize(); ++j)
		{
			ItemStack it = inv.getItem(j);
			if(!it.isEmpty())
			{
				++i;
				stackedcontents.accountStack(it, 1);
				
				if(it.is(ItemsBA.TESSERACT_ATTUNER))
				{
					CompoundTag compoundtag = it.getTagElement("display");
					if(compoundtag != null && compoundtag.contains("Name", 8))
					{
						try
						{
							Component component = Component.Serializer.fromJson(compoundtag.getString("Name"));
							if(component != null)
							{
								channel = component.getString();
								continue;
							}
							
							compoundtag.remove("Name");
						} catch(Exception exception)
						{
							compoundtag.remove("Name");
						}
					}
				}
			}
		}
		
		return i == this.getIngredients().size() && channel != null && stackedcontents.canCraft(this, null);
	}
	
	@Override
	public ItemStack assemble(CraftingContainer inv)
	{
		var item = super.assemble(inv);
		
		String channel = null;
		boolean channelPrivate = false;
		
		for(int j = 0; j < inv.getContainerSize(); ++j)
		{
			ItemStack it = inv.getItem(j);
			if(!it.isEmpty())
				if(it.is(ItemsBA.TESSERACT_ATTUNER))
				{
					CompoundTag compoundtag = it.getTagElement("display");
					if(compoundtag != null && compoundtag.contains("Name", 8))
					{
						try
						{
							Component component = Component.Serializer.fromJson(compoundtag.getString("Name"));
							if(component != null)
							{
								channel = component.getString();
								channelPrivate = ItemsBA.TESSERACT_ATTUNER.isPrivate(it);
								continue;
							}
							
							compoundtag.remove("Name");
						} catch(Exception exception)
						{
							compoundtag.remove("Name");
						}
					}
				}
		}
		
		if(channel == null) return ItemStack.EMPTY;
		
		item.getOrCreateTag().putBoolean("Private", channelPrivate);
		item.getOrCreateTag().putString("Channel", channel);
		
		return item;
	}
}