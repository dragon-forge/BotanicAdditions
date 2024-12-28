package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.NotNull;
import org.zeith.botanicadds.proxy.ClientProxyBA;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;
import vazkii.botania.api.mana.PoolOverlayProvider;

import java.util.List;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class BlockTerraCatalyst
		extends Block
		implements PoolOverlayProvider, ICreativeTabBlock
{
	public BlockTerraCatalyst()
	{
		super(BlockBehaviour.Properties.of().strength(2F, 10F));
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.STONE, this);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public ResourceLocation getIcon(Level level, BlockPos blockPos)
	{
		return ClientProxyBA.TERRA_CATALYST_OVERLAY.texture();
	}
	
	@Override
	public @NotNull CreativeTab getCreativeTab()
	{
		return TAB;
	}
}