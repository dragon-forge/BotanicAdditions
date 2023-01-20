package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.client.ISTER;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.tiles.TileElvenBrewery;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.common.block.mana.BotanicalBreweryBlock;

import java.util.List;
import java.util.function.Consumer;

public class BlockElvenBrewery
		extends BotanicalBreweryBlock
		implements ICustomBlockItem
{
	public BlockElvenBrewery(Properties builder)
	{
		super(builder);
		TagAdapter.bind(BlockTags.MINEABLE_WITH_PICKAXE, this);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flags)
	{
		tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".spark_attachable")
				.withStyle(Style.EMPTY.withColor(0x444444).withItalic(true)));
	}
	
	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return new TileElvenBrewery(pos, state);
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		return new BlockItem(this, ItemsBA.baseProperties())
		{
			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer)
			{
				ISTER.initItem(consumer);
			}
		};
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, TilesBA.ELVEN_BREWERY, TileElvenBrewery::commonTickElven);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		return List.of(new ItemStack(this));
	}
}