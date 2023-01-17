package org.zeith.botanicadds.mixins.floating_overgrowth;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.zeith.botanicadds.init.IslandTypesBA;
import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.common.item.FloatingFlowerVariant;
import vazkii.botania.common.item.OvergrowthSeedItem;

@Mixin(value = OvergrowthSeedItem.class, remap = false)
@Implements({
		@Interface(iface = FloatingFlowerVariant.class, prefix = "ba_ffv$")
})
public class OvergrowthSeedItemMixin
{
	public FloatingFlower.IslandType ba_ffv$getIslandType(ItemStack stack)
	{
		return IslandTypesBA.OVERGROWTH;
	}
}