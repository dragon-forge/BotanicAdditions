package org.zeith.botanicadds.items;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;
import vazkii.botania.common.item.equipment.bauble.GreaterBandOfManaItem;
import vazkii.botania.forge.CapabilityUtil;

import java.util.Set;

import static org.zeith.botanicadds.init.ItemsBA.GAIASTEEL_RARITY;

public class ItemGaiaManaBand
		extends GreaterBandOfManaItem
		implements ITabItem
{
	private static final int MAX_MANA = (BandOfManaItem.MAX_MANA * 4) * 4;
	
	public ItemGaiaManaBand()
	{
		super(ItemsBA.baseProperties().stacksTo(1).rarity(GAIASTEEL_RARITY));
		TagAdapter.bind(ItemTags.create(new ResourceLocation("curios", "ring")), this);
	}
	
	@Override
	public CreativeModeTab getItemCategory()
	{
		return BotanicAdditions.TAB.tab();
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab tab, Set<ItemStack> items)
	{
		if(allowedIn(tab))
		{
			items.add(new ItemStack(this));
			
			ItemStack full = new ItemStack(this);
			setMana(full, MAX_MANA);
			items.add(full);
		}
	}
	
	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		return CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_ITEM, new GaiaManaItemImpl(stack));
	}
	
	
	public static class GaiaManaItemImpl
			extends ManaItemImpl
	{
		public GaiaManaItemImpl(ItemStack stack)
		{
			super(stack);
		}
		
		@Override
		public int getMaxMana()
		{
			return MAX_MANA * stack.getCount();
		}
	}
}
