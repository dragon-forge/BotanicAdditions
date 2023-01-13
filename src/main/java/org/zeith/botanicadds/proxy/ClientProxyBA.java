package org.zeith.botanicadds.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.init.ItemsBA;

import java.awt.*;
import java.util.*;

public class ClientProxyBA
		extends CommonProxyBA
{
	private static final Map<ResourceLocation, Set<ResourceLocation>> SPRITES_BY_ATLAS = new HashMap<>();
	
	public static final Material TERRA_CATALYST_OVERLAY = register(new Material(InventoryMenu.BLOCK_ATLAS, BotanicAdditions.id("block/terra_catalyst_overlay")));
	
	public ClientProxyBA()
	{
		var bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		bus.addListener(this::registerItemColors);
		bus.addListener(this::registerMaterials);
	}
	
	public float getWorldElapsedTicks()
	{
		var mc = Minecraft.getInstance();
		if(mc.level != null) return mc.level.dayTime() + mc.getPartialTick();
		return 0F;
	}
	
	public void registerItemColors(RegisterColorHandlersEvent.Item e)
	{
		e.register((s, t) -> Color.HSBtoRGB(getWorldElapsedTicks() * 2 % 360 / 360F, 0.25F, 1F), ItemsBA.GAIA_SHARD);
	}
	
	public void registerMaterials(TextureStitchEvent.Pre e)
	{
		SPRITES_BY_ATLAS.getOrDefault(e.getAtlas().location(), Set.of()).forEach(e::addSprite);
	}
	
	private static Material register(Material mat)
	{
		SPRITES_BY_ATLAS.computeIfAbsent(mat.atlasLocation(), rl -> new HashSet<>()).add(mat.texture());
		return mat;
	}
}