package org.zeith.botanicadds.blocks;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.forge.block.ForgeSpecialFlowerBlock;

import java.util.function.Supplier;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class ForgeSpecialFlowerBlockBA
		extends ForgeSpecialFlowerBlock
		implements ICustomBlockItem
{
	public ForgeSpecialFlowerBlockBA(MobEffect stewEffect, int stewDuration, Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType)
	{
		super(stewEffect, stewDuration, props, blockEntityType);
		TagAdapter.bind(BotaniaTags.Blocks.SPECIAL_FLOWERS, this);
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		return new SpecialFlowerBlockItem(this, new Item.Properties().tab(TAB));
	}
}