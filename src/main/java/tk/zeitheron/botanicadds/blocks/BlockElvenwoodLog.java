package tk.zeitheron.botanicadds.blocks;

import com.zeitheron.hammercore.utils.IRegisterListener;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockElvenwoodLog extends BlockLog implements IRegisterListener, ILexiconable
{
	public BlockElvenwoodLog()
	{
		setTranslationKey("elvenwood_log");
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, LOG_AXIS);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(LOG_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(LOG_AXIS, EnumAxis.values()[meta % 4]);
	}
	
	@Override
	public void onRegistered()
	{
		OreDictionary.registerOre("logElvenwood", this);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon)
	{
		return LexiconData.elvenResources;
	}
}