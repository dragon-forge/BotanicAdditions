package org.zeith.botanicadds.init;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.zeith.botanicadds.blocks.BlockDecorativeMetal;
import org.zeith.botanicadds.blocks.BlockTerraCatalyst;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface BlocksBA
{
	@RegistryName("gaiasteel_block")
	BlockDecorativeMetal GAIASTEEL_BLOCK = new BlockDecorativeMetal("gaiasteel");
	
	@RegistryName("terra_catalyst")
	BlockTerraCatalyst TERRA_CATALYST = new BlockTerraCatalyst();
}