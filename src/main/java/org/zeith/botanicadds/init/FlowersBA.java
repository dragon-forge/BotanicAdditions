package org.zeith.botanicadds.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.zeith.botanicadds.blocks.ForgeSpecialFlowerBlockBA;
import org.zeith.botanicadds.blocks.flowers.*;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.function.Supplier;

@SimplyRegister
public interface FlowersBA
{
	BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY);
	BlockBehaviour.Properties FLOATING_PROPS = BotaniaBlocks.FLOATING_PROPS;
	
	// BLOCKS
	
	@RegistryName("wither_flower")
	FlowerBlock WITHER_FLOWER = createSpecialFlowerBlock(MobEffects.WITHER, 200, FLOWER_PROPS, () -> FlowersBA.WITHER_FLOWER_TYPE);
	
	@RegistryName("lightning_flower")
	FlowerBlock LIGHTNING_FLOWER = createSpecialFlowerBlock(MobEffects.DAMAGE_BOOST, 200, FLOWER_PROPS, () -> FlowersBA.LIGHTNING_FLOWER_TYPE);
	
	@RegistryName("rain_flower")
	FlowerBlock RAIN_FLOWER = createSpecialFlowerBlock(MobEffects.WATER_BREATHING, 200, FLOWER_PROPS, () -> FlowersBA.RAIN_FLOWER_TYPE);
	
	@RegistryName("snow_flower")
	FlowerBlock SNOW_FLOWER = createSpecialFlowerBlock(MobEffects.WEAKNESS, 200, FLOWER_PROPS, () -> FlowersBA.SNOW_FLOWER_TYPE);
	
	// TILE ENTITY TYPES
	
	@RegistryName("wither_flower")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<WitherFlower> WITHER_FLOWER_TYPE = BlockEntityType.Builder.of(WitherFlower::new, WITHER_FLOWER).build(null);
	
	@RegistryName("lightning_flower")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<LightningFlower> LIGHTNING_FLOWER_TYPE = BlockEntityType.Builder.of(LightningFlower::new, LIGHTNING_FLOWER).build(null);
	
	@RegistryName("rain_flower")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<RainFlower> RAIN_FLOWER_TYPE = BlockEntityType.Builder.of(RainFlower::new, RAIN_FLOWER).build(null);
	
	@RegistryName("snow_flower")
	@TileRenderer(SpecialFlowerBlockEntityRenderer.class)
	BlockEntityType<SnowFlower> SNOW_FLOWER_TYPE = BlockEntityType.Builder.of(SnowFlower::new, SNOW_FLOWER).build(null);
	
	private static FlowerBlock createSpecialFlowerBlock(MobEffect effect, int effectDuration, BlockBehaviour.Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType)
	{
		return new ForgeSpecialFlowerBlockBA(effect, effectDuration, props, beType);
	}
}