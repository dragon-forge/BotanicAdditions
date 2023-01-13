package org.zeith.botanicadds.compat.jei.cats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.botanicadds.init.BlocksBA;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.integration.jei.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.TerrestrialAgglomerationDrawable;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.Iterator;

public class GaiaPlateRecipeCategory
		implements IRecipeCategory<RecipeGaiaPlate>
{
	public static final RecipeType<RecipeGaiaPlate> TYPE = new RecipeType<>(BotanicAdditions.id("gaia_plate"), RecipeGaiaPlate.class);
	private final Component localizedName;
	private final IDrawable background;
	private final IDrawable overlay;
	private final IDrawable icon;
	private final IDrawable terraPlate;
	
	public GaiaPlateRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = ResourceLocationHelper.prefix("textures/gui/terrasteel_jei_overlay.png");
		this.background = guiHelper.createBlankDrawable(114, 131);
		this.overlay = guiHelper.createDrawable(location, 42, 29, 64, 64);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlocksBA.GAIA_PLATE));
		this.localizedName = BlocksBA.GAIA_PLATE.getName();
		IDrawable livingrock = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlocksBA.DREAMROCK));
		this.terraPlate = new TerrestrialAgglomerationDrawable(livingrock, livingrock, guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlocksBA.ELVEN_LAPIS_BLOCK)));
	}
	
	@Override
	public @NotNull RecipeType<RecipeGaiaPlate> getRecipeType()
	{
		return TYPE;
	}
	
	@Override
	public @NotNull Component getTitle()
	{
		return this.localizedName;
	}
	
	@Override
	public @NotNull IDrawable getBackground()
	{
		return this.background;
	}
	
	@Override
	public @NotNull IDrawable getIcon()
	{
		return this.icon;
	}
	
	@Override
	public void draw(@NotNull RecipeGaiaPlate recipe, @NotNull IRecipeSlotsView view, @NotNull PoseStack ms, double mouseX, double mouseY)
	{
		RenderSystem.enableBlend();
		this.overlay.draw(ms, 25, 14);
		HUDHandler.renderManaBar(ms, 6, 126, 255, 0.75F, recipe.getMana(), 1_000_000);
		this.terraPlate.draw(ms, 35, 92);
		RenderSystem.disableBlend();
	}
	
	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeGaiaPlate recipe, @NotNull IFocusGroup focusGroup)
	{
		builder.addSlot(RecipeIngredientRole.OUTPUT, 48, 37).addItemStack(recipe.getResultItem());
		double angleBetweenEach = 360.0 / (double) recipe.getIngredients().size();
		Vec2 point = new Vec2(48.0F, 5.0F);
		Vec2 center = new Vec2(48.0F, 37.0F);
		
		for(Iterator<Ingredient> var8 = recipe.getIngredients().iterator(); var8.hasNext(); point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angleBetweenEach))
		{
			Ingredient ingr = var8.next();
			builder.addSlot(RecipeIngredientRole.INPUT, (int) point.x, (int) point.y).addIngredients(ingr);
		}
		
		builder.addSlot(RecipeIngredientRole.CATALYST, 48, 92).addItemStack(new ItemStack(BlocksBA.GAIA_PLATE));
	}
}
