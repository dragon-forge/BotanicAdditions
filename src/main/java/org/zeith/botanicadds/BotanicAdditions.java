package org.zeith.botanicadds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.botanicadds.proxy.ClientProxyBA;
import org.zeith.botanicadds.proxy.CommonProxyBA;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.util.CommonMessages;

@Mod(BotanicAdditions.MOD_ID)
public class BotanicAdditions
{
	public static final Logger LOG = LogManager.getLogger("BotanicAdditions");
	public static final CommonProxyBA PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxyBA::new, () -> CommonProxyBA::new);
	public static final String MOD_ID = "botanicadds";
	
	public static CreativeModeTab TAB = new CreativeModeTab(MOD_ID)
	{
		@Override
		public ItemStack makeIcon()
		{
			return ItemsBA.RUNE_TP.getDefaultInstance();
		}
	};
	
	public BotanicAdditions()
	{
		CommonMessages.printMessageOnIllegalRedistribution(BotanicAdditions.class,
				LOG, "BotanicAdditions", "https://www.curseforge.com/minecraft/mc-mods/botanic-additions");
		LanguageAdapter.registerMod(MOD_ID);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::checkFingerprint);
	}
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
	
	public void checkFingerprint(FMLFingerprintCheckEvent e)
	{
		CommonMessages.printMessageOnFingerprintViolation(e, "97e852e9b3f01b83574e8315f7e77651c6605f2b455919a7319e9869564f013c",
				LOG, "BotanicAdditions", "https://www.curseforge.com/minecraft/mc-mods/botanic-additions");
	}
}