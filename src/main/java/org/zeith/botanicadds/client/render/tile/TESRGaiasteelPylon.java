package org.zeith.botanicadds.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.blocks.BlockGaiasteelPylon;
import org.zeith.botanicadds.client.model.ModelGaiasteelPylon;
import org.zeith.botanicadds.mixins.RenderHelperAccessor;
import org.zeith.botanicadds.tiles.TileGaiasteelPylon;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.BotaniaModelLayers;
import vazkii.botania.client.render.block_entity.TEISR;

import java.util.Random;

public class TESRGaiasteelPylon
		implements BlockEntityRenderer<TileGaiasteelPylon>
{
	public static final ResourceLocation GAIASTEEL_TEXTURE = BotanicAdditions.id("textures/block/gaiasteel_pylon.png");
	public static final RenderType NATURA_PYLON_GLOW = RenderHelperAccessor.callGetPylonGlow_BotanicAdditions("gaiasteel_pylon_glow", GAIASTEEL_TEXTURE);
	public static final RenderType NATURA_PYLON_GLOW_DIRECT = RenderHelperAccessor.callGetPylonGlowDirect_BotanicAdditions("gaiasteel_pylon_glow_direct", GAIASTEEL_TEXTURE);
	
	private final ModelGaiasteelPylon model;
	
	// Overrides for when we call this without an actual pylon
	private static ItemTransforms.TransformType forceTransform = ItemTransforms.TransformType.NONE;
	
	public TESRGaiasteelPylon(BlockEntityRendererProvider.Context ctx)
	{
		model = new ModelGaiasteelPylon(ctx.bakeLayer(BotaniaModelLayers.PYLON_NATURA));
	}
	
	@Override
	public void render(@Nullable TileGaiasteelPylon pylon, float pticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay)
	{
		boolean renderingItem = pylon == null;
		boolean direct = renderingItem && (forceTransform == ItemTransforms.TransformType.GUI || forceTransform.firstPerson()); // loosely based off ItemRenderer logic
		
		var shaderLayer = direct ? NATURA_PYLON_GLOW_DIRECT : NATURA_PYLON_GLOW;
		
		ms.pushPose();
		
		float worldTime = ClientTickHandler.ticksInGame + pticks;
		
		worldTime += pylon == null ? 0 : new Random(pylon.getBlockPos().hashCode()).nextInt(360);
		
		ms.translate(0, pylon == null ? 1.35 : 1.5, 0);
		ms.scale(1.0F, -1.0F, -1.0F);
		
		ms.pushPose();
		ms.translate(0.5F, 0F, -0.5F);
		if(pylon != null)
			ms.mulPose(Vector3f.YP.rotationDegrees(worldTime * 1.5F));
		
		RenderType layer = RenderType.entityTranslucent(GAIASTEEL_TEXTURE);
		
		float r = 1F, g = 1F, b = 1F, a = 1F;
		
		VertexConsumer buffer = buffers.getBuffer(layer);
		model.renderRing(ms, buffer, light, overlay, r, g, b, a);
		if(pylon != null)
			ms.translate(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
		ms.popPose();
		
		ms.pushPose();
		if(pylon != null)
			ms.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);
		
		ms.translate(0.5F, 0F, -0.5F);
		if(pylon != null)
			ms.mulPose(Vector3f.YP.rotationDegrees(-worldTime));
		
		buffer = buffers.getBuffer(shaderLayer);
		model.renderCrystal(ms, buffer, light, overlay, r, g, b, a);
		
		ms.popPose();
		
		ms.popPose();
	}
	
	public static class ItemRenderer
			extends TEISR
	{
		public ItemRenderer(Block block)
		{
			super(block);
		}
		
		@Override
		public void render(ItemStack stack, ItemTransforms.TransformType type, PoseStack ms, MultiBufferSource buffers, int light, int overlay)
		{
			if(Block.byItem(stack.getItem()) instanceof BlockGaiasteelPylon pylon)
			{
				forceTransform = type;
				super.render(stack, type, ms, buffers, light, overlay);
			}
		}
	}
}