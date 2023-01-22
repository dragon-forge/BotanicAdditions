package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.client.ISTER;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.tiles.TileGaiasteelPylon;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;

import java.util.List;
import java.util.function.Consumer;

public class BlockGaiasteelPylon
		extends BotaniaWaterloggedBlock
		implements EntityBlock, ICustomBlockItem
{
	private static final VoxelShape SHAPE = box(2, 0, 2, 14, 21, 14);
	
	public BlockGaiasteelPylon(Properties builder)
	{
		super(builder);
		TagAdapter.bind(BlockTags.MINEABLE_WITH_PICKAXE, this);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
	{
		return SHAPE;
	}
	
	@NotNull
	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
	
	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return new TileGaiasteelPylon(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, TilesBA.GAIASTEEL_PYLON, TileGaiasteelPylon::commonTick);
	}
	
	@Override
	public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos)
	{
		return 30F;
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		var bi = new BlockItem(this, ItemsBA.baseProperties().rarity(ItemsBA.GAIASTEEL_RARITY))
		{
			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer)
			{
				ISTER.initItem(consumer);
			}
		};
		return bi;
	}
}
