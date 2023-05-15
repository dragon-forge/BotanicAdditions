package org.zeith.botanicadds.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.botanicadds.client.particle.lightning.Bolt;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class PacketSpawnEnergizeraFX
		implements IPacket
{
	Vec3 start, end;
	
	public PacketSpawnEnergizeraFX(Vec3 start, Vec3 end)
	{
		this.start = start;
		this.end = end;
	}
	
	public PacketSpawnEnergizeraFX()
	{
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeDouble(start.x).writeDouble(start.y).writeDouble(start.z);
		buf.writeDouble(end.x).writeDouble(end.y).writeDouble(end.z);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		start = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		end = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		var level = Minecraft.getInstance().level;
		if(level == null) return;
		
		long seed = level.random.nextLong();
		
		var options = new Bolt(seed, 30, 1.5F, 2, new Bolt.Fractal(1, 30F),
				new Bolt.Layer(771, 0xff2222, true),
				new Bolt.Layer(772, 0xff0000, true)
		);
		
		level.addParticle(options,
				start.x, start.y, start.z,
				end.x, end.y, end.z
		);
	}
}
