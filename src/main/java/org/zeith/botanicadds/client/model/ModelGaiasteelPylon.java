package org.zeith.botanicadds.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;

public class ModelGaiasteelPylon
{
	private final ModelPart platef;
	private final ModelPart plateb;
	private final ModelPart platel;
	private final ModelPart plater;
	
	private final ModelPart shardlbt;
	private final ModelPart shardrbt;
	private final ModelPart shardlft;
	private final ModelPart shardrft;
	
	private final ModelPart shardlbb;
	private final ModelPart shardrbb;
	private final ModelPart shardlfb;
	private final ModelPart shardrfb;
	
	public ModelGaiasteelPylon(ModelPart root)
	{
		platef = root.getChild("platef");
		plateb = root.getChild("plateb");
		platel = root.getChild("platel");
		plater = root.getChild("plater");
		shardlbt = root.getChild("shardlbt");
		shardrbt = root.getChild("shardrbt");
		shardlft = root.getChild("shardlft");
		shardrft = root.getChild("shardrft");
		shardlbb = root.getChild("shardlbb");
		shardrbb = root.getChild("shardrbb");
		shardlfb = root.getChild("shardlfb");
		shardrfb = root.getChild("shardrfb");
	}
	
	public static MeshDefinition createMesh()
	{
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("platef", CubeListBuilder.create().texOffs(36, 0)
						.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("plateb", CubeListBuilder.create().texOffs(36, 0)
						.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2),
				PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, Mth.PI, 0.0F));
		root.addOrReplaceChild("platel", CubeListBuilder.create().texOffs(36, 0)
						.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2),
				PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, Mth.HALF_PI, 0.0F));
		root.addOrReplaceChild("plater", CubeListBuilder.create().texOffs(36, 0)
						.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 2),
				PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, -Mth.HALF_PI, 0.0F));
		root.addOrReplaceChild("shardrft", CubeListBuilder.create().texOffs(16, 32)
						.addBox(2.0F, -13.0F, -5.0F, 3, 7, 3),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardlbt", CubeListBuilder.create()
						.addBox(-5.0F, -11.0F, 0.0F, 6, 9, 5),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardrbt", CubeListBuilder.create().texOffs(22, 0)
						.addBox(3.0F, -12.0F, 0.0F, 2, 8, 5),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardlft", CubeListBuilder.create().texOffs(0, 32)
						.addBox(-5.0F, -10.0F, -5.0F, 5, 10, 3),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardrfb", CubeListBuilder.create().texOffs(16, 42)
						.addBox(2.0F, -4.0F, -5.0F, 3, 9, 3),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardlbb", CubeListBuilder.create().texOffs(0, 14)
						.addBox(-5.0F, 0.0F, 0.0F, 6, 7, 5),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardrbb", CubeListBuilder.create().texOffs(22, 13)
						.addBox(3.0F, -2.0F, 0.0F, 2, 8, 5),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardlfb", CubeListBuilder.create().texOffs(0, 45)
						.addBox(-5.0F, 2.0F, -5.0F, 5, 6, 3),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		return mesh;
	}
	
	public void renderCrystal(PoseStack ms, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha)
	{
		shardrft.render(ms, buffer, light, overlay, red, green, blue, alpha);
		shardlbt.render(ms, buffer, light, overlay, red, green, blue, alpha);
		shardrbt.render(ms, buffer, light, overlay, red, green, blue, alpha);
		shardlft.render(ms, buffer, light, overlay, red, green, blue, alpha);
		
		shardrfb.render(ms, buffer, light, overlay, red, green, blue, alpha);
		shardlbb.render(ms, buffer, light, overlay, red, green, blue, alpha);
		shardrbb.render(ms, buffer, light, overlay, red, green, blue, alpha);
		shardlfb.render(ms, buffer, light, overlay, red, green, blue, alpha);
	}
	
	public void renderRing(PoseStack ms, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha)
	{
		platef.render(ms, buffer, light, overlay, red, green, blue, alpha);
		plateb.render(ms, buffer, light, overlay, red, green, blue, alpha);
		platel.render(ms, buffer, light, overlay, red, green, blue, alpha);
		plater.render(ms, buffer, light, overlay, red, green, blue, alpha);
	}
}