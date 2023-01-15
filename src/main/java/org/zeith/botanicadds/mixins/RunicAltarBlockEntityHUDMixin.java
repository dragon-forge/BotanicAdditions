package org.zeith.botanicadds.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.botanicadds.tiles.TileElvenAltar;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;

@Mixin(RunicAltarBlockEntity.Hud.class)
public class RunicAltarBlockEntityHUDMixin
{
	@Inject(
			method = "render",
			cancellable = true,
			remap = false,
			at = @At("HEAD")
	)
	private static void hookElvenHUD(RunicAltarBlockEntity altar, PoseStack ms, Minecraft mc, CallbackInfo ci)
	{
		if(altar instanceof TileElvenAltar elven)
		{
			TileElvenAltar.ElvenHud.render(elven, ms, mc);
			ci.cancel();
		}
	}
}