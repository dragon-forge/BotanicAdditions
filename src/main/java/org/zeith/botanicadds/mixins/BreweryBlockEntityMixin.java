package org.zeith.botanicadds.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.botanicadds.api.tile.IElvenBrewery;
import vazkii.botania.api.recipe.BotanicalBreweryRecipe;
import vazkii.botania.common.block.block_entity.BreweryBlockEntity;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;

import java.util.Optional;

@Mixin(BreweryBlockEntity.class)
public abstract class BreweryBlockEntityMixin
		extends SimpleInventoryBlockEntity
{
	@Shadow(remap = false)
	public BotanicalBreweryRecipe recipe;
	
	protected BreweryBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	@Inject(
			remap = false,
			method = "findRecipe",
			at = @At("HEAD"),
			cancellable = true
	)
	public void findRecipe_botanicAdditions(CallbackInfo ci)
	{
		if(this instanceof IElvenBrewery)
		{
			ci.cancel();
			Optional<BotanicalBreweryRecipe> maybeRecipe = level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.BREW_TYPE, getItemHandler(), level);
			maybeRecipe.ifPresent(recipeBrew ->
			{
				this.recipe = recipeBrew;
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, true));
			});
		}
	}
}