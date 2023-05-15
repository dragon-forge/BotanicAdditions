package org.zeith.botanicadds.mixins;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;

@Mixin(value = RecipeBuilder.class, remap = false)
public interface RecipeBuilderAccessor
{
	@Invoker
	ResourceLocation callGetIdentifier();
}
