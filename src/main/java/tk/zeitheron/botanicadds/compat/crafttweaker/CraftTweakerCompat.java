package tk.zeitheron.botanicadds.compat.crafttweaker;

import net.minecraftforge.fml.common.Loader;
import tk.zeitheron.botanicadds.compat.crafttweaker.core.ICTCompat;

public class CraftTweakerCompat
		implements ICTCompat
{
	private static ICTCompat ct = null;

	public static ICTCompat compat()
	{
		if(ct == null)
			if(Loader.isModLoaded("crafttweaker"))
				ct = new CTCompatImpl();
			else
				ct = new CraftTweakerCompat();
		return ct;
	}

	@Override
	public void onLoadComplete()
	{
	}

	@Override
	public void init()
	{
	}

	@Override
	public void addLateAction(Object action)
	{
	}
}