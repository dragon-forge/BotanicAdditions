package org.zeith.botanicadds.blocks;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.RotatedPillarBlock;
import org.zeith.hammerlib.core.adapter.TagAdapter;

public class RotatedPillarBlockBA
		extends RotatedPillarBlock
{
	public RotatedPillarBlockBA(Properties props)
	{
		super(props);
		TagAdapter.bind(BlockTags.MINEABLE_WITH_AXE, this);
	}
}