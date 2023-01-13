package org.zeith.botanicadds.blocks;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.RotatedPillarBlock;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.core.adapter.TagAdapter;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class RotatedPillarBlockBA
		extends RotatedPillarBlock
		implements ICreativeTabBlock
{
	public RotatedPillarBlockBA(Properties props)
	{
		super(props);
		TagAdapter.bind(BlockTags.MINEABLE_WITH_AXE, this);
	}
	
	@Override
	public CreativeModeTab getCreativeTab()
	{
		return TAB;
	}
}