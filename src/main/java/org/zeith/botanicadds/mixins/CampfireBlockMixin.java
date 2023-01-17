package org.zeith.botanicadds.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.botanicadds.tiles.flowers.Apicaria;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(
			method = "isSmokeyPos",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void isSmokeyPos_BotanicAdditions(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir)
	{
		if(Apicaria.findActiveApicariaAndUseMana(level, pos))
			cir.setReturnValue(true);
	}
}