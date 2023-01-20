package org.zeith.botanicadds.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.zeith.botanicadds.blocks.flowers.*;
import org.zeith.botanicadds.tiles.flowers.*;
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
	
	// Functional flowers
	
	
	@RegistryName("necroidus")
	FlowerBlock NECROIDUS = createFunctionalFlowerBlock(MobEffects.WITHER, 200, FLOWER_PROPS, () -> FlowersBA.NECROIDUS_TYPE);
	@RegistryName("floating/necroidus")
	FloatingSpecialFlowerBlockBA NECROIDUS_FLOATING = new FloatingSpecialFlowerBlockBA(FLOATING_PROPS, () -> FlowersBA.NECROIDUS, () -> FlowersBA.NECROIDUS_TYPE);
	
	
	@RegistryName("apicaria")
	FlowerBlock APICARIA = createFunctionalFlowerBlock(MobEffects.POISON, 100, FLOWER_PROPS, () -> FlowersBA.APICARIA_TYPE);
	@RegistryName("floating/apicaria")
	FloatingSpecialFlowerBlockBA APICARIA_FLOATING = new FloatingSpecialFlowerBlockBA(FLOATING_PROPS, () -> FlowersBA.APICARIA, () -> FlowersBA.APICARIA_TYPE);
	
	
	// Generating flowers
	
	@RegistryName("tempestea")
	FlowerBlock TEMPESTEA = createGeneratingFlowerBlock(MobEffects.DAMAGE_BOOST, 200, FLOWER_PROPS, () -> FlowersBA.TEMPESTEA_TYPE);
	@RegistryName("floating/tempestea")
	FloatingSpecialFlowerBlockBA TEMPESTEA_FLOATING = new FloatingSpecialFlowerBlockBA(FLOATING_PROPS, () -> FlowersBA.TEMPESTEA, () -> FlowersBA.TEMPESTEA_TYPE);
	
	@RegistryName("rainute")
	FlowerBlock RAINUTE = createGeneratingFlowerBlock(MobEffects.WATER_BREATHING, 200, FLOWER_PROPS, () -> FlowersBA.RAINUTE_TYPE);
	@RegistryName("floating/rainute")
	FloatingSpecialFlowerBlockBA RAINUTE_FLOATING = new FloatingSpecialFlowerBlockBA(FLOATING_PROPS, () -> FlowersBA.RAINUTE, () -> FlowersBA.RAINUTE_TYPE);
	
	@RegistryName("glaciflora")
	FlowerBlock GLACIFLORA = createGeneratingFlowerBlock(MobEffects.WEAKNESS, 200, FLOWER_PROPS, () -> FlowersBA.GLACIFLORA_TYPE);
	@RegistryName("floating/glaciflora")
	FloatingSpecialFlowerBlockBA GLACIFLORA_FLOATING = new FloatingSpecialFlowerBlockBA(FLOATING_PROPS, () -> FlowersBA.GLACIFLORA, () -> FlowersBA.GLACIFLORA_TYPE);
	
	@RegistryName("vibrantia")
	VibrantiaBlock VIBRANTIA = new VibrantiaBlock(MobEffects.BLINDNESS, 200, FLOWER_PROPS, () -> FlowersBA.VIBRANTIA_TYPE);
	@RegistryName("floating/vibrantia")
	FloatingVibrantiaBlock VIBRANTIA_FLOATING = new FloatingVibrantiaBlock(FLOATING_PROPS, () -> FlowersBA.VIBRANTIA, () -> FlowersBA.VIBRANTIA_TYPE);
	
	@RegistryName("energizera")
	FlowerBlock ENERGIZERA = createGeneratingFlowerBlock(MobEffects.HEALTH_BOOST, 200, FLOWER_PROPS, () -> FlowersBA.ENERGIZERA_TYPE);
	@RegistryName("floating/energizera")
	FloatingSpecialFlowerBlockBA ENERGIZERA_FLOATING = new FloatingSpecialFlowerBlockBA(FLOATING_PROPS, () -> FlowersBA.ENERGIZERA, () -> FlowersBA.ENERGIZERA_TYPE);
	
	// TILE ENTITY TYPES
	
	// Functional
	
	@RegistryName("necroidus")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Necroidus> NECROIDUS_TYPE = BlockAPI.createBlockEntityType(Necroidus::new, NECROIDUS, NECROIDUS_FLOATING);
	
	@RegistryName("apicaria")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Apicaria> APICARIA_TYPE = BlockAPI.createBlockEntityType(Apicaria::new, APICARIA, APICARIA_FLOATING);
	
	// Generating
	
	@RegistryName("tempestea")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Tempestea> TEMPESTEA_TYPE = BlockAPI.createBlockEntityType(Tempestea::new, TEMPESTEA, TEMPESTEA_FLOATING);
	
	@RegistryName("rainute")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Rainute> RAINUTE_TYPE = BlockAPI.createBlockEntityType(Rainute::new, RAINUTE, RAINUTE_FLOATING);
	
	@RegistryName("glaciflora")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Glaciflora> GLACIFLORA_TYPE = BlockAPI.createBlockEntityType(Glaciflora::new, GLACIFLORA, GLACIFLORA_FLOATING);
	
	@RegistryName("vibrantia")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Vibrantia> VIBRANTIA_TYPE = BlockAPI.createBlockEntityType(Vibrantia::new, VIBRANTIA, VIBRANTIA_FLOATING);
	
	@RegistryName("energizera")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<Energizera> ENERGIZERA_TYPE = BlockAPI.createBlockEntityType(Energizera::new, ENERGIZERA, ENERGIZERA_FLOATING);
	
	private static FlowerBlock createFunctionalFlowerBlock(MobEffect effect, int effectDuration, BlockBehaviour.Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType)
	{
		return new ForgeSpecialFlowerBlockBA(ForgeSpecialFlowerBlockBA.FlowerKind.FUNCTIONAL, effect, effectDuration, props, beType);
	}
	
	private static FlowerBlock createGeneratingFlowerBlock(MobEffect effect, int effectDuration, BlockBehaviour.Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType)
	{
		return new ForgeSpecialFlowerBlockBA(ForgeSpecialFlowerBlockBA.FlowerKind.GENERATING, effect, effectDuration, props, beType);
	}
}