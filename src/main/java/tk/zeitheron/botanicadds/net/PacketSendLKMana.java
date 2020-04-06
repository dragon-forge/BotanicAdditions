package tk.zeitheron.botanicadds.net;

import tk.zeitheron.botanicadds.utils.ILastManaAknowledgedTile;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendLKMana implements IPacket
{
	static
	{
		IPacket.handle(PacketSendLKMana.class, PacketSendLKMana::new);
	}
	
	int mana;
	long pos;
	
	public PacketSendLKMana(ILastManaAknowledgedTile tile)
	{
		mana = tile.getCurrentMana();
		pos = tile.pos().toLong();
	}
	
	public PacketSendLKMana()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("m", mana);
		nbt.setLong("p", pos);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		mana = nbt.getInteger("m");
		pos = nbt.getLong("p");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		ILastManaAknowledgedTile t = WorldUtil.cast(Minecraft.getMinecraft().world.getTileEntity(BlockPos.fromLong(pos)), ILastManaAknowledgedTile.class);
		if(t != null)
			t.setLastKnownMana(mana);
		return null;
	}
}