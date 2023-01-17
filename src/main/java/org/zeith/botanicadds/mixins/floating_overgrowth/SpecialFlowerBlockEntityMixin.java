package org.zeith.botanicadds.mixins.floating_overgrowth;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.botanicadds.init.IslandTypesBA;
import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

@Mixin(value = SpecialFlowerBlockEntity.class, remap = false)
public abstract class SpecialFlowerBlockEntityMixin
{
	@Shadow
	public abstract boolean isFloating();
	
	@Shadow
	@Nullable
	public abstract FloatingFlower getFloatingData();
	
	@Inject(
			method = "isOnSpecialSoil",
			at = @At("HEAD"),
			cancellable = true
	)
	private void isOnSpecialSoil_BotanicAdditions(CallbackInfoReturnable<Boolean> cir)
	{
		FloatingFlower ff;
		if(isFloating() && (ff = getFloatingData()) != null && ff.getIslandType() == IslandTypesBA.OVERGROWTH)
			cir.setReturnValue(true);
	}
}