package tk.zeitheron.botanicadds.net;

import java.util.concurrent.ThreadLocalRandom;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.net.internal.thunder.Thunder.Layer;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MainThreaded
public class PacketFXLine implements IPacket
{
	static
	{
		IPacket.handle(PacketFXLine.class, PacketFXLine::new);
	}
	
	public Vec3d start, end;
	public int particles, color;
	
	public PacketFXLine()
	{
	}
	
	public PacketFXLine(Vec3d start, Vec3d end, int details, int color)
	{
		this.start = start;
		this.end = end;
		this.particles = details;
		this.color = color;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		Helper.setVec3d(nbt, "s", start);
		Helper.setVec3d(nbt, "e", end);
		nbt.setInteger("p", particles);
		nbt.setInteger("c", color);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		start = Helper.getVec3d(nbt, "s");
		end = Helper.getVec3d(nbt, "e");
		particles = nbt.getInteger("p");
		color = nbt.getInteger("c");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		HammerCore.particleProxy.spawnSimpleThunder(Minecraft.getMinecraft().world, start, end, ThreadLocalRandom.current().nextLong(), particles, 1F, new Layer(771, color, true), new Layer(771, color, true));
		return null;
	}
}