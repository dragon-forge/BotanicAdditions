package tk.zeitheron.botanicadds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHUDRenderable
{
	@SideOnly(Side.CLIENT)
	void renderHUDPlz(Minecraft mc, ScaledResolution res);
}