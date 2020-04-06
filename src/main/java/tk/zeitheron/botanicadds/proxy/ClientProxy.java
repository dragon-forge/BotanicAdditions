package tk.zeitheron.botanicadds.proxy;

import java.awt.Color;

import tk.zeitheron.botanicadds.BotanicAdditions;
import tk.zeitheron.botanicadds.IHUDRenderable;
import tk.zeitheron.botanicadds.InfoBA;
import tk.zeitheron.botanicadds.blocks.tiles.TileElvenAltar;
import tk.zeitheron.botanicadds.flowers.base.Flower;
import tk.zeitheron.botanicadds.init.BlocksBA;
import tk.zeitheron.botanicadds.init.ItemsBA;
import tk.zeitheron.botanicadds.items.ItemTerraProtector;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.Botania;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		ModelLoader.setCustomStateMapper(BlocksBA.DREAMING_POOL, new StateMap.Builder().ignore(BotaniaStateProps.COLOR).build());
		super.preInit();
	}
	
	@Override
	public void init()
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((s, t) -> Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 0.25F, 1F), ItemsBA.GAIA_SHARD);
		
		super.init();
	}
	
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent evt)
	{
		for(Class<? extends SubTileEntity> cl : BotanicAdditions.flowers)
		{
			Flower fl = cl.getDeclaredAnnotation(Flower.class);
			BotaniaAPIClient.registerSubtileModel(cl, new ModelResourceLocation(InfoBA.MOD_ID + ":" + fl.value()));
		}
	}
	
	public static TextureAtlasSprite terraCatalystOverlay;
	
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre evt)
	{
		terraCatalystOverlay = forName(evt.getMap(), "terra_catalyst_overlay", "blocks");
	}
	
	public static TextureAtlasSprite forName(TextureMap ir, String name, String dir)
	{
		return ir.registerSprite(new ResourceLocation(InfoBA.MOD_ID, dir + "/" + name));
	}
	
	@SubscribeEvent
	public void onDrawScreenPost(RenderGameOverlayEvent.Post event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.profiler;
		ItemStack main = mc.player.getHeldItemMainhand();
		ItemStack offhand = mc.player.getHeldItemOffhand();
		
		if(event.getType() == ElementType.ALL)
		{
			profiler.startSection("botanicadditions-hud");
			RayTraceResult pos = mc.objectMouseOver;
			
			if(pos != null)
			{
				IBlockState state = pos.typeOfHit == RayTraceResult.Type.BLOCK ? mc.world.getBlockState(pos.getBlockPos()) : null;
				Block block = state == null ? null : state.getBlock();
				TileEntity tile = pos.typeOfHit == RayTraceResult.Type.BLOCK ? mc.world.getTileEntity(pos.getBlockPos()) : null;
				
				if(tile instanceof IHUDRenderable)
					((IHUDRenderable) tile).renderHUDPlz(mc, event.getResolution());
				if(tile != null && tile instanceof TileElvenAltar)
					((TileElvenAltar) tile).renderHUD(mc, event.getResolution());
			}
		}
	}
	
	@SubscribeEvent
	public void onToolTipRender(RenderTooltipEvent.PostText evt)
	{
		if(evt.getStack().isEmpty())
			return;
		
		ItemStack stack = evt.getStack();
		Minecraft mc = Minecraft.getMinecraft();
		int width = evt.getWidth();
		int height = 3;
		int tooltipX = evt.getX();
		int tooltipY = evt.getY() - 4;
		FontRenderer font = evt.getFontRenderer();
		
		if(stack.getItem() instanceof ItemTerraProtector)
		{
			NBTTagCompound shield = stack.getSubCompound("Shield");
			if(shield != null)
			{
				float charge = shield.getInteger("Charge") / 100_000F;
				float defense = shield.getInteger("Defense") / 100F;
				float heat = shield.getInteger("Heat") / 1000F;
				
				drawManaBar(stack, charge, tooltipX, tooltipY, width, height, 0.528F);
				drawManaBar(stack, defense, tooltipX, tooltipY - height - 1, width, height, defense / 4F);
				drawManaBar(stack, heat, tooltipX, tooltipY - height * 2 - 2, width, height, 0.75F);
			}
		}
	}
	
	private static void drawManaBar(ItemStack stack, float fraction, int mouseX, int mouseY, int width, int height, float hue)
	{
		int manaBarWidth = (int) Math.ceil(width * fraction);
		
		GlStateManager.disableDepth();
		Gui.drawRect(mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
		Gui.drawRect(mouseX, mouseY - height, mouseX + manaBarWidth, mouseY, Color.HSBtoRGB(hue, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.1F + 0.6F, 1F));
		Gui.drawRect(mouseX + manaBarWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);
	}
}