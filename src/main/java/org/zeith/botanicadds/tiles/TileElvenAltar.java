package org.zeith.botanicadds.tiles;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.mixins.BlockEntityAccessor;
import org.zeith.botanicadds.mixins.RunicAltarBlockEntityAccessor;
import org.zeith.botanicadds.util.SparkUtil;
import org.zeith.hammerlib.util.java.Cast;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.WandOfTheForestItem;

import java.util.List;

public class TileElvenAltar
		extends RunicAltarBlockEntity
		implements SparkAttachable
{
	public TileElvenAltar(BlockPos pos, BlockState state)
	{
		super(pos, state);
		((BlockEntityAccessor) this).botanicAdditionsSetType(TilesBA.ELVEN_ALTAR);
	}
	
	@Override
	public boolean canAttachSpark(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getAvailableSpaceForMana()
	{
		return Math.max(0, getTargetMana() - getCurrentMana());
	}
	
	@Override
	public ManaSpark getAttachedSpark()
	{
		List<Entity> sparks = this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition.above(), this.worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(ManaSpark.class));
		if(sparks.size() == 1) return Cast.cast(sparks.get(0));
		else return null;
	}
	
	@Override
	public boolean areIncomingTranfersDone()
	{
		return !canReceiveManaFromBursts();
	}
	
	@Override
	public void receiveMana(int mana)
	{
		super.receiveMana(mana);
		if(mana != 0) VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}
	
	private final LazyOptional<SparkAttachable> spark = LazyOptional.of(() -> this);
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == BotaniaForgeCapabilities.SPARK_ATTACHABLE) return BotaniaForgeCapabilities.SPARK_ATTACHABLE.orEmpty(cap, spark);
		return super.getCapability(cap, side);
	}
	
	public static void serverTickElven(Level level, BlockPos worldPosition, BlockState state, TileElvenAltar self)
	{
		serverTick(level, worldPosition, state, self);
		if(self.manaToGet > 0) SparkUtil.startRequestingMana(self, self.getAttachedSpark());
	}
	
	public static class ElvenHud
	{
		public static void render(TileElvenAltar altar, PoseStack ms, Minecraft mc)
		{
			int xc = mc.getWindow().getGuiScaledWidth() / 2;
			int yc = mc.getWindow().getGuiScaledHeight() / 2;
			
			float angle = -90;
			int radius = 24;
			int amt = 0;
			for(int i = 0; i < altar.inventorySize(); i++)
			{
				if(altar.getItemHandler().getItem(i).isEmpty())
				{
					break;
				}
				amt++;
			}
			
			if(amt > 0)
			{
				float anglePer = 360F / amt;
				altar.level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, altar.getItemHandler(), altar.level).ifPresent(recipe ->
				{
					RenderSystem.enableBlend();
					RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					
					float progress = (float) altar.getCurrentMana() / (float) altar.manaToGet;
					
					if(Float.isFinite(progress)) progress = Mth.clamp(progress, 0F, 1F);
					else progress = 0;
					
					RenderSystem.setShaderTexture(0, HUDHandler.manaBar);
					RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
					RenderHelper.drawTexturedModalRect(ms, xc + radius + 9, yc - 8, progress == 1F ? 0 : 22, 8, 22, 15);
					
					if(progress == 1F)
					{
						mc.getItemRenderer().renderGuiItem(new ItemStack(BotaniaBlocks.livingrock), xc + radius + 16, yc + 8);
						PoseStack pose = RenderSystem.getModelViewStack();
						pose.pushPose();
						pose.translate(0, 0, 100);
						RenderSystem.applyModelViewMatrix();
						// If the player is holding a WandOfTheForestItem or has one in their inventory, render that instead of a generic twigWand
						ItemStack playerWand = PlayerHelper.getFirstHeldItemClass(mc.player, WandOfTheForestItem.class);
						if(playerWand.isEmpty())
						{
							playerWand = PlayerHelper.getItemClassFromInventory(mc.player, WandOfTheForestItem.class);
						}
						ItemStack wandToRender = playerWand.isEmpty() ? new ItemStack(BotaniaItems.twigWand) : playerWand;
						mc.getItemRenderer().renderGuiItem(wandToRender, xc + radius + 24, yc + 8);
						pose.popPose();
						RenderSystem.applyModelViewMatrix();
					}
					
					RenderHelper.renderProgressPie(ms, xc + radius + 32, yc - 8, progress, recipe.assemble(altar.getItemHandler()));
					
					if(progress == 1F)
					{
						mc.font.draw(ms, "+", xc + radius + 14, yc + 12, 0xFFFFFF);
					}
				});
				
				for(int i = 0; i < amt; i++)
				{
					double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
					double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
					PoseStack pose = RenderSystem.getModelViewStack();
					pose.pushPose();
					pose.translate(xPos, yPos, 0);
					RenderSystem.applyModelViewMatrix();
					mc.getItemRenderer().renderGuiItem(altar.getItemHandler().getItem(i), 0, 0);
					pose.popPose();
					RenderSystem.applyModelViewMatrix();
					
					angle += anglePer;
				}
			}
			if(altar instanceof RunicAltarBlockEntityAccessor a && a.botanicAdditions_recipeKeepTicks() > 0 && altar.canAddLastRecipe())
			{
				String s = I18n.get("botaniamisc.altarRefill0");
				mc.font.drawShadow(ms, s, xc - mc.font.width(s) / 2, yc + 10, 0xFFFFFF);
				s = I18n.get("botaniamisc.altarRefill1");
				mc.font.drawShadow(ms, s, xc - mc.font.width(s) / 2, yc + 20, 0xFFFFFF);
			}
		}
	}
}