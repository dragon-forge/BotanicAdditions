package com.zeitheron.botanicadds;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zeitheron.botanicadds.flowers.base.Flower;
import com.zeitheron.botanicadds.init.AltarRecipesBA;
import com.zeitheron.botanicadds.init.BlocksBA;
import com.zeitheron.botanicadds.init.ElvenTradesBA;
import com.zeitheron.botanicadds.init.GaiaPlateRecipesBA;
import com.zeitheron.botanicadds.init.ItemsBA;
import com.zeitheron.botanicadds.init.LexiconBA;
import com.zeitheron.botanicadds.init.PetalRecipesBA;
import com.zeitheron.botanicadds.init.PoolRecipesBA;
import com.zeitheron.botanicadds.init.RecipesBA;
import com.zeitheron.botanicadds.proxy.CommonProxy;
import com.zeitheron.botanicadds.recipes.RecipeLinkTesseract;
import com.zeitheron.hammercore.internal.SimpleRegistration;
import com.zeitheron.hammercore.utils.AnnotatedInstanceUtil;
import com.zeitheron.hammercore.utils.FinalFieldHelper;
import com.zeitheron.hammercore.utils.ForgeRegistryUtils;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.signature.BasicSignature;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibOreDict;

@EventBusSubscriber
@Mod(modid = InfoBA.MOD_ID, name = InfoBA.MOD_NAME, version = InfoBA.MOD_VERSION, dependencies = "required-after:hammercore;required-after:botania")
public class BotanicAdditions
{
	public static final List<Class<? extends SubTileEntity>> flowers = null;
	
	@SidedProxy(serverSide = "com.zeitheron.botanicadds.proxy.CommonProxy", clientSide = "com.zeitheron.botanicadds.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static final Logger LOG = LogManager.getLogger(InfoBA.MOD_ID);
	
	public static final CreativeTabs TAB = new CreativeTabs(InfoBA.MOD_ID)
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(BlocksBA.MANA_TESSERACT);
		}
		
		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> items)
		{
			Item sf = Item.getItemFromBlock(ModBlocks.specialFlower);
			for(Item item : Item.REGISTRY)
			{
				item.getSubItems(this, items);
				if(item == sf)
				{
					for(Class<? extends SubTileEntity> cl : flowers)
					{
						Flower fl = cl.getDeclaredAnnotation(Flower.class);
						String id = "ba_" + fl.value();
						items.add(ItemBlockSpecialFlower.ofType(id));
					}
				}
			}
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		FinalFieldHelper.setStaticFinalField(BotanicAdditions.class, "flowers", AnnotatedInstanceUtil.getTypes(e.getAsmData(), Flower.class, SubTileEntity.class));
		SimpleRegistration.registerFieldBlocksFrom(BlocksBA.class, InfoBA.MOD_ID, TAB);
		SimpleRegistration.registerFieldItemsFrom(ItemsBA.class, InfoBA.MOD_ID, TAB);
		MinecraftForge.EVENT_BUS.register(proxy);
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init();
		
		GaiaPlateRecipesBA.init();
		PetalRecipesBA.init();
		AltarRecipesBA.init();
		PoolRecipesBA.init();
		ElvenTradesBA.init();
		RecipesBA.init();
		LexiconBA.init();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e)
	{
		for(Class<? extends SubTileEntity> cl : flowers)
		{
			Flower fl = cl.getDeclaredAnnotation(Flower.class);
			String id = "ba_" + fl.value();
			BotaniaAPI.registerSubTile(id, cl);
			BotaniaAPI.registerSubTileSignature(cl, new BasicSignature(id));
			BotaniaAPI.addSubTileToCreativeMenu(id);
		}
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> e)
	{
		ResourceLocation ultraSpreader = new ResourceLocation("botania", "spreader_3");
		
		ForgeRegistryUtils.deleteEntry(e.getRegistry(), ultraSpreader);
		
		ModContainer c = Loader.instance().activeModContainer();
		
		Loader.instance().setActiveModContainer(Loader.instance().getIndexedModList().get("botania"));
		// Begin overrides
		e.getRegistry().register(SimpleRegistration.parseShapedRecipe(new ItemStack(ModBlocks.spreader, 1, 3), "gsd", 'g', new ItemStack(ItemsBA.GAIA_SHARD), 's', new ItemStack(ModBlocks.spreader, 1, 2), 'd', LibOreDict.DRAGONSTONE).setRegistryName(ultraSpreader));
		//End overrides
		Loader.instance().setActiveModContainer(c);
	}
}