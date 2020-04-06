package tk.zeitheron.botanicadds.net;

import tk.zeitheron.botanicadds.blocks.tiles.TileGaiaPlate;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.common.Botania;

public class PacketGaiaPlateEffect implements IPacket
{
	public BlockPos pos;
	
	static
	{
		IPacket.handle(PacketGaiaPlateEffect.class, PacketGaiaPlateEffect::new);
	}
	
	public void setPos(BlockPos pos)
	{
		this.pos = pos;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("Pos", pos.toLong());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		pos = BlockPos.fromLong(nbt.getLong("Pos"));
	}
	
	@Override
	public IPacket executeOnClient(PacketContext net)
	{
		TileEntity te = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(pos));
		if(te instanceof TileGaiaPlate && ((TileGaiaPlate) te).maxMana != 0)
		{
			int ticks = (int) (100.0 * ((double) ((TileGaiaPlate) te).getCurrentMana() / (double) ((TileGaiaPlate) te).maxMana));
			
			int totalSpiritCount = 3;
			double tickIncrement = 360D / totalSpiritCount;
			
			int speed = 5;
			double wticks = ticks * speed - tickIncrement;
			
			double r = Math.sin((ticks - 100) / 10D) * 2;
			double g = Math.sin(wticks * Math.PI / 180 * 0.55);
			
			for(int i = 0; i < totalSpiritCount; i++)
			{
				double x = pos.getX() + Math.sin(wticks * Math.PI / 180) * r + 0.5;
				double y = pos.getY() + 0.25 + Math.abs(r) * 0.7;
				double z = pos.getZ() + Math.cos(wticks * Math.PI / 180) * r + 0.5;
				
				wticks += tickIncrement;
				float[] colorsfx = new float[] { 0F, (float) ticks / (float) 100, 1F - (float) ticks / (float) 100 };
				Botania.proxy.wispFX(x, y, z, colorsfx[0], colorsfx[1], colorsfx[2], 0.85F, (float) g * 0.05F, 0.25F);
				Botania.proxy.wispFX(x, y, z, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.1F + 0.1F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, 0.9F);
				
				if(ticks == 100)
					for(int j = 0; j < 15; j++)
						Botania.proxy.wispFX(pos.getX() + 0.5, pos.getY() + 0.5, pos.getY() + 0.5, colorsfx[0], colorsfx[1], colorsfx[2], (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F);
			}
		}
		
		return null;
	}
}