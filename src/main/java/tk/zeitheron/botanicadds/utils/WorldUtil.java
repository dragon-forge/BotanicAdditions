package tk.zeitheron.botanicadds.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldUtil
{
	public static boolean isSnowingAt(World world, BlockPos position)
	{
		return world.isRaining() && !world.isRainingAt(position) && world.canBlockSeeSky(position);
	}
}