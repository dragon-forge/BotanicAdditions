package org.zeith.botanicadds.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;

import java.util.List;

public class ItemTesseractAttuner
		extends Item
{
	public ItemTesseractAttuner(Properties props)
	{
		super(props.stacksTo(1));
	}
	
	public boolean isPrivate(ItemStack stack)
	{
		var tag = stack.getTag();
		return tag != null && tag.getBoolean("Private");
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		if(player.isShiftKeyDown())
		{
			var it = player.getItemInHand(hand);
			it.getOrCreateTag().putBoolean("Private", !isPrivate(it));
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, it);
		}
		
		return super.use(level, player, hand);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner1")
				.withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
		);
		
		tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner2")
				.withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
		);
		
		tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner3")
				.withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
		);
		
		int modeColor = 0x22AA22;
		String mode = "public";
		
		if(isPrivate(stack))
		{
			modeColor = 0xAA2222;
			mode = "private";
		}
		
		tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner.mode",
						Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner.mode." + mode)
								.withStyle(Style.EMPTY.withColor(modeColor))
				).withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
		);
	}
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack)
	{
		return stack.copy().split(1);
	}
	
	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack)
	{
		return true;
	}
}
