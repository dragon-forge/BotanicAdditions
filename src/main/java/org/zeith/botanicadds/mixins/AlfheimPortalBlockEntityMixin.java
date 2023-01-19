package org.zeith.botanicadds.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.botanicadds.api.tile.IElvenGatewayPylonTile;
import org.zeith.botanicadds.util.AlfheimPortalHelper;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.List;

@Mixin(value = AlfheimPortalBlockEntity.class)
public abstract class AlfheimPortalBlockEntityMixin
		extends BotaniaBlockEntity
{
	@Shadow(remap = false)
	private boolean closeNow;
	
	private DirectStorage<Boolean> closeNowDirect;
	
	public AlfheimPortalBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	/**
	 * This injection replaces the check of {@link vazkii.botania.common.block.BotaniaBlocks#naturaPylon} & {@link vazkii.botania.common.block.mana.ManaPoolBlock}
	 * with a more flexible check of pylon from {@link org.zeith.botanicadds.init.TagsBA.Blocks#ALFHEIM_GATEWAY_PYLONS} (while also allowing custom pylons via {@link IElvenGatewayPylonTile}) and any {@link net.minecraft.world.level.block.entity.BlockEntity} extending {@link ManaPoolBlockEntity}
	 */
	@Inject(
			method = "lambda$locatePylons$1",
			at = @At("HEAD"),
			cancellable = true,
			remap = false
	)
	private void locatePylonsWithAnyManaPool_BotanicAdditions(BlockPos p, CallbackInfoReturnable<Boolean> cir)
	{
		cir.setReturnValue(IElvenGatewayPylonTile.findPylon(level, p) != null && level.getBlockEntity(p.below()) instanceof ManaPoolBlockEntity);
	}
	
	/**
	 * This injection allows for custom pylons to be added with their multiplers for mana consumption.
	 */
	@Inject(
			method = "consumeMana",
			at = @At("HEAD"),
			cancellable = true,
			remap = false
	)
	public void consumeMana_BotanicAdditions(List<BlockPos> pylons, int totalCost, boolean close, CallbackInfoReturnable<Boolean> cir)
	{
		if(closeNowDirect == null)
			closeNowDirect = DirectStorage.create(v -> closeNow = v, () -> closeNow);
		cir.setReturnValue(AlfheimPortalHelper.consumeMana(Cast.cast(this), pylons, totalCost, close, closeNowDirect));
	}
}