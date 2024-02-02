package org.zeith.botanicadds.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.lib.BotaniaTags;

import static org.zeith.botanicadds.init.ItemsBA.GAIASTEEL_RARITY;

public class ItemGaiaAuraRing
		extends BaubleItem
{
	public ItemGaiaAuraRing()
	{
		super(ItemsBA.baseProperties().stacksTo(1).rarity(GAIASTEEL_RARITY));
		TagAdapter.bind(ItemTags.create(new ResourceLocation("curios", "ring")), this);
		TagAdapter.bind(BotaniaTags.Items.TERRA_PICK_BLACKLIST, this);
	}
	
	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity)
	{
		if(!entity.level.isClientSide && entity instanceof Player player)
		{
			ManaItemHandler.instance().dispatchManaExact(stack, player, 1, true);
		}
	}
}