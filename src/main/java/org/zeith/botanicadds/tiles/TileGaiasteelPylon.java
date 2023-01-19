package org.zeith.botanicadds.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.zeith.botanicadds.api.tile.IElvenGatewayPylonTile;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.tiles.TileSyncable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.Random;

public class TileGaiasteelPylon
		extends TileSyncable
		implements IElvenGatewayPylonTile
{
	@NBTSerializable
	boolean activated = false;
	@NBTSerializable
	BlockPos centerPos;
	private int ticks = 0;
	
	public TileGaiasteelPylon(BlockPos pos, BlockState state)
	{
		super(TilesBA.GAIASTEEL_PYLON, pos, state);
	}
	
	public static float dither(float base, float by)
	{
		return Mth.clamp(base + ((float) Math.random() - (float) Math.random()) * by, 0F, 1F);
	}
	
	public static void commonTick(Level level, BlockPos pos, BlockState state, TileGaiasteelPylon self)
	{
		++self.ticks;
		
		var rgb = ItemsBA.GAIASTEEL_COLOR;
		
		if(self.activated && level.isClientSide)
		{
			if(self.portalOff() || !(level.getBlockEntity(pos.below()) instanceof ManaPoolBlockEntity))
			{
				self.activated = false;
				return;
			}
			
			Vec3 centerBlock = new Vec3(self.centerPos.getX() + 0.5, self.centerPos.getY() + 0.75 + (Math.random() - 0.5 * 0.25), self.centerPos.getZ() + 0.5);
			
			if(BotaniaConfig.client().elfPortalParticlesEnabled())
			{
				double worldTime = self.ticks;
				worldTime += new Random(pos.hashCode()).nextInt(1000);
				worldTime /= 5;
				
				float r = 0.75F + (float) Math.random() * 0.05F;
				double x = pos.getX() + 0.5 + Math.cos(worldTime) * r;
				double z = pos.getZ() + 0.5 + Math.sin(worldTime) * r;
				
				Vec3 ourCoords = new Vec3(x, pos.getY() + 0.25, z);
				centerBlock = centerBlock.subtract(0, 0.5, 0);
				Vec3 mov = centerBlock.subtract(ourCoords).normalize().scale(0.2);
				
				WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F,
						dither(rgb.getRed() / 255F, 0.15F), dither(rgb.getGreen() / 255F, 0.15F), dither(rgb.getBlue() / 255F, 0.15F),
						1
				);
				level.addParticle(data, x, pos.getY() + 0.25, z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
				
				if(level.random.nextInt(3) == 0)
				{
					data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F,
							dither(rgb.getRed() / 255F, 0.15F), dither(rgb.getGreen() / 255F, 0.15F), dither(rgb.getBlue() / 255F, 0.15F));
					level.addParticle(data, x, pos.getY() + 0.25, z, (float) mov.x, (float) mov.y, (float) mov.z);
				}
			}
		}
		
		if(level.random.nextBoolean() && level.isClientSide)
		{
			SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(),
					rgb.getRed() / 255F, rgb.getGreen() / 255F, rgb.getBlue() / 255F,
					2
			);
			level.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random() * 1.5, pos.getZ() + Math.random(), 0, 0, 0);
		}
	}
	
	private boolean portalOff()
	{
		return !level.getBlockState(centerPos).is(BotaniaBlocks.alfPortal)
				|| level.getBlockState(centerPos).getValue(BotaniaStateProperties.ALFPORTAL_STATE) == AlfheimPortalState.OFF;
	}
	
	@Override
	public float getManaCostMultiplier()
	{
		return 0.25F;
	}
	
	@Override
	public void activate(BlockPos corePos)
	{
		activated = true;
		centerPos = corePos;
	}
	
	@Override
	public void deactivate()
	{
		activated = false;
	}
}