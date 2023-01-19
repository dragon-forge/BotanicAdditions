package org.zeith.botanicadds.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.zeith.botanicadds.api.tile.IElvenGatewayPylonTile;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

@Mixin(PylonBlockEntity.class)
@Implements({
		@Interface(iface = IElvenGatewayPylonTile.class, prefix = "egpt$")
})
public class PylonBlockEntityMixin
{
	@Shadow
	boolean activated;
	
	@Shadow
	BlockPos centerPos;
	
	@Redirect(
			method = "commonTick",
			remap = false,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lvazkii/botania/common/block/block_entity/PylonBlockEntity;portalOff()Z", remap = false),
					to = @At(value = "INVOKE", target = "Lvazkii/botania/xplat/BotaniaConfig$ClientConfigAccess;elfPortalParticlesEnabled()Z", remap = false)
			)
	)
	private static Block commonTick_BotanicAdditions(BlockState instance)
	{
		// Trick Botania into thinking it's a mana pool even if the block class itself is not mana pool directly.
		if(instance.getBlock() instanceof EntityBlock be && be.newBlockEntity(BlockPos.ZERO, instance) instanceof ManaPoolBlockEntity manaPool)
			return BotaniaBlocks.manaPool;
		
		return instance.getBlock();
	}
	
	public float egpt$getManaCostMultiplier()
	{
		return 1F;
	}
	
	public void egpt$activate(BlockPos corePos)
	{
		activated = true;
		centerPos = corePos;
	}
	
	public void egpt$deactivate()
	{
		activated = false;
	}
}