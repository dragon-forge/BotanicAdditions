package org.zeith.botanicadds.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.botanicadds.api.tile.ICustomCapacityManaPool;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

@Mixin(ManaPoolBlockEntity.class)
public class ManaPoolBlockEntityMixin
{
	@Shadow
	private int manaCap;
	
	@Shadow
	private int mana;
	
	@Inject(
			method = "initManaCapAndNetwork",
			at = @At("HEAD"),
			remap = false
	)
	private void initManaCapAndNetwork_botanicAdditions(CallbackInfo ci)
	{
		if(this instanceof ICustomCapacityManaPool ccmp)
		{
			manaCap = ccmp.getMaxCustomMana();
		}
	}
	
	@Inject(
			method = "getCurrentMana",
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	private void getCurrentMana_botanicAdditions(CallbackInfoReturnable<Integer> cir)
	{
		if(this instanceof ICustomCapacityManaPool)
		{
			cir.setReturnValue(mana);
		}
	}
}