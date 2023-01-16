package org.zeith.botanicadds.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.tile.ICustomCapacityManaPool;
import vazkii.botania.api.mana.PoolOverlayProvider;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

public class TESRCustomManaPool
		implements BlockEntityRenderer<ManaPoolBlockEntity>
{
	private final BlockRenderDispatcher blockRenderDispatcher;
	
	public TESRCustomManaPool(BlockEntityRendererProvider.Context ctx)
	{
		this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
	}
	
	@Override
	public void render(@Nullable ManaPoolBlockEntity pool, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay)
	{
		if(!(pool instanceof ICustomCapacityManaPool cp)) return;
		
		ms.pushPose();
		
		int insideUVStart = cp.getPoolInset();
		int insideUVEnd = 16 - insideUVStart;
		
		float poolBottom = cp.getPoolBottom();
		float poolTop = (cp.getPoolTop() - 1) / 16F;
		
		Block below = pool.getLevel().getBlockState(pool.getBlockPos().below()).getBlock();
		if(below instanceof PoolOverlayProvider overlayProvider)
		{
			var overlaySpriteId = overlayProvider.getIcon(pool.getLevel(), pool.getBlockPos());
			var overlayIcon = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(overlaySpriteId);
			ms.pushPose();
			
			float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 20.0) + 1) * 0.3 + 0.2);
			
			ms.translate(0, poolBottom, 0);
			ms.mulPose(Vector3f.XP.rotationDegrees(90F));
			
			VertexConsumer buffer = buffers.getBuffer(RenderHelper.ICON_OVERLAY);
			RenderHelper.renderIconCropped(
					ms, buffer,
					insideUVStart, insideUVStart, insideUVEnd, insideUVEnd,
					overlayIcon, 0xFFFFFF, alpha, light
			);
			
			ms.popPose();
		}
		
		int mana = pool.getCurrentMana();
		int maxMana = pool.getMaxMana();
		if(maxMana == -1) maxMana = cp.getMaxCustomMana();
		
		float manaLevel = (float) mana / (float) maxMana;
		if(manaLevel > 0)
		{
			ms.pushPose();
			ms.translate(0, Mth.clampedMap(manaLevel, 0, 1, poolBottom, poolTop), 0);
			ms.mulPose(Vector3f.XP.rotationDegrees(90F));
			
			VertexConsumer buffer = buffers.getBuffer(RenderHelper.MANA_POOL_WATER);
			RenderHelper.renderIconCropped(
					ms, buffer,
					insideUVStart, insideUVStart, insideUVEnd, insideUVEnd,
					MiscellaneousModels.INSTANCE.manaWater.sprite(), 0xFFFFFF, 1, light);
			
			ms.popPose();
		}
		ms.popPose();
	}
}