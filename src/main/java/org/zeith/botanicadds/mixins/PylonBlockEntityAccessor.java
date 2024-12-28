package org.zeith.botanicadds.mixins;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;

@Mixin(value = PylonBlockEntity.class, remap = false)
public interface PylonBlockEntityAccessor
{
	@Accessor("activated")
	void botanicAdditions_activated(boolean activated);
	
	@Accessor("centerPos")
	void botanicAdditions_centerPos(BlockPos pos);
}
