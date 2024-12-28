package org.zeith.botanicadds.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

public class SimpleBlockBA
		extends Block
{
	public SimpleBlockBA(Properties props)
	{
		super(props);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_)
	{
		return List.of(new ItemStack(this));
	}
}