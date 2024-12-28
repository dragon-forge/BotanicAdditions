package org.zeith.botanicadds.blocks.flowers;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.FloatingSpecialFlowerBlock;
import vazkii.botania.common.item.block.SpecialFlowerBlockItem;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;
import java.util.function.Supplier;

public class FloatingSpecialFlowerBlockBA
		extends FloatingSpecialFlowerBlock
		implements ICustomBlockItem, IRegisterListener
{
	public final Supplier<Block> nonFloating;
	
	public FloatingSpecialFlowerBlockBA(Properties props, Supplier<Block> nonFloating, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType)
	{
		super(props, blockEntityType);
		this.nonFloating = nonFloating;
		TagAdapter.bind(BotaniaTags.Blocks.SPECIAL_FLOATING_FLOWERS, this);
	}
	
	@Override
	public void onPostRegistered()
	{
		HammerLib.EVENT_BUS.addListener(this::addRecipes);
	}
	
	protected void addRecipes(RegisterRecipesEvent e)
	{
		e.shapeless()
				.add(BotaniaTags.Items.FLOATING_FLOWERS)
				.add(nonFloating.get())
				.result(this)
				.register();
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		var bi = new SpecialFlowerBlockItem(this, new Item.Properties());
		TagAdapter.bind(BotaniaTags.Items.SPECIAL_FLOATING_FLOWERS, bi);
		return bi;
	}
}