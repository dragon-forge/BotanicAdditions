package org.zeith.botanicadds.blocks;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import org.zeith.botanicadds.util.MaterialType;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;
import org.zeith.hammerlib.core.adapter.TagAdapter;

import java.util.List;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class BlockDecorativeMetal
		extends Block
		implements ICustomBlockItem
{
	private final List<String> metals;
	
	public BlockDecorativeMetal(String... metals)
	{
		this(Properties.of(Material.METAL), metals);
	}
	
	public BlockDecorativeMetal(Properties props, String... metals)
	{
		super(props.sound(SoundType.METAL).strength(2.5F));
		this.metals = List.of(metals);
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, this);
		for(var metal : metals)
			TagAdapter.bind(BlockTags.create(MaterialType.STORAGE_BLOCK.createId(metal)), this);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		var bi = new BlockItem(this, new Item.Properties().tab(TAB));
		for(var metal : metals)
			TagAdapter.bind(ItemTags.create(MaterialType.STORAGE_BLOCK.createId(metal)), bi);
		return bi;
	}
}