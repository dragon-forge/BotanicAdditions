package tk.zeitheron.botanicadds.blocks;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockDummy extends Block implements ILexiconable
{
	public BlockDummy(String name)
	{
		this(Material.ROCK, SoundType.STONE, name);
	}
	
	public BlockDummy(Material mat, String name)
	{
		this(mat, SoundType.STONE, name);
	}
	
	public BlockDummy(Material mat, SoundType type, String name)
	{
		super(mat);
		setSoundType(type);
		setTranslationKey(name);
		setHardness(2.0F);
		setResistance(10.0F);
	}
	
	Supplier<LexiconEntry> lexicon;
	
	public BlockDummy setLexiconEntry(Supplier<LexiconEntry> e)
	{
		lexicon = e;
		return this;
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon)
	{
		return this.lexicon != null ? this.lexicon.get() : null;
	}
}