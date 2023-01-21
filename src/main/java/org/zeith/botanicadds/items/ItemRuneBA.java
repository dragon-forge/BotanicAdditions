package org.zeith.botanicadds.items;

import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.common.item.material.RuneItem;
import vazkii.botania.common.lib.BotaniaTags;

public class ItemRuneBA
		extends RuneItem
{
	public ItemRuneBA(Properties builder)
	{
		super(builder);
		TagAdapter.bind(BotaniaTags.Items.RUNES, this);
	}
}