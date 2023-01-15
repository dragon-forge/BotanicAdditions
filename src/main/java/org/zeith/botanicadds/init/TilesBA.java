package org.zeith.botanicadds.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.botanicadds.client.render.tile.TESRElvenBrewery;
import org.zeith.botanicadds.client.render.tile.TESRGaiaPlate;
import org.zeith.botanicadds.tiles.*;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import org.zeith.hammerlib.api.forge.BlockAPI;
import vazkii.botania.client.render.block_entity.RunicAltarBlockEntityRenderer;

@SimplyRegister
public interface TilesBA
{
	@RegistryName("gaia_plate")
	@TileRenderer(TESRGaiaPlate.class)
	BlockEntityType<TileGaiaPlate> GAIA_PLATE = BlockAPI.createBlockEntityType(TileGaiaPlate::new, BlocksBA.GAIA_PLATE);
	
	@RegistryName("mana_tesseract")
	BlockEntityType<TileManaTesseract> MANA_TESSERACT = BlockAPI.createBlockEntityType(TileManaTesseract::new, BlocksBA.MANA_TESSERACT);
	
	@RegistryName("elven_altar")
	@TileRenderer(RunicAltarBlockEntityRenderer.class)
	BlockEntityType<TileElvenAltar> ELVEN_ALTAR = BlockAPI.createBlockEntityType(TileElvenAltar::new, BlocksBA.ELVEN_ALTAR);
	
	@RegistryName("elven_brewery")
	@TileRenderer(TESRElvenBrewery.class)
	BlockEntityType<TileElvenBrewery> ELVEN_BREWERY = BlockAPI.createBlockEntityType(TileElvenBrewery::new, BlocksBA.ELVEN_BREWERY);
}