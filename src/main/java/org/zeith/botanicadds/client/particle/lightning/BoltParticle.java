package org.zeith.botanicadds.client.particle.lightning;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.hammerlib.util.colors.ColorHelper;

public class BoltParticle
		extends Particle
{
	protected BoltCore main;
	protected Bolt settings;
	
	private final RenderType renderTypeSmall = RenderType.entityTranslucent(BotanicAdditions.id("textures/particle/p_small.png"));
	private final RenderType renderTypeLarge = RenderType.entityTranslucent(BotanicAdditions.id("textures/particle/p_large.png"));
	
	protected BoltParticle(ClientLevel level, Vec3 start, Vec3 end, Bolt settings)
	{
		super(level, start.x, start.y, start.z);
		this.main = new BoltCore(start.x, start.y, start.z, end.x, end.y, end.z, settings.seed(), settings.age(), settings.angleMult(), settings.speed());
		this.settings = settings;
		
		settings.fractal().apply(main);
		main.finalizeBolt();
		setupFromMain();
		
		setBoundingBox(main.getBoundingBox());
	}
	
	public void finalizeBolt()
	{
		main.finalizeBolt();
		setupFromMain();
		Minecraft.getInstance().particleEngine
				.add(this);
	}
	
	private void setupFromMain()
	{
		lifetime = main.particleMaxAge;
		setPos(main.start.x, main.start.y, main.start.z);
	}
	
	@Override
	public void tick()
	{
		main.onUpdate();
		if(main.particleAge >= main.particleMaxAge)
			remove();
	}
	
	@Override
	public void render(VertexConsumer b, Camera cam, float partialframe)
	{
		var tessellator = Minecraft.getInstance().renderBuffers().bufferSource();
		
		var aura = settings.outer();
		var core = settings.inner();
		
		RenderSystem.depthMask(false);
		
		if(aura.active())
		{
			RenderSystem.blendFunc(770, aura.blendFunc());
			int rgb = aura.color();
			rCol = ColorHelper.getRed(rgb);
			gCol = ColorHelper.getGreen(rgb);
			bCol = ColorHelper.getBlue(rgb);
			
			VertexConsumer vertexconsumer = tessellator.getBuffer(this.renderTypeLarge);
			renderBolt(vertexconsumer, cam, partialframe, 0);
			tessellator.endBatch();
		}
		
		if(core.active())
		{
			RenderSystem.blendFunc(770, core.blendFunc());
			
			int rgb = core.color();
			rCol = ColorHelper.getRed(rgb);
			gCol = ColorHelper.getGreen(rgb);
			bCol = ColorHelper.getBlue(rgb);
			
			VertexConsumer vertexconsumer = tessellator.getBuffer(this.renderTypeSmall);
			renderBolt(vertexconsumer, cam, partialframe, 1);
			tessellator.endBatch();
		}
		
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(true);
	}
	
	public void renderBolt(VertexConsumer b, Camera cam, float partialframe, int pass)
	{
		Vec3 cp = cam.getPosition();
		float boltage = main.particleAge >= 0 ? main.particleAge / (float) main.particleMaxAge : 0.0f;
		
		float mainalpha = 1;
		
		if(pass == 0)
			mainalpha = (mainalpha - boltage) * .4F;
		else
			mainalpha = mainalpha - boltage * .5F;
		
		int renderlength = (int) ((main.particleAge + partialframe + (int) (main.length * 3.0f)) / (int) (main.length * 3.0f) * main.numsegments0);
		
		if(renderlength >= main.numsegments0)
		{
			float progress = 1 - (main.particleAge + partialframe) / (float) main.particleMaxAge;
			progress *= .75F;
			mainalpha *= progress;
		}
		
		var interpPosX = cp.x;
		var interpPosY = cp.y;
		var interpPosZ = cp.z;
		
		int fullBright = 0xF000F0;
		
		ThVector3 cptv = getActiveViewVector(cam);
		
		for(var seg : main.segments)
			if(seg.segmentno <= renderlength)
			{
				float width = 0.03f * (getRelativeViewVector(seg.startpoint.point, cam).length() / 5F + 1F) * (1F + seg.light) * 0.5F;
				
				final ThVector3 diff1 = ThVector3.crossProduct(cptv, seg.prevdiff).scale(width / seg.sinprev);
				final ThVector3 diff2 = ThVector3.crossProduct(cptv, seg.nextdiff).scale(width / seg.sinnext);
				var startvec = seg.startpoint.point;
				var endvec = seg.endpoint.point;
				
				float rx1 = (float) (startvec.x - interpPosX);
				float ry1 = (float) (startvec.y - interpPosY);
				float rz1 = (float) (startvec.z - interpPosZ);
				float rx2 = (float) (endvec.x - interpPosX);
				float ry2 = (float) (endvec.y - interpPosY);
				float rz2 = (float) (endvec.z - interpPosZ);
				
				vertex(b, null,
						rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z,
						rCol, gCol, bCol, mainalpha * seg.light,
						0.5F, 0,
						OverlayTexture.NO_OVERLAY,
						fullBright,
						0, 1, 0
				);
				
				vertex(b, null,
						rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z,
						rCol, gCol, bCol, mainalpha * seg.light,
						0.5F, 0,
						OverlayTexture.NO_OVERLAY,
						fullBright,
						0, 1, 0
				);
				
				vertex(b, null,
						rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z,
						rCol, gCol, bCol, mainalpha * seg.light,
						0.5F, 1,
						OverlayTexture.NO_OVERLAY,
						fullBright,
						0, 1, 0
				);
				
				vertex(b, null,
						rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z,
						rCol, gCol, bCol, mainalpha * seg.light,
						0.5F, 1,
						OverlayTexture.NO_OVERLAY,
						fullBright,
						0, 1, 0
				);
			} else
				break;
	}
	
	static ThVector3 getActiveViewVector(Camera cam)
	{
		var lv = cam.getLookVector();
		return new ThVector3(lv.x(), lv.y(), lv.z());
	}
	
	static void vertex(VertexConsumer vc, PoseStack.Pose pose,
					   double x, double y, double z,
					   float r, float g, float b, float a,
					   float u, float v,
					   int overlay,
					   int uv2,
					   float nx, float ny, float nz
	)
	{
		vc.vertex((float) x, (float) y, (float) z);
		vc.color(r, g, b, a);
		vc.uv(u, v);
		vc.overlayCoords(overlay);
		vc.uv2(uv2);
		vc.normal(nx, ny, nz);
		vc.endVertex();
	}
	
	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.CUSTOM;
	}
	
	private static ThVector3 getRelativeViewVector(ThVector3 pos, Camera cam)
	{
		var r = cam.getPosition();
		return new ThVector3(r.x - pos.x, r.y - pos.y, r.z - pos.z);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider
			implements ParticleProvider<Bolt>
	{
		@Nullable
		@Override
		public Particle createParticle(Bolt options, ClientLevel level, double x, double y, double z, double tx, double ty, double tz)
		{
			return new BoltParticle(level, new Vec3(x, y, z), new Vec3(tx, ty, tz), options);
		}
	}
}