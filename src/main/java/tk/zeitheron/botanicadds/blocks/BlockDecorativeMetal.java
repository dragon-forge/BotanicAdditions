package tk.zeitheron.botanicadds.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDecorativeMetal
		extends Block
{
	public BlockDecorativeMetal(String key)
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setTranslationKey(key);
		setHardness(2.5F);
		setHarvestLevel("pickaxe", 1);
	}
}