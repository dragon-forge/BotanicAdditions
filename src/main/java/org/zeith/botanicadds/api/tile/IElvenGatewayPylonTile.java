package org.zeith.botanicadds.api.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.zeith.botanicadds.init.TagsBA;

public interface IElvenGatewayPylonTile
{
	IElvenGatewayPylonTile DUMMY = new IElvenGatewayPylonTile()
	{
		@Override
		public float getManaCostMultiplier()
		{
			return 1F;
		}
		
		@Override
		public void activate(BlockPos corePos)
		{
		}
		
		@Override
		public void deactivate()
		{
		}
	};
	
	float getManaCostMultiplier();
	
	void activate(BlockPos corePos);
	
	void deactivate();
	
	static IElvenGatewayPylonTile findPylon(Level level, BlockPos pos)
	{
		return level.getBlockEntity(pos) instanceof IElvenGatewayPylonTile pylon ? pylon :
				(level.getBlockState(pos).is(TagsBA.Blocks.ALFHEIM_GATEWAY_PYLONS) ? DUMMY : null);
	}
}