package org.zeith.botanicadds.crafting;

import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.init.*;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SuppressWarnings("NonInterfaceSimplyRegister")
@SimplyRegister
public class RecipeAttuneTesseract
		extends ShapelessRecipe
{
	@RegistryName("attune_tesseract")
	public static final SimpleCraftingRecipeSerializer<RecipeAttuneTesseract> ATTUNE_TESSERACT = new SimpleCraftingRecipeSerializer<>(RecipeAttuneTesseract::new);
	
	public RecipeAttuneTesseract(ResourceLocation id, CraftingBookCategory cat)
	{
		super(id, "", cat, new ItemStack(BlocksBA.MANA_TESSERACT), Util.make(NonNullList.create(), lst ->
						{
							lst.add(Ingredient.of(ItemsBA.TESSERACT_ATTUNER));
							lst.add(Ingredient.of(TagsBA.Items.TESSERACT_ATTUNABLE));
						}
				)
		);
	}
	
	@Override
	public boolean isSpecial()
	{
		return true;
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
			if(it.isEmpty()) continue;
			
			++i;
			stackedcontents.accountStack(it, 1);
			
			if(!it.is(ItemsBA.TESSERACT_ATTUNER)) continue;
			
			CompoundTag display = it.getTagElement("display");
			if(display == null || !display.contains("Name", 8)) continue;
			
			try
			{
				Component component = Component.Serializer.fromJson(display.getString("Name"));
				if(component != null)
				{
					channel = component.getString();
					continue;
				}
				
				display.remove("Name");
			} catch(Exception exception)
			{
				display.remove("Name");
			}
		}
		
		return i == this.getIngredients().size()
			   && channel != null
			   && stackedcontents.canCraft(this, null);
	}
	
	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess registry)
	{
		var item = super.assemble(inv, registry);
		
		String channel = null;
		boolean channelPrivate = false;
		
		for(int j = 0; j < inv.getContainerSize(); ++j)
		{
			ItemStack it = inv.getItem(j);
			if(!it.isEmpty())
			{
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
				} else
				{
					item = it.copy().split(1);
				}
			}
		}
		
		if(channel == null) return ItemStack.EMPTY;
		
		item.getOrCreateTag().putBoolean("Private", channelPrivate);
		item.getOrCreateTag().putString("Channel", channel);
		
		return item;
	}
}