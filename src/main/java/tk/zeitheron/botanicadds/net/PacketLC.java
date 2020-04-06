package tk.zeitheron.botanicadds.net;

import tk.zeitheron.botanicadds.init.ItemsBA;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

@MainThreaded
public class PacketLC implements IPacket
{
	static
	{
		IPacket.handle(PacketLC.class, PacketLC::new);
	}
	
	@Override
	public IPacket executeOnServer(PacketContext net)
	{
		ItemsBA.MANA_STEALER_SWORD.trySpawnBurst(net.getSender());
		return null;
	}
}