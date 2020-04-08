package tk.zeitheron.botanicadds.items;

import com.zeitheron.hammercore.utils.IRegisterListener;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOD
		extends Item
		implements IRegisterListener
{
	public final String[] OD;

	public ItemOD(String name, String... OD)
	{
		setTranslationKey(name);
		this.OD = OD;
	}

	@Override
	public void onRegistered()
	{
		if(OD != null) for(String s : OD) OreDictionary.registerOre(s, this);
	}
}