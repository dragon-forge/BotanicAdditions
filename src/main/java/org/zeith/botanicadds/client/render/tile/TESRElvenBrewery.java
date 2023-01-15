package org.zeith.botanicadds.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.client.model.ModelElvenBrewery;
import org.zeith.botanicadds.tiles.TileElvenBrewery;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.BotaniaModelLayers;
import vazkii.botania.client.model.BotanicalBreweryModel;

public class TESRElvenBrewery
		implements BlockEntityRenderer<TileElvenBrewery>
{
	final BotanicalBreweryModel model;
	
	public TESRElvenBrewery(BlockEntityRendererProvider.Context ctx)
	{
		model = new ModelElvenBrewery(ctx.bakeLayer(BotaniaModelLayers.BREWERY));
	}
	
	@Override
	public void render(@Nullable TileElvenBrewery brewery, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay)
	{
		ms.pushPose();
		
		ms.scale(1F, -1F, -1F);
		ms.translate(0.5F, -1.5F, -0.5F);
		
		double time = ClientTickHandler.ticksInGame + f;
		
		model.render(brewery, time, ms, buffers, light, overlay);
		ms.popPose();
	}
	
}
