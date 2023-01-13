package org.zeith.botanicadds.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import org.zeith.botanicadds.tiles.TileGaiaPlate;
import org.zeith.hammerlib.client.render.tile.IBESR;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.client.core.helper.RenderHelper;

public class TESRGaiaPlate
		implements IBESR<TileGaiaPlate>
{
	@Override
	public void render(TileGaiaPlate plate, float partial, PoseStack mat, MultiBufferSource buf, int lighting, int overlay)
	{
		float alphaMod = Math.min(1.0F, plate.getCompletion() / 0.1F);
		mat.pushPose();
		mat.translate(0.0, 0.18850000202655792, 0.0);
		mat.mulPose(Vector3f.XP.rotationDegrees(90.0F));
		float alpha = (float) ((Math.sin((double) ((float) ClientTickHandler.ticksInGame + partial) / 8.0) + 1.0) / 5.0 + 0.6) * alphaMod;
		VertexConsumer buffer = buf.getBuffer(RenderHelper.TERRA_PLATE);
		RenderHelper.renderIconFullBright(mat, buffer, MiscellaneousModels.INSTANCE.terraPlateOverlay.sprite(), alpha);
		mat.popPose();
	}
}