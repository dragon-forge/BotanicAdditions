package org.zeith.botanicadds.net;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class PacketLeftClickManaStealerSword
		implements IPacket
{
	@Override
	public void serverExecute(PacketContext ctx)
	{
		var s = ctx.getSender();
		if(s != null) ItemsBA.MANA_STEALER_SWORD.trySpawnBurst(s);
	}
	
	@Mod.EventBusSubscriber(Dist.CLIENT)
	public static class Listener
	{
		@SubscribeEvent
		public static void leftClick(PlayerInteractEvent.LeftClickEmpty e)
		{
			Network.sendToServer(new PacketLeftClickManaStealerSword());
		}
	}
}