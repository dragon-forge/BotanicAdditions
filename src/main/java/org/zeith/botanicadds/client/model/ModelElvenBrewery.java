package org.zeith.botanicadds.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.mixins.ModelAccessor;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.client.model.BotanicalBreweryModel;

import java.util.Map;
import java.util.function.Function;

public class ModelElvenBrewery
		extends BotanicalBreweryModel
{
	public ModelElvenBrewery(ModelPart root)
	{
		super(root);
		
		Map<ResourceLocation, ResourceLocation> replacedTexture = Map.of(
				new ResourceLocation(ResourcesLib.MODEL_BREWERY), BotanicAdditions.id("textures/model/elven_brewery.png")
		);
		
		((ModelAccessor) this).botanicAdditions_setRenderType(replacing(RenderType::entitySolid, replacedTexture));
	}
	
	public static Function<ResourceLocation, RenderType> replacing(Function<ResourceLocation, RenderType> origin, Map<ResourceLocation, ResourceLocation> replaces)
	{
		return tx -> origin.apply(replaces.getOrDefault(tx, tx));
	}
}