package org.zeith.botanicadds.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface RecipeSerializersBA
{
	@RegistryName("gaia_plate")
	RecipeSerializer<RecipeGaiaPlate> GAIA_PLATE = new RecipeGaiaPlate.Serializer();
}