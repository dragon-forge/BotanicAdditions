package org.zeith.botanicadds.net;

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
}