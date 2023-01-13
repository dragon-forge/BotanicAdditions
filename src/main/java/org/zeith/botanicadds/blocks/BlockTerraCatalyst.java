package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import org.zeith.botanicadds.proxy.ClientProxyBA;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
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
		super(BlockBehaviour.Properties.of(Material.STONE).strength(2F, 10F));
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.STONE, this);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_)
	{
		return List.of(new ItemStack(this));
	}
	
	@Override
	public ResourceLocation getIcon(Level level, BlockPos blockPos)
	{
		return ClientProxyBA.TERRA_CATALYST_OVERLAY.texture();
	}
	
	@Override
	public CreativeModeTab getCreativeTab()
	{
		return TAB;
	}
}