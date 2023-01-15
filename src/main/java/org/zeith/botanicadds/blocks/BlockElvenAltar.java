package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.tiles.TileElvenAltar;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.common.block.mana.RunicAltarBlock;

import java.util.List;

public class BlockElvenAltar
		extends RunicAltarBlock
		implements ICustomBlockItem
{
	public BlockElvenAltar(Properties builder)
	{
		super(builder);
		TagAdapter.bind(BlockTags.MINEABLE_WITH_PICKAXE, this);
	}
	
	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return new TileElvenAltar(pos, state);
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		return new BlockItem(this, ItemsBA.baseProperties())
		{
			@Override
			public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags)
			{
				tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".spark_attachable")
						.withStyle(Style.EMPTY.withColor(0x666666).withItalic(true)));
			}
		};
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		if(level.isClientSide)
			return createTickerHelper(type, TilesBA.ELVEN_ALTAR, TileElvenAltar::clientTick);
		else
			return createTickerHelper(type, TilesBA.ELVEN_ALTAR, TileElvenAltar::serverTickElven);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		return List.of(new ItemStack(this));
	}
}