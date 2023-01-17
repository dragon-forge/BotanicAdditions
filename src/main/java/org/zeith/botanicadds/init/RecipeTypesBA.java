package org.zeith.botanicadds.init;

import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface RecipeTypesBA
{
	@RegistryName("gaia_plate")
	RecipeGaiaPlate.GaiaPlateRecipeType GAIA_PLATE = new RecipeGaiaPlate.GaiaPlateRecipeType();
}