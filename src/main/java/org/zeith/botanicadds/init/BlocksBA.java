package org.zeith.botanicadds.init;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.blocks.*;
import org.zeith.hammerlib.annotations.*;

@SimplyRegister(creativeTabs = @Ref(value = BotanicAdditions.class, field = "TAB"))
public interface BlocksBA
{
	@RegistryName("gaiasteel_pylon")
	BlockGaiasteelPylon GAIASTEEL_PYLON = new BlockGaiasteelPylon(BlockBehaviour.Properties.of().strength(5.5F).sound(SoundType.METAL).lightLevel(s -> 13).requiresCorrectToolForDrops());
	
	@RegistryName("terra_catalyst")
	BlockTerraCatalyst TERRA_CATALYST = new BlockTerraCatalyst();
	
	@RegistryName("gaia_plate")
	BlockGaiaPlate GAIA_PLATE = new BlockGaiaPlate();
	
	@RegistryName("dreaming_pool")
	BlockDreamingPool DREAMING_POOL = new BlockDreamingPool(BlockBehaviour.Properties.of().strength(1.5F));
	
	@RegistryName("elven_altar")
	BlockElvenAltar ELVEN_ALTAR = new BlockElvenAltar(BlockBehaviour.Properties.of().strength(1.5F).requiresCorrectToolForDrops());
	
	@RegistryName("elven_brewery")
	BlockElvenBrewery ELVEN_BREWERY = new BlockElvenBrewery(BlockBehaviour.Properties.of().strength(1.5F).requiresCorrectToolForDrops());
	
	@RegistryName("mana_tesseract")
	BlockManaTesseract MANA_TESSERACT = new BlockManaTesseract();
	
	@RegistryName("elven_fluxfield")
	BlockElvenFluxField ELVEN_FLUX_FIELD = new BlockElvenFluxField(BlockBehaviour.Properties.of().strength(1.5F).requiresCorrectToolForDrops());
	
	@RegistryName("gaiasteel_block")
	BlockStorage GAIASTEEL_BLOCK = new BlockStorage("gaiasteel")
			.withItemProps(props -> props.rarity(ItemsBA.GAIASTEEL_RARITY));
	
	@RegistryName("mana_lapis_block")
	BlockStorage MANA_LAPIS_BLOCK = new BlockStorage(BlockBehaviour.Properties.of().sound(SoundType.STONE), "mana_lapis");
	
	@RegistryName("elven_lapis_block")
	BlockStorage ELVEN_LAPIS_BLOCK = new BlockStorage(BlockBehaviour.Properties.of().sound(SoundType.STONE), "elven_lapis");
	
	@RegistryName("dreamrock")
	SimpleBlockBA DREAMROCK = new SimpleBlockBA(BlockBehaviour.Properties.of().strength(1.5F).requiresCorrectToolForDrops());
	
	@RegistryName("elvenwood_log")
	RotatedPillarBlock ELVENWOOD_LOG = log(MapColor.COLOR_ORANGE, MapColor.STONE);
	
	@RegistryName("elvenwood")
	RotatedPillarBlock ELVENWOOD = new RotatedPillarBlockBA(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOD));
	
	@RegistryName("reduced_sculk_sensor")
	BlockSculkSensorDisabled REDUCED_SCULK_SENSOR = new BlockSculkSensorDisabled(BlockBehaviour.Properties.copy(Blocks.SCULK_SENSOR).lightLevel(s -> 0).emissiveRendering((s, w, p) -> false));
	
	private static RotatedPillarBlock log(MapColor y, MapColor xz)
	{
		return new RotatedPillarBlockBA(
				BlockBehaviour.Properties.of()
						.mapColor((state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? y : xz)
						.strength(2.0F).sound(SoundType.WOOD));
	}
}