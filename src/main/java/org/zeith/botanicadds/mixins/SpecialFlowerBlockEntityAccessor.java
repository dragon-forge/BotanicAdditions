package org.zeith.botanicadds.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

@Mixin(value = SpecialFlowerBlockEntity.class, remap = false)
public interface SpecialFlowerBlockEntityAccessor
{
	@Accessor("floatingData")
	FloatingFlower getFloatingData_botanicAdditions();
}
