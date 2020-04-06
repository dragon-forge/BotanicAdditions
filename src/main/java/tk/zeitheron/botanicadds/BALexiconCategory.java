package tk.zeitheron.botanicadds;

import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibLexicon;

public class BALexiconCategory extends LexiconCategory
{
	public BALexiconCategory(String unlocalizedName, int priority)
	{
		super(LibLexicon.CATEGORY_PREFIX + unlocalizedName);
		setIcon(new ResourceLocation(LibResources.PREFIX_CATEGORIES + unlocalizedName + ".png"));
		setPriority(priority);
	}
}