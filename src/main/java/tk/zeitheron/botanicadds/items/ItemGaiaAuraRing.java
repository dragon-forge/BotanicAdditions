package tk.zeitheron.botanicadds.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.lib.LibItemNames;

public class ItemGaiaAuraRing extends Item implements IBauble, IManaGivingItem
{
	public ItemGaiaAuraRing()
	{
		setTranslationKey("ring_aura_gaia");
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player)
	{
		if(player instanceof EntityPlayer)
			ManaItemHandler.dispatchManaExact(stack, (EntityPlayer) player, 1, true);
	}
	
	@Deprecated
	public int getDelay()
	{
		return 1;
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.RING;
	}
}