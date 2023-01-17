package org.zeith.botanicadds.blocks.flowers;

import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
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
	public final FlowerKind kind;
	
	public ForgeSpecialFlowerBlockBA(FlowerKind kind, MobEffect stewEffect, int stewDuration, Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType)
	{
		super(stewEffect, stewDuration, props, blockEntityType);
		this.kind = kind;
		TagAdapter.bind(kind.blockTag, this);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		var bi = new SpecialFlowerBlockItem(this, new Item.Properties().tab(TAB));
		TagAdapter.bind(kind.itemTag, bi);
		return bi;
	}
	
	public enum FlowerKind
	{
		FUNCTIONAL(BotaniaTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS, BotaniaTags.Items.FUNCTIONAL_SPECIAL_FLOWERS),
		GENERATING(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS, BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS),
		MISC(BotaniaTags.Blocks.MISC_SPECIAL_FLOWERS, BotaniaTags.Items.MISC_SPECIAL_FLOWERS);
		
		final TagKey<Block> blockTag;
		final TagKey<Item> itemTag;
		
		FlowerKind(TagKey<Block> blockTag, TagKey<Item> itemTag)
		{
			this.blockTag = blockTag;
			this.itemTag = itemTag;
		}
	}
}