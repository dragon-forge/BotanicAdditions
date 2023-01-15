package org.zeith.botanicadds.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;

@Mixin(value = RunicAltarBlockEntity.class, remap = false)
public interface RunicAltarBlockEntityAccessor
{
	@Accessor("recipeKeepTicks")
	int botanicAdditions_recipeKeepTicks();
}
