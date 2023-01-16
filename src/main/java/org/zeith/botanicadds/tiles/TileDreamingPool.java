package org.zeith.botanicadds.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.botanicadds.api.tile.ICustomCapacityManaPool;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.mixins.BlockEntityAccessor;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

public class TileDreamingPool
		extends ManaPoolBlockEntity
		implements ICustomCapacityManaPool
{
	public TileDreamingPool(BlockPos pos, BlockState state)
	{
		super(pos, state);
		((BlockEntityAccessor) this).botanicAdditionsSetType(TilesBA.DREAMING_POOL);
	}
	
	@Override
	public int getMaxCustomMana()
	{
		return 2000000;
	}
	
	@Override
	public float getPoolTop()
	{
		return 10;
	}
}