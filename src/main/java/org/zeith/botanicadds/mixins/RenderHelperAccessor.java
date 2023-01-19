package org.zeith.botanicadds.mixins;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vazkii.botania.client.core.helper.RenderHelper;

@Mixin(value = RenderHelper.class, remap = false)
public interface RenderHelperAccessor
{
	@Invoker("getPylonGlowDirect")
	static RenderType callGetPylonGlowDirect_BotanicAdditions(String name, ResourceLocation texture)
	{
		throw new UnsupportedOperationException();
	}
	
	@Invoker("getPylonGlow")
	static RenderType callGetPylonGlow_BotanicAdditions(String name, ResourceLocation texture)
	{
		throw new UnsupportedOperationException();
	}
}
