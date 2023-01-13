package org.zeith.botanicadds.init;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.zeith.botanicadds.blocks.*;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface BlocksBA
{
	@RegistryName("gaiasteel_block")
	BlockStorage GAIASTEEL_BLOCK = new BlockStorage("gaiasteel");
	
	@RegistryName("mana_lapis_block")
	BlockStorage MANA_LAPIS_BLOCK = new BlockStorage("mana_lapis");
	
	@RegistryName("elven_lapis_block")
	BlockStorage ELVEN_LAPIS_BLOCK = new BlockStorage("elven_lapis");
	
	@RegistryName("terra_catalyst")
	BlockTerraCatalyst TERRA_CATALYST = new BlockTerraCatalyst();
	
	@RegistryName("gaia_plate")
	BlockGaiaPlate GAIA_PLATE = new BlockGaiaPlate();
	
	@RegistryName("dreamrock")
	SimpleBlockBA DREAMROCK = new SimpleBlockBA(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F).requiresCorrectToolForDrops());
	
	@RegistryName("elvenwood_log")
	RotatedPillarBlock ELVENWOOD_LOG = log(MaterialColor.COLOR_ORANGE, MaterialColor.STONE);
	
	@RegistryName("elvenwood")
	RotatedPillarBlock ELVENWOOD = new RotatedPillarBlockBA(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOD));
	
	private static RotatedPillarBlock log(MaterialColor p_50789_, MaterialColor p_50790_)
	{
		return new RotatedPillarBlockBA(BlockBehaviour.Properties.of(Material.WOOD, (p_152624_) -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? p_50789_ : p_50790_).strength(2.0F).sound(SoundType.WOOD));
	}
}