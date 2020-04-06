package tk.zeitheron.botanicadds.blocks;

import java.util.List;

import tk.zeitheron.botanicadds.blocks.tiles.TileManaTesseract;
import tk.zeitheron.botanicadds.init.LexiconBA;
import tk.zeitheron.botanicadds.net.PacketSendLKMana;
import com.zeitheron.hammercore.internal.blocks.base.BlockTileHC;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.core.handler.ModSounds;

public class BlockManaTesseract extends BlockTileHC<TileManaTesseract> implements IWandHUD, IWandable, ILexiconable
{
	public BlockManaTesseract()
	{
		super(Material.ROCK, TileManaTesseract.class, "mana_tesseract");
		setHardness(2.0F);
		setResistance(10.0F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(stack.hasTagCompound())
			tooltip.add("Channel: " + Integer.toString(stack.getTagCompound().getInteger("Channel"), Character.MAX_RADIX));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileManaTesseract tess = WorldUtil.cast(worldIn.getTileEntity(pos), TileManaTesseract.class);
		if(tess == null)
			worldIn.setTileEntity(pos, tess = new TileManaTesseract());
		tess.setOwner(placer.getUniqueID());
		tess.setLink((!stack.hasTagCompound() ? new NBTTagCompound() : stack.getTagCompound()).getInteger("Channel"));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos)
	{
		TileManaTesseract t = WorldUtil.cast(world.getTileEntity(pos), TileManaTesseract.class);
		if(t != null)
			t.renderHUD(mc, res);
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side)
	{
		TileManaTesseract t = WorldUtil.cast(world.getTileEntity(pos), TileManaTesseract.class);
		if(t != null && player instanceof EntityPlayerMP && player.isServerWorld())
			HCNet.INSTANCE.sendTo(new PacketSendLKMana(t), (EntityPlayerMP) player);
		world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 0.11F, 1F);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon)
	{
		return LexiconBA.mana_tesseract;
	}
}