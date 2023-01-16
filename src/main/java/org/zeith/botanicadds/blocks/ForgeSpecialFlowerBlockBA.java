package org.zeith.botanicadds.blocks;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.forge.block.ForgeSpecialFlowerBlock;

import java.util.List;
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
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		return new SpecialFlowerBlockItem(this, new Item.Properties().tab(TAB));
	}
}