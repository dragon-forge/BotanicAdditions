package tk.zeitheron.botanicadds.lexicon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import tk.zeitheron.botanicadds.InfoBA;
import tk.zeitheron.botanicadds.api.GaiaPlateRecipes;
import tk.zeitheron.botanicadds.init.BlocksBA;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class PageGaiaPlate extends PageRecipe
{
	private static final ResourceLocation terrasteelOverlay = new ResourceLocation(LibResources.GUI_TERRASTEEL_OVERLAY);
	
	final List<GaiaPlateRecipes.RecipeGaiaPlate> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;
	
	public PageGaiaPlate(String unlocalizedName, List<GaiaPlateRecipes.RecipeGaiaPlate> recipes)
	{
		super(unlocalizedName);
		this.recipes = recipes;
	}
	
	public PageGaiaPlate(String unlocalizedName, GaiaPlateRecipes.RecipeGaiaPlate recipe)
	{
		this(unlocalizedName, Collections.singletonList(recipe));
	}
	
	@Override
	public void onPageAdded(LexiconEntry entry, int index)
	{
		for(GaiaPlateRecipes.RecipeGaiaPlate recipe : recipes)
			LexiconRecipeMappings.map(recipe.getOutput(), entry, index);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen()
	{
		if(GuiScreen.isShiftKeyDown())
			return;
		
		if(ticksElapsed % 20 == 0)
		{
			recipeAt++;
			
			if(recipeAt == recipes.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}
	
	@Override
	public List<ItemStack> getDisplayedRecipes()
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		for(GaiaPlateRecipes.RecipeGaiaPlate r : recipes)
			list.add(r.getOutput());
		return list;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my)
	{
		GaiaPlateRecipes.RecipeGaiaPlate recipe = recipes.get(recipeAt);
		
		Block block1 = BlocksBA.DREAMROCK;
		Block block2 = BlocksBA.ELVEN_LAPIS_BLOCK;
		Block block3 = BlocksBA.GAIA_PLATE;
		
		GlStateManager.translate(0F, 0F, -10F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 103, new ItemStack(block1), false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 106, new ItemStack(block2), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 106, new ItemStack(block2), false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 110, new ItemStack(block1), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 14, gui.getTop() + 110, new ItemStack(block1), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 13, gui.getTop() + 110, new ItemStack(block1), false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 114, new ItemStack(block2), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 114, new ItemStack(block2), false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 1, gui.getTop() + 117, new ItemStack(block1), false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 102, new ItemStack(block3), false);
		GlStateManager.translate(0F, 0F, -10F);
		
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 30, recipe.getOutput(), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 80, convert(recipe.getInputs().get(0)), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 20, gui.getTop() + 86, convert(recipe.getInputs().get(1)), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 19, gui.getTop() + 86, convert(recipe.getInputs().get(2)), false);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(terrasteelOverlay);
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();
		
		int width = gui.getWidth() - 30;
		int height = gui.getHeight();
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + height - 52;
		PageText.renderText(x, y, width, height, I18n.format(InfoBA.MOD_ID + ".text.pools", (recipe.getMana() / (float) TilePool.MAX_MANA)) + "");
	}
	
	private ItemStack convert(Object input)
	{
		float currentDegree = ConfigHandler.lexiconRotatingItems ? GuiScreen.isShiftKeyDown() ? ticksElapsed : ticksElapsed + ClientTickHandler.partialTicks : 0;
		if(input instanceof String)
		{
			NonNullList<ItemStack> list = OreDictionary.getOres((String) input);
			int size = list.size();
			input = list.get(size - (int) (currentDegree / 40) % size - 1);
			input = list.get(0);
		}
		return (ItemStack) input;
	}
}