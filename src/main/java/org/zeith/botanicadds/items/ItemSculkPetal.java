package org.zeith.botanicadds.items;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.init.BlocksBA;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelShearsItem;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemSculkPetal
		extends Item
		implements IRegisterListener
{
	public static final TagKey<Item> PETALS_SCULK = ItemTags.create(new ResourceLocation("botania", "petals/sculk"));
	
	public ItemSculkPetal(Properties props)
	{
		super(props);
	}
	
	@Override
	public void onPostRegistered()
	{
		TagAdapter.bind(BotaniaTags.Items.PETALS, this);
		TagAdapter.bind(PETALS_SCULK, this);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags)
	{
		tooltip.add(Component.translatable(Util.makeDescriptionId("info", ForgeRegistries.ITEMS.getKey(this)),
						Blocks.SCULK_SENSOR.getName(),
						BotaniaItems.manasteelShears.getDefaultInstance().getHoverName()
				).withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
		);
	}
	
	@SubscribeEvent
	public static void rightClickBlock(PlayerInteractEvent.RightClickBlock e)
	{
		var level = e.getLevel();
		var pos = e.getPos();
		var it = e.getItemStack();
		
		if(it.getItem() instanceof ManasteelShearsItem)
			if(BlocksBA.REDUCED_SCULK_SENSOR.reduce(level, pos))
			{
				// Play a mix of 2 sounds to get something interesting, maybe?
				level.playSound(e.getEntity(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1F, 1F);
				level.playSound(e.getEntity(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.SCULK_SENSOR_HIT, SoundSource.PLAYERS, 1F, 0.4F);
				
				Block.popResource(level, pos, new ItemStack(ItemsBA.SCULK_PETAL, 4));
				it.hurtAndBreak(1, e.getEntity(), p -> p.broadcastBreakEvent(e.getHand()));
				
				e.setCancellationResult(InteractionResult.SUCCESS);
				e.setUseItem(Event.Result.DENY);
				e.setUseBlock(Event.Result.DENY);
				e.setCanceled(true);
			}
	}
}
