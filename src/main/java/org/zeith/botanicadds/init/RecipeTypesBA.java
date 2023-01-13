package org.zeith.botanicadds.init;

import net.minecraft.world.item.crafting.RecipeType;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface RecipeTypesBA
{
	@RegistryName("gaia_plate")
	RecipeType<RecipeGaiaPlate> GAIA_PLATE = RecipeType.simple(BotanicAdditions.id("gaia_plate"));
}