package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.tiles.TileElvenFluxField;
import org.zeith.hammerlib.api.forge.BlockAPI;

import java.util.List;

public class BlockElvenFluxField
		extends SimpleBlockBA
		implements EntityBlock
{
	public BlockElvenFluxField(Properties props)
	{
		super(props);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileElvenFluxField(pos, state);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flags)
	{
		tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".spark_attachable")
				.withStyle(Style.EMPTY.withColor(0x444444).withItalic(true)));
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_)
	{
		return BlockAPI.ticker();
	}
}