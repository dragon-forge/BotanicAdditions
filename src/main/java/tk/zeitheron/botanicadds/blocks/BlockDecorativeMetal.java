package tk.zeitheron.botanicadds.blocks;

import com.zeitheron.hammercore.utils.IRegisterListener;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

public class BlockDecorativeMetal
		extends Block
		implements IRegisterListener
{
	public final String[] OD;

	public BlockDecorativeMetal(String key, String... OD)
	{
		super(Material.IRON);
		this.OD = OD;
		setSoundType(SoundType.METAL);
		setTranslationKey(key);
		setHardness(2.5F);
		setHarvestLevel("pickaxe", 1);
	}

	@Override
	public void onRegistered()
	{
		if(OD != null) for(String s : OD) OreDictionary.registerOre(s, this);
	}
}