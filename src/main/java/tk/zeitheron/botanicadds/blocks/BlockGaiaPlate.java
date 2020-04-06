package tk.zeitheron.botanicadds.blocks;

import javax.annotation.Nonnull;

import tk.zeitheron.botanicadds.blocks.tiles.TileGaiaPlate;
import tk.zeitheron.botanicadds.init.LexiconBA;
import com.zeitheron.hammercore.api.ITileBlock;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockGaiaPlate extends Block implements ITileBlock<TileGaiaPlate>, ILexiconable
{
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 3.0 / 16, 1);
	
	public BlockGaiaPlate()
	{
		super(Material.ROCK);
		setTranslationKey("gaia_plate");
		setHardness(3F);
		setResistance(10F);
		setSoundType(SoundType.METAL);
		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
	}
	
	@Override
	public Class<TileGaiaPlate> getTileClass()
	{
		return TileGaiaPlate.class;
	}
	
	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return AABB;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty())
		{
			if(!world.isRemote)
			{
				ItemStack target = stack.splitStack(1);
				EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, target);
				item.setPickupDelay(40);
				item.motionX = item.motionY = item.motionZ = 0;
				world.spawnEntity(item);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
	{
		TileGaiaPlate plate = (TileGaiaPlate) world.getTileEntity(pos);
		int val = (int) ((double) plate.getCurrentMana() / (double) plate.maxMana * 15.0);
		if(plate.getCurrentMana() > 0)
			val = Math.max(val, 1);
		return val;
	}
	
	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon)
	{
		return LexiconBA.gaia_plate;
	}
}