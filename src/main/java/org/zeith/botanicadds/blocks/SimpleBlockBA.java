package org.zeith.botanicadds.blocks;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;

import java.util.List;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class SimpleBlockBA
		extends Block
		implements ICreativeTabBlock
{
	public SimpleBlockBA(Properties props)
	{
		super(props);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public CreativeModeTab getCreativeTab()
	{
		return TAB;
	}
}