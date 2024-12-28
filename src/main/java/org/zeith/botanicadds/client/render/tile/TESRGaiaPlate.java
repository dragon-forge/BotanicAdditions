package org.zeith.botanicadds.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import org.zeith.botanicadds.tiles.TileGaiaPlate;
import org.zeith.hammerlib.client.render.tile.IBESR;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TESRGaiaPlate
		implements IBESR<TileGaiaPlate>
{
	public TextureAtlasSprite getOverlaySprite()
	{
		return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
				.apply(prefix("block/terra_plate_overlay"));
	}
	
	@Override
	public void render(TileGaiaPlate plate, float partial, PoseStack mat, MultiBufferSource buf, int lighting, int overlay)
	{
		float alphaMod = Math.min(1.0F, plate.getCompletion() / 0.1F);
		
		mat.pushPose();
		mat.translate(0.0, 3F / 16F + 0.001F, 0.0);
		mat.mulPose(Axis.XP.rotationDegrees(90.0F));
		
		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + partial) / 8D) + 1D) / 5D + 0.6D) * alphaMod;
		
		VertexConsumer buffer = buf.getBuffer(RenderHelper.TERRA_PLATE);
		RenderHelper.renderIconFullBright(mat, buffer, getOverlaySprite(), alpha);
		
		mat.popPose();
	}
}