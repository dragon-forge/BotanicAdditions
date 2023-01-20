package org.zeith.botanicadds.mixins;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.botanicadds.BotanicAdditions;

import java.util.*;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin
{
	@Shadow
	private Map<ResourceLocation, Recipe<?>> byName;
	
	private final Map<ResourceLocation, ResourceLocation> botanicAdditionsSpoof = Util.make(new HashMap<>(), map ->
	{
		map.put(new ResourceLocation("botania", "mana_fluxfield"), BotanicAdditions.id("mana_fluxfield"));
	});
	
	@Inject(
			method = "byKey",
			at = @At("HEAD"),
			cancellable = true
	)
	private void replaceRecipeId(ResourceLocation id, CallbackInfoReturnable<Optional<? extends Recipe<?>>> cir)
	{
		if(botanicAdditionsSpoof.containsKey(id))
			cir.setReturnValue(Optional.ofNullable(
					byName.getOrDefault(
							botanicAdditionsSpoof.getOrDefault(id, id),
							byName.get(id)
					)
			));
	}
}