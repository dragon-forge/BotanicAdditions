package org.zeith.botanicadds;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.botanicadds.init.BlocksBA;
import org.zeith.botanicadds.init.LootTableAlteratorsBA;
import org.zeith.botanicadds.proxy.ClientProxyBA;
import org.zeith.botanicadds.proxy.CommonProxyBA;
import org.zeith.botanicadds.tiles.TileGaiaPlate;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.util.CommonMessages;
import org.zeith.hammerlib.util.mcf.Resources;
import vazkii.patchouli.api.PatchouliAPI;

@Mod(BotanicAdditions.MOD_ID)
public class BotanicAdditions
{
	public static final Logger LOG = LogManager.getLogger("BotanicAdditions");
	public static final CommonProxyBA PROXY = IProxy.create(() -> ClientProxyBA::new, () -> CommonProxyBA::new);
	public static final String MOD_ID = "botanicadds";
	
	@CreativeTab.RegisterTab
	public static final CreativeTab TAB = new CreativeTab(id("root"),
			b -> b.icon(() -> BlocksBA.MANA_TESSERACT.asItem().getDefaultInstance())
					.title(Component.translatable("itemGroup." + MOD_ID))
	);
	
	public BotanicAdditions()
	{
		CommonMessages.printMessageOnIllegalRedistribution(BotanicAdditions.class,
				LOG, "BotanicAdditions", "https://www.curseforge.com/minecraft/mc-mods/botanic-additions");
		LanguageAdapter.registerMod(MOD_ID);
		
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		modBus.addListener(this::checkFingerprint);
		modBus.addListener(this::setup);
	}
	
	public static ResourceLocation id(String path)
	{
		return Resources.location(MOD_ID, path);
	}
	
	public void checkFingerprint(FMLFingerprintCheckEvent e)
	{
		CommonMessages.printMessageOnFingerprintViolation(e, "97e852e9b3f01b83574e8315f7e77651c6605f2b455919a7319e9869564f013c",
				LOG, "BotanicAdditions", "https://www.curseforge.com/minecraft/mc-mods/botanic-additions");
	}
	
	public void setup(FMLCommonSetupEvent e)
	{
		PatchouliAPI.get().registerMultiblock(ForgeRegistries.ITEMS.getKey(BlocksBA.GAIA_PLATE.asItem()), TileGaiaPlate.MULTIBLOCK.get());
		LootTableAlteratorsBA.init();
	}
}