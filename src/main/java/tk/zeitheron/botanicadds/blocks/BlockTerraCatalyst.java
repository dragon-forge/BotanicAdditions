package tk.zeitheron.botanicadds.blocks;

import tk.zeitheron.botanicadds.init.LexiconBA;
import tk.zeitheron.botanicadds.proxy.ClientProxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IPoolOverlayProvider;

public class BlockTerraCatalyst extends Block implements ILexiconable, IPoolOverlayProvider
{
	public BlockTerraCatalyst()
	{
		super(Material.ROCK);
		setTranslationKey("terra_catalyst");
		setHardness(2.0F);
		setResistance(10.0F);
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon)
	{
		return LexiconBA.terra_catalyst;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon(World world, BlockPos pos)
	{
		return ClientProxy.terraCatalystOverlay;
	}
}