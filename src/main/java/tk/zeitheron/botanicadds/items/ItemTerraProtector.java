package tk.zeitheron.botanicadds.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

@EventBusSubscriber
public class ItemTerraProtector extends Item implements IBauble, IManaUsingItem
{
	public ItemTerraProtector()
	{
		setTranslationKey("terra_protector");
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.AMULET;
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player)
	{
		if(!(player instanceof EntityPlayerMP))
			return;
		
		EntityPlayerMP mp = (EntityPlayerMP) player;
		
		NBTTagCompound shield = stack.getOrCreateSubCompound("Shield");
		
		int heat = shield.getInteger("Heat");
		if(heat > 0)
			shield.setInteger("Heat", --heat);
		
		int LastUpdate = shield.getInteger("LastUpdate");
		if(LastUpdate > 0)
			shield.setInteger("LastUpdate", --LastUpdate);
		
		int charge = shield.getInteger("Charge");
		if(charge < 100_000)
			charge += ManaItemHandler.requestMana(stack, mp, 100_000 - charge, true);
		
		int def = shield.getInteger("Defense");
		if(def < 100)
		{
			int times = heat / 10;
			
			if(shield.getInteger("LastUpdate") <= 0)
			{
				++def;
				shield.setInteger("Defense", def);
				shield.setInteger("LastUpdate", times);
			}
		}
		
		shield.setInteger("Charge", charge);
	}
	
	@Override
	public boolean usesMana(ItemStack stack)
	{
		return true;
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void handleDamage(LivingAttackEvent e)
	{
		if(e.getEntityLiving() instanceof EntityPlayer)
		{
			IBaublesItemHandler h = BaublesApi.getBaublesHandler((EntityPlayer) e.getEntityLiving());
			for(int i : BaubleType.AMULET.getValidSlots())
			{
				ItemStack stack = h.getStackInSlot(i);
				if(!e.isCanceled() && !stack.isEmpty() && stack.getItem() instanceof ItemTerraProtector)
				{
					NBTTagCompound shield = stack.getOrCreateSubCompound("Shield");
					
					int def = shield.getInteger("Defense");
					
					if(def > 0)
					{
						int take = Math.min(10, (int) Math.cbrt(1 + e.getAmount()));
						def = Math.max(0, def - take);
						shield.setInteger("Defense", def);
						shield.setInteger("Heat", Math.min(1000, shield.getInteger("Heat") + 20 * take));
						
						e.setCanceled(true);
					}
				}
			}
		}
	}
}