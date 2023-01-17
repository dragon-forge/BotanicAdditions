package org.zeith.botanicadds.blocks.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.init.IslandTypesBA;
import org.zeith.botanicadds.mixins.SpecialFlowerBlockEntityAccessor;
import org.zeith.botanicadds.tiles.flowers.Vibrantia;
import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.api.block.FloatingFlowerProvider;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.internal.VanillaPacketDispatcher;

import java.util.function.Supplier;

public class FloatingVibrantiaBlock
		extends FloatingSpecialFlowerBlockBA
{
	public FloatingVibrantiaBlock(Properties props, Supplier<Block> nonFloating, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType)
	{
		super(props, nonFloating, blockEntityType);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getItemInHand(hand);
		BlockEntity te = world.getBlockEntity(pos);
		if(!stack.isEmpty() && te instanceof FloatingFlowerProvider provider && provider.getFloatingData() != null)
		{
			FloatingFlower flower = provider.getFloatingData();
			FloatingFlower.IslandType type = null;
			if(stack.is(Items.SCULK)) type = IslandTypesBA.SCULK;
			
			if(type != null && type != flower.getIslandType())
			{
				if(!world.isClientSide)
				{
					flower.setIslandType(type);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(te);
				}
				
				if(!player.getAbilities().instabuild) stack.shrink(1);
				
				return InteractionResult.sidedSuccess(world.isClientSide());
			}
		}
		
		return super.use(state, world, pos, player, hand, hit);
	}
	
	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		var be = super.newBlockEntity(pos, state);
		((SpecialFlowerBlockEntityAccessor) be).getFloatingData_botanicAdditions().setIslandType(IslandTypesBA.SCULK);
		return be;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(SculkSensorBlock.PHASE);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource rng)
	{
		if(SculkSensorBlock.getPhase(state) != SculkSensorPhase.ACTIVE)
		{
			if(SculkSensorBlock.getPhase(state) == SculkSensorPhase.COOLDOWN)
				lvl.setBlock(pos, state.setValue(SculkSensorBlock.PHASE, SculkSensorPhase.INACTIVE), 3);
		} else
			VibrantiaBlock.deactivate(lvl, pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T be)
	{
		return be instanceof Vibrantia vib ? vib.getListener() : null;
	}
}