package tk.zeitheron.botanicadds.flowers;

import tk.zeitheron.botanicadds.flowers.base.Flower;
import tk.zeitheron.botanicadds.init.LexiconBA;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketSyncAnyTile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;

@Flower("wither_flower")
public class WitherFlower extends SubTileFunctional
{
	private static final int RANGE = 3;
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if(mana >= 100 && ticksExisted % 10 == 0 && !getWorld().isRemote && redstoneSignal <= 0)
		{
			int slowdown = getSlowdownFactor();
			
			for(EntityItem item : supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1))))
			{
				if(item.age >= 19 + slowdown && !item.isDead)
				{
					ItemStack stack = item.getItem();
					if(stack.isEmpty())
						continue;
					
					Item ssi = Item.getItemFromBlock(Blocks.SOUL_SAND);
					
					if(stack.getItem() == ssi || (stack.getItem() == Items.SKULL && stack.getItemDamage() == 1))
					{
						BlockPos starting = getPos();
						IBlockState cs;
						int steps = 0;
						while((cs = getWorld().getBlockState(starting)).getBlock() != Blocks.AIR)
						{
							if(cs.getBlock() == Blocks.SOUL_SAND)
								break;
							starting = starting.up();
							++steps;
							if(steps > 5)
								return;
						}
						
						BlockPos soulSandPos = null;
						BlockPos skullPos = null;
						
						for(int i = 0; i < 2; ++i)
						{
							BlockPos cp = starting.up(i);
							if(getWorld().getBlockState(cp).getBlock() != Blocks.SOUL_SAND)
							{
								soulSandPos = cp;
								break;
							} else if(i == 1)
							{
								BlockPos cp2 = starting.up(2);
								TileEntity tile = getWorld().getTileEntity(cp2);
								if(!(tile instanceof TileEntitySkull && ((TileEntitySkull) tile).getSkullType() == 1))
									skullPos = cp2;
							}
						}
						
						boolean xValid = true;
						boolean zValid = true;
						
						if(soulSandPos == null)
						{
							for(int i = 0; i < 3; i += 2)
							{
								BlockPos cp = starting.add(i - 1, 1, 0);
								Block bl = getWorld().getBlockState(cp).getBlock();
								if(bl != Blocks.SOUL_SAND && bl != Blocks.AIR)
								{
									xValid = false;
									break;
								}
							}
							
							for(int i = 0; i < 3; i += 2)
							{
								BlockPos cp = starting.add(0, 1, i - 1);
								Block bl = getWorld().getBlockState(cp).getBlock();
								if(bl != Blocks.SOUL_SAND && bl != Blocks.AIR)
								{
									zValid = false;
									break;
								}
							}
							
							if(xValid)
								for(int i = 0; i < 3; i += 2)
								{
									BlockPos cp = starting.add(i - 1, 1, 0);
									Block bl = getWorld().getBlockState(cp).getBlock();
									if(bl != Blocks.SOUL_SAND)
									{
										soulSandPos = cp;
										break;
									} else
									{
										BlockPos cp2 = starting.add(i - 1, 2, 0);
										TileEntity tile = getWorld().getTileEntity(cp2);
										if(!(tile instanceof TileEntitySkull && ((TileEntitySkull) tile).getSkullType() == 1))
											skullPos = cp2;
									}
								}
							else if(zValid)
								for(int i = 0; i < 3; i += 2)
								{
									BlockPos cp = starting.add(0, 1, i - 1);
									Block bl = getWorld().getBlockState(cp).getBlock();
									if(bl != Blocks.SOUL_SAND)
									{
										soulSandPos = cp;
										break;
									} else
									{
										BlockPos cp2 = starting.add(0, 2, i - 1);
										TileEntity tile = getWorld().getTileEntity(cp2);
										if(!(tile instanceof TileEntitySkull && ((TileEntitySkull) tile).getSkullType() == 1))
											skullPos = cp2;
									}
								}
						}
						
						if(stack.getItem() == ssi && soulSandPos != null)
						{
							stack.shrink(1);
							getWorld().setBlockState(soulSandPos, Blocks.SOUL_SAND.getDefaultState());
							mana -= 100;
							HCNet.INSTANCE.sendToAllAround(new PacketSyncAnyTile(supertile), new TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 256));
						} else if(stack.getItem() != ssi && skullPos != null && (xValid || zValid))
						{
							getWorld().setBlockState(skullPos, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
							TileEntitySkull te = new TileEntitySkull();
							te.setType(1);
							getWorld().setTileEntity(skullPos, te);
							stack.shrink(1);
							mana -= 100;
							HCNet.INSTANCE.sendToAllAround(new PacketSyncAnyTile(supertile), new TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 256));
							
							Blocks.SKULL.checkWitherSpawn(getWorld(), skullPos, te);
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean acceptsRedstone()
	{
		return true;
	}
	
	@Override
	public int getMaxMana()
	{
		return 700;
	}
	
	@Override
	public int getColor()
	{
		return 0x1B1B1B;
	}
	
	@Override
	public LexiconEntry getEntry()
	{
		return LexiconBA.wither_flower;
	}
}