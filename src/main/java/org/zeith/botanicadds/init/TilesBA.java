package org.zeith.botanicadds.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.botanicadds.client.render.tile.TESRGaiaPlate;
import org.zeith.botanicadds.tiles.TileGaiaPlate;
import org.zeith.botanicadds.tiles.TileManaTesseract;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import org.zeith.hammerlib.api.forge.BlockAPI;

@SimplyRegister
public interface TilesBA
{
	@RegistryName("gaia_plate")
	@TileRenderer(TESRGaiaPlate.class)
	BlockEntityType<TileGaiaPlate> GAIA_PLATE = BlockAPI.createBlockEntityType(TileGaiaPlate::new, BlocksBA.GAIA_PLATE);
	
	@RegistryName("mana_tesseract")
	BlockEntityType<?> MANA_TESSERACT = BlockAPI.createBlockEntityType(TileManaTesseract::new, BlocksBA.MANA_TESSERACT);
}