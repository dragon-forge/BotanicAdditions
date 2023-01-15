package org.zeith.botanicadds.blocks;

import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.botanicadds.util.MaterialType;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;
import org.zeith.hammerlib.core.adapter.TagAdapter;

import java.util.List;
import java.util.function.UnaryOperator;

public class BlockStorage
		extends Block
		implements ICustomBlockItem
{
	private final String metal;
	
	public final TagKey<Block> blockTag;
	public final TagKey<Item> itemTag;
	
	public UnaryOperator<Item.Properties> itemProps = UnaryOperator.identity();
	
	public BlockStorage(String metal)
	{
		this(Properties.of(Material.METAL).sound(SoundType.METAL), metal);
	}
	
	public BlockStorage(Properties props, String metal)
	{
		super(props.strength(2.5F));
		this.metal = metal;
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, this);
		
		itemTag = ItemTags.create(MaterialType.STORAGE_BLOCK.createId(metal));
		TagAdapter.bind(blockTag = BlockTags.create(MaterialType.STORAGE_BLOCK.createId(metal)), this);
	}
	
	public BlockStorage withItemProps(UnaryOperator<Item.Properties> itemProps)
	{
		this.itemProps = itemProps;
		return this;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		var bi = new BlockItem(this, itemProps.apply(ItemsBA.baseProperties()));
		TagAdapter.bind(itemTag, bi);
		return bi;
	}
}