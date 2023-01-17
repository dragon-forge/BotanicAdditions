package org.zeith.botanicadds.compat.patchouli.client.component;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import org.zeith.botanicadds.BotanicAdditions;
import vazkii.patchouli.api.*;

import java.util.function.UnaryOperator;

public class GaiaPlateComponent
		implements ICustomComponent
{
	public IVariable corner = IVariable.wrap(BotanicAdditions.id("dreamrock").toString());
	public IVariable center = IVariable.wrap(BotanicAdditions.id("dreamrock").toString());
	public IVariable edge = IVariable.wrap(BotanicAdditions.id("elven_lapis_block").toString());
	public IVariable plate = IVariable.wrap(BotanicAdditions.id("gaia_plate").toString());
	
	private transient int x, y;
	private transient ItemStack cornerBlock, centerBlock, middleBlock, plateBlock;
	
	@Override
	public void build(int componentX, int componentY, int pageNum)
	{
		this.x = componentX;
		this.y = componentY;
	}
	
	@Override
	public void render(PoseStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY)
	{
		ms.pushPose();
		ms.translate(0, 0, -10);
		context.renderItemStack(ms, x + 13, y + 1, mouseX, mouseY, cornerBlock);
		
		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 20, y + 4, mouseX, mouseY, middleBlock);
		context.renderItemStack(ms, x + 7, y + 4, mouseX, mouseY, middleBlock);
		
		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 13, y + 8, mouseX, mouseY, cornerBlock);
		context.renderItemStack(ms, x + 27, y + 8, mouseX, mouseY, centerBlock);
		context.renderItemStack(ms, x, y + 8, mouseX, mouseY, cornerBlock);
		
		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 7, y + 12, mouseX, mouseY, middleBlock);
		context.renderItemStack(ms, x + 20, y + 12, mouseX, mouseY, middleBlock);
		
		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 14, y + 15, mouseX, mouseY, cornerBlock);
		
		ms.translate(0F, 0F, 5F);
		context.renderItemStack(ms, x + 13, y, mouseX, mouseY, plateBlock);
		ms.popPose();
	}
	
	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup)
	{
		cornerBlock = lookup.apply(corner).as(ItemStack.class);
		centerBlock = lookup.apply(center).as(ItemStack.class);
		middleBlock = lookup.apply(edge).as(ItemStack.class);
		plateBlock = lookup.apply(plate).as(ItemStack.class);
	}
}