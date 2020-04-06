package tk.zeitheron.botanicadds.flowers.base;

import tk.zeitheron.botanicadds.net.PacketFXLine;
import com.zeitheron.hammercore.net.HCNet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.ModBlocks;

public class SubTilePassiveGen extends SubTileGenerating
{
	@Override
	public void onUpdate()
	{
		if(preventDecay())
			passiveDecayTicks = 0;
		
		generateMana();
		
		if(voidShareMana())
			for(EnumFacing dir : EnumFacing.HORIZONTALS)
			{
				BlockPos tpos = getPos().offset(dir);
				TileEntity tile = getWorld().getTileEntity(tpos);
				if(tile instanceof ISubTileContainer)
				{
					boolean rain = getClass().isAssignableFrom(((ISubTileContainer) tile).getSubTile().getClass());
					if(rain && mana > 0)
					{
						mana = Math.max(0, mana - 5);
						
						if(!supertile.getWorld().isRemote && Math.random() <= .05F)
						{
							AxisAlignedBB aabb = getWorld().getBlockState(getPos()).getBoundingBox(getWorld(), getPos());
							AxisAlignedBB aabb2 = getWorld().getBlockState(tpos).getBoundingBox(getWorld(), tpos);
							
							Vec3d start = getCenter(aabb);
							Vec3d end = getCenter(aabb2);
							
							start = start.add(0, -.48, 0).add(getPos().getX(), getPos().getY(), getPos().getZ());
							end = end.add(0, -.48, 0).add(tpos.getX(), tpos.getY(), tpos.getZ());
							
							HCNet.INSTANCE.sendToAllAround(new PacketFXLine(start, end, 20, getColor()), new TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 100));
						}
					}
				}
			}
		
		super.onUpdate();
	}
	
	public static Vec3d getCenter(AxisAlignedBB aabb)
    {
        return new Vec3d(aabb.minX + (aabb.maxX - aabb.minX) * 0.5D, aabb.minY + (aabb.maxY - aabb.minY) * 0.5D, aabb.minZ + (aabb.maxZ - aabb.minZ) * 0.5D);
    }
	
	public void generateMana()
	{
	}
	
	public boolean preventDecay()
	{
		return true;
	}
	
	public boolean voidShareMana()
	{
		return true;
	}
	
	@Override
	public boolean isPassiveFlower()
	{
		return true;
	}
	
	@Override
	public boolean canGeneratePassively()
	{
		return false;
	}
	
	public boolean isOnSpecialSoil()
	{
		return getWorld().getBlockState(getPos().down()).getBlock() == ModBlocks.enchantedSoil;
	}
}