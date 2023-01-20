package org.zeith.botanicadds.tiles.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.GenerationalFlowerHUD;
import org.zeith.botanicadds.init.FlowersBA;
import org.zeith.botanicadds.net.PacketSpawnEnergizeraFX;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.colors.ColorHelper;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.WispParticleData;

import static org.zeith.botanicadds.tiles.TileElvenFluxField.BOTANIA_MANA;

@GenerationalFlowerHUD
public class Energizera
		extends GeneratingFlowerBlockEntity
{
	public static final String TAG_COOLDOWN = "cooldown";
	public static final String TAG_SOURCE = "source";
	
	public int cooldown, prevCooldown;
	public BlockPos lastSuccessfulPos;
	
	public Energizera(BlockPos pos, BlockState state)
	{
		super(FlowersBA.ENERGIZERA_TYPE, pos, state);
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
		if(cooldown > 0)
		{
			int reduce = Math.min(2, cooldown);
			
			cooldown -= reduce;
			
			if(!level.isClientSide)
			{
				addMana(reduce);
				sync();
			}
			
			var rgb = getColor();
			WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb), 1);
			emitParticle(data, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
			
			prevCooldown = cooldown;
			return;
		}
		
		if(!level.isClientSide && getMana() < getMaxMana()) for(int i = 0; i < 8; ++i)
		{
			var rng = level.random;
			var pos = lastSuccessfulPos != null ? lastSuccessfulPos : worldPosition.offset(rng.nextInt(-4, 4), rng.nextInt(-2, 2), rng.nextInt(-4, 4));
			var be = level.getBlockEntity(pos);
			if(be != null)
			{
				IEnergyStorage fe = null;
				
				for(Direction dir : Direction.values())
				{
					var fe0 = be.getCapability(ForgeCapabilities.ENERGY, dir).orElse(null);
					if(fe0 != null && fe0.canExtract() && fe0.getEnergyStored() >= BOTANIA_MANA.toFE)
					{
						fe = fe0;
						lastSuccessfulPos = pos;
						break;
					}
				}
				
				if(fe != null)
				{
					int canAcceptMana = getMaxMana() - getMana();
					int canAcceptFE = (int) BOTANIA_MANA.getInFE(canAcceptMana);
					int canTakeFE = fe.extractEnergy(canAcceptFE, true);
					
					// Remove 5 from 15, resulting in 10.
					canTakeFE -= canTakeFE % BOTANIA_MANA.toFE;
					
					int addMana = (int) BOTANIA_MANA.getFromFE(canTakeFE);
					
					fe.extractEnergy(canAcceptFE, false);
					
					cooldown += addMana;
					
					var start = level.getBlockState(lastSuccessfulPos).getShape(level, lastSuccessfulPos).bounds().move(lastSuccessfulPos).getCenter();
					var end = level.getBlockState(worldPosition).getShape(level, worldPosition).bounds().move(worldPosition).getCenter().add(0, 0.2F, 0);
					Network.sendToTracking(this, new PacketSpawnEnergizeraFX(start, end));
					
					sync();
					break;
				} else if(lastSuccessfulPos == pos)
				{
					lastSuccessfulPos = null;
				}
			} else if(lastSuccessfulPos == pos)
			{
				lastSuccessfulPos = null;
			}
		}
	}
	
	@Override
	public int getMaxMana()
	{
		return 100;
	}
	
	@Override
	public int getColor()
	{
		return 0xEF4040;
	}
	
	@Override
	public void writeToPacketNBT(CompoundTag cmp)
	{
		super.writeToPacketNBT(cmp);
		
		cmp.putInt(TAG_COOLDOWN, cooldown);
		if(lastSuccessfulPos != null) cmp.put(TAG_SOURCE, NbtUtils.writeBlockPos(lastSuccessfulPos));
	}
	
	@Override
	public void readFromPacketNBT(CompoundTag cmp)
	{
		super.readFromPacketNBT(cmp);
		
		cooldown = cmp.getInt(TAG_COOLDOWN);
		if(cmp.contains(TAG_SOURCE, Tag.TAG_COMPOUND)) lastSuccessfulPos = NbtUtils.readBlockPos(cmp.getCompound(TAG_SOURCE));
		else lastSuccessfulPos = null;
	}
	
	@Override
	public @Nullable RadiusDescriptor getRadius()
	{
		return RadiusDescriptor.Rectangle.square(worldPosition, 4);
	}
}