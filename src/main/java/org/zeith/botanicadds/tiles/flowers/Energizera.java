package org.zeith.botanicadds.tiles.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.ConfigsBA;
import org.zeith.botanicadds.api.FlowerHUD;
import org.zeith.botanicadds.init.FlowersBA;
import org.zeith.botanicadds.net.PacketSpawnEnergizeraFX;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.colors.ColorHelper;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.WispParticleData;

@FlowerHUD
public class Energizera
		extends GeneratingFlowerBlockEntity
{
	public static final String TAG_SEARCH_COOLDOWN = "searchcooldown";
	public static final String TAG_COOLDOWN = "cooldown";
	public static final String TAG_SOURCE = "source";
	
	public int searchCooldown;
	public int cooldown, prevCooldown;
	public Direction lastSuccessfulDir;
	public BlockPos lastSuccessfulPos;
	
	public Energizera(BlockPos pos, BlockState state)
	{
		super(FlowersBA.ENERGIZERA_TYPE, pos, state);
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
		if(searchCooldown > 0) --searchCooldown;
		
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
		
		if(level.isClientSide || getMana() >= getMaxMana() || searchCooldown > 0) return;
		
		var cfg = ConfigsBA.INSTANCE.get(LogicalSide.SERVER).gameplay;
		int feRate = cfg.energizeraRate;
		int multiplier = cfg.energizeraMaxPull;
		
		searchCooldown += 3;
		
		if(suckEnergy(feRate, multiplier)) return;
		
		for(int i = 0; i < 12; ++i)
		{
			var rng = level.random;
			var pos = worldPosition.offset(rng.nextInt(-4, 4), rng.nextInt(-2, 2), rng.nextInt(-4, 4));
			if(findStorage(pos, feRate) != null && suckEnergy(feRate, multiplier))
			{
				break;
			}
		}
	}
	
	protected boolean suckEnergy(int feRate, int multiplier)
	{
		IEnergyStorage fe = findStorage(null, feRate);
		if(fe == null) return false;
		
		int canAcceptMana = getMaxMana() - getMana();
		int canAcceptFE = feRate * canAcceptMana;
		int canTakeFE = fe.extractEnergy(canAcceptFE * multiplier, true);
		
		// Remove 5 from 15, resulting in 10.
		canTakeFE -= canTakeFE % feRate;
		
		int addMana = fe.extractEnergy(canTakeFE, false) / feRate;
		
		cooldown += addMana;
		
		sync();
		
		if(lastSuccessfulPos != null && worldPosition != null)
		{
			var start = level.getBlockState(lastSuccessfulPos).getShape(level, lastSuccessfulPos).bounds().move(lastSuccessfulPos).getCenter();
			var end = level.getBlockState(worldPosition).getShape(level, worldPosition).bounds().move(worldPosition).getCenter().add(0, 0.2F, 0);
			Network.sendToTracking(this, new PacketSpawnEnergizeraFX(start, end));
		}
		
		sync();
		
		return true;
	}
	
	public IEnergyStorage findStorage(BlockPos pos, int minEnergy)
	{
		if(pos == null) pos = lastSuccessfulPos;
		if(pos == null || !level.isLoaded(pos)) return null;
		
		var be = level.getBlockEntity(pos);
		if(be == null) return null;
		
		IEnergyStorage fe = null;
		if(lastSuccessfulDir != null)
		{
			fe = be.getCapability(ForgeCapabilities.ENERGY, lastSuccessfulDir).orElse(null);
			if(fe != null && fe.canExtract() && fe.getEnergyStored() >= minEnergy)
			{
				lastSuccessfulPos = pos;
				return fe;
			}
		}
		
		lastSuccessfulPos = null;
		lastSuccessfulDir = null;
		
		for(Direction dir : Direction.values())
		{
			fe = be.getCapability(ForgeCapabilities.ENERGY, dir).orElse(null);
			
			if(fe != null && fe.canExtract() && fe.getEnergyStored() >= minEnergy)
			{
				lastSuccessfulPos = pos;
				lastSuccessfulDir = dir;
				return fe;
			}
		}
		
		return fe;
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
		cmp.putInt(TAG_SEARCH_COOLDOWN, searchCooldown);
		if(lastSuccessfulPos != null) cmp.put(TAG_SOURCE, NbtUtils.writeBlockPos(lastSuccessfulPos));
	}
	
	@Override
	public void readFromPacketNBT(CompoundTag cmp)
	{
		super.readFromPacketNBT(cmp);
		
		cooldown = cmp.getInt(TAG_COOLDOWN);
		searchCooldown = cmp.getInt(TAG_SEARCH_COOLDOWN);
		if(cmp.contains(TAG_SOURCE, Tag.TAG_COMPOUND)) lastSuccessfulPos = NbtUtils.readBlockPos(cmp.getCompound(TAG_SOURCE));
		else lastSuccessfulPos = null;
	}
	
	@Override
	public @Nullable RadiusDescriptor getRadius()
	{
		return RadiusDescriptor.Rectangle.square(worldPosition, 4);
	}
}