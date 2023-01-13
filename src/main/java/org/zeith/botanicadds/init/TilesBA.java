package org.zeith.botanicadds.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.botanicadds.client.render.tile.TESRGaiaPlate;
import org.zeith.botanicadds.tiles.TileGaiaPlate;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;

@SimplyRegister
public interface TilesBA
{
	@RegistryName("gaia_plate")
	@TileRenderer(TESRGaiaPlate.class)
	BlockEntityType<TileGaiaPlate> GAIA_PLATE = BlockEntityType.Builder.of(TileGaiaPlate::new, BlocksBA.GAIA_PLATE).build(null);
}