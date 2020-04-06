package tk.zeitheron.botanicadds.blocks;

import javax.annotation.Nonnull;

import tk.zeitheron.botanicadds.blocks.tiles.TileElvenAltar;
import tk.zeitheron.botanicadds.init.LexiconBA;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;

public class BlockElvenAltar extends Block implements ITileEntityProvider, IWandable, ILexiconable, IWandHUD
{
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.75, 1);
	
	public BlockElvenAltar()
	{
		super(Material.ROCK);
		setTranslationKey("elven_altar");
		setHardness(2.0F);
		setResistance(10.0F);
		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param)
	{
		System.out.println(id + " -> " + param);
		
		super.eventReceived(state, world, pos, id, param);
		TileEntity tileentity = world.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}
	
	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return AABB;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float par7, float par8, float par9)
	{
		if(world.isRemote)
			return true;
		
		TileElvenAltar altar = (TileElvenAltar) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		
		if(player.isSneaking())
		{
			if(altar.manaToGet == 0)
			{
				InventoryHelper.withdrawFromInventory(altar, player);
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
				return true;
			}
		} else if(altar.isEmpty() && stack.isEmpty())
		{
			altar.trySetLastRecipe(player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			return true;
		} else if(!stack.isEmpty())
		{
			boolean result = altar.addItem(player, stack, hand);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			return result;
		}
		
		return false;
	}
	
	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state)
	{
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);
		
		InventoryHelper.dropInventory(inv, world, state, pos);
		
		super.breakBlock(world, pos, state);
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
		TileElvenAltar altar = (TileElvenAltar) world.getTileEntity(pos);
		return altar.signal;
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side)
	{
		((TileElvenAltar) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon)
	{
		return LexiconBA.elven_altar;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileElvenAltar();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileElvenAltar();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos)
	{
		((TileElvenAltar) world.getTileEntity(pos)).renderHUD(mc, res);
	}
}