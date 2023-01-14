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
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;
import vazkii.botania.common.item.equipment.bauble.GreaterBandOfManaItem;
import vazkii.botania.forge.CapabilityUtil;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class ItemGaiaManaBand
		extends GreaterBandOfManaItem
{
	private static final int MAX_MANA = (BandOfManaItem.MAX_MANA * 4) * 4;
	
	public ItemGaiaManaBand()
	{
		super(new Properties().tab(TAB).stacksTo(1));
		TagAdapter.bind(ItemTags.create(new ResourceLocation("curios", "ring")), this);
	}
	
	@Override
	public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> stacks)
	{
		if(allowedIn(tab))
		{
			stacks.add(new ItemStack(this));
			
			ItemStack full = new ItemStack(this);
			setMana(full, MAX_MANA);
			stacks.add(full);
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
