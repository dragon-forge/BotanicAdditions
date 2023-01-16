package org.zeith.botanicadds.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.zeith.botanicadds.blocks.ForgeSpecialFlowerBlockBA;
import org.zeith.botanicadds.tiles.flowers.*;
import org.zeith.botanicadds.blocks.flowers.VibrantiaBlock;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import org.zeith.hammerlib.api.forge.BlockAPI;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.function.Supplier;

@SimplyRegister(prefix = "flowers/")
public interface FlowersBA
{
	BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY);
	BlockBehaviour.Properties FLOATING_PROPS = BotaniaBlocks.FLOATING_PROPS;
	
	// BLOCKS
	
	@RegistryName("necroidus")
	FlowerBlock NECROIDUS = createSpecialFlowerBlock(MobEffects.WITHER, 200, FLOWER_PROPS, () -> FlowersBA.NECROIDUS_TYPE);
	
	@RegistryName("tempestea")
	FlowerBlock TEMPESTEA = createSpecialFlowerBlock(MobEffects.DAMAGE_BOOST, 200, FLOWER_PROPS, () -> FlowersBA.TEMPESTEA_TYPE);
	
	@RegistryName("rainute")
	FlowerBlock RAINUTE = createSpecialFlowerBlock(MobEffects.WATER_BREATHING, 200, FLOWER_PROPS, () -> FlowersBA.RAINUTE_TYPE);
	
	@RegistryName("glaciflora")
	FlowerBlock GLACIFLORA = createSpecialFlowerBlock(MobEffects.WEAKNESS, 200, FLOWER_PROPS, () -> FlowersBA.GLACIFLORA_TYPE);
	
	@RegistryName("vibrantia")
	VibrantiaBlock VIBRANTIA = new VibrantiaBlock(MobEffects.BLINDNESS, 200, FLOWER_PROPS, () -> FlowersBA.VIBRANTIA_TYPE);
	
	// TILE ENTITY TYPES
	
	@RegistryName("necroidus")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Necroidus> NECROIDUS_TYPE = BlockAPI.createBlockEntityType(Necroidus::new, NECROIDUS);
	
	@RegistryName("tempestea")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Tempestea> TEMPESTEA_TYPE = BlockAPI.createBlockEntityType(Tempestea::new, TEMPESTEA);
	
	@RegistryName("rainute")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Rainute> RAINUTE_TYPE = BlockAPI.createBlockEntityType(Rainute::new, RAINUTE);
	
	@RegistryName("glaciflora")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Glaciflora> GLACIFLORA_TYPE = BlockAPI.createBlockEntityType(Glaciflora::new, GLACIFLORA);
	
	@RegistryName("vibrantia")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Vibrantia> VIBRANTIA_TYPE = BlockAPI.createBlockEntityType(Vibrantia::new, VIBRANTIA);
	
	private static FlowerBlock createSpecialFlowerBlock(MobEffect effect, int effectDuration, BlockBehaviour.Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType)
	{
		return new ForgeSpecialFlowerBlockBA(effect, effectDuration, props, beType);
	}
}