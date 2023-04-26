package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.init.RecipeTypesBA;
import org.zeith.botanicadds.tiles.TileGaiaPlate;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import vazkii.botania.common.lib.BotaniaTags;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class BlockGaiaPlate
		extends SimpleBlockBA
		implements EntityBlock, ICreativeTabBlock
{
	private static final VoxelShape AABB = box(0, 0, 0, 16, 3, 16);
	
	public BlockGaiaPlate()
	{
		super(Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3F, 10F));
		TagAdapter.bind(BotaniaTags.Blocks.MAGNET_RING_BLACKLIST, this);
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.STONE, this);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(!stack.isEmpty() && usesItem(stack, world))
		{
			if(!world.isClientSide)
			{
				ItemStack target = stack.split(1);
				ItemEntity item = new ItemEntity(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, target);
				item.setPickUpDelay(40);
				item.setDeltaMovement(Vec3.ZERO);
				world.addFreshEntity(item);
			}
			return InteractionResult.sidedSuccess(world.isClientSide());
		} else
		{
			return InteractionResult.PASS;
		}
	}
	
	private static boolean usesItem(ItemStack stack, Level world)
	{
		for(Recipe<?> value : world.getRecipeManager().byType(RecipeTypesBA.GAIA_PLATE).values())
			for(Ingredient i : value.getIngredients())
				if(i.test(stack))
					return true;
		
		return false;
	}
	
	@Override
	public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, CollisionContext context)
	{
		return AABB;
	}
	
	@Override
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, PathComputationType type)
	{
		return false;
	}
	
	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
	{
		return new TileGaiaPlate(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return BlockAPI.ticker();
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos)
	{
		if(world.getBlockEntity(pos) instanceof TileGaiaPlate plate) return plate.getComparatorLevel();
		return 0;
	}
	
	@Override
	public CreativeModeTab getCreativeTab()
	{
		return TAB;
	}
}