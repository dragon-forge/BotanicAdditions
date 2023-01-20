package org.zeith.botanicadds.tiles;

import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.util.SparkUtil;
import org.zeith.hammerlib.api.energy.EnergyUnit;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.util.java.Cast;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class TileElvenFluxField
		extends TileSyncableTickable
		implements ManaReceiver, SparkAttachable, IEnergyStorage
{
	public static final EnergyUnit BOTANIA_MANA = EnergyUnit.getUnit("botania:mana", XplatAbstractions.INSTANCE.isForge() ? 10 : 3);
	
	@NBTSerializable
	public int mana;
	
	@NBTSerializable
	public int maxMana = 5000;
	
	@NBTSerializable
	public final EnergyStorage fe = new EnergyStorage(32_000);
	
	public TileElvenFluxField(BlockPos pos, BlockState state)
	{
		super(TilesBA.ELVEN_FLUX_FIELD, pos, state);
	}
	
	@Override
	public void update()
	{
		if(isOnClient()) return;
		
		if(mana > 0)
		{
			int freeFE = fe.getMaxEnergyStored() - fe.getEnergyStored();
			if(freeFE > BOTANIA_MANA.toFE)
			{
				int availableFE = (int) BOTANIA_MANA.getInFE(mana);
				int receivedFE = fe.receiveEnergy(availableFE, false);
				receiveMana(-(int) BOTANIA_MANA.getFromFE(receivedFE));
			}
		}
		
		if(!isFull())
			SparkUtil.startRequestingMana(this, getAttachedSpark());
		
		int toTransfer = fe.getEnergyStored();
		int unconsumed = XplatAbstractions.INSTANCE.transferEnergyToNeighbors(level, worldPosition, toTransfer);
		if(unconsumed != toTransfer) fe.extractEnergy(toTransfer - unconsumed, false);
	}
	
	@Override
	public Level getManaReceiverLevel()
	{
		return level;
	}
	
	@Override
	public BlockPos getManaReceiverPos()
	{
		return worldPosition;
	}
	
	@Override
	public int getCurrentMana()
	{
		return mana;
	}
	
	@Override
	public boolean isFull()
	{
		// Never overfill it, to prevent any mana loss.
		return mana >= maxMana - 1000;
	}
	
	@Override
	public void receiveMana(int mana)
	{
		this.mana = Mth.clamp(this.mana + mana, 0, maxMana);
	}
	
	@Override
	public boolean canReceiveManaFromBursts()
	{
		return isFull();
	}
	
	@Override
	public boolean canAttachSpark(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getAvailableSpaceForMana()
	{
		return maxMana - mana;
	}
	
	@Override
	public ManaSpark getAttachedSpark()
	{
		List<Entity> sparks = this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition.above(), this.worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(ManaSpark.class));
		if(sparks.size() == 1) return Cast.cast(sparks.get(0));
		else return null;
	}
	
	@Override
	public boolean areIncomingTranfersDone()
	{
		return isFull();
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return fe.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int getEnergyStored()
	{
		return fe.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return fe.getMaxEnergyStored();
	}
	
	@Override
	public boolean canExtract()
	{
		return true;
	}
	
	@Override
	public boolean canReceive()
	{
		return false;
	}
	
	final LazyOptional<?> baseLazy = LazyOptional.of(() -> this);
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == ForgeCapabilities.ENERGY
				|| cap == BotaniaForgeCapabilities.SPARK_ATTACHABLE
				|| cap == BotaniaForgeCapabilities.MANA_RECEIVER)
			return baseLazy.cast();
		return super.getCapability(cap, side);
	}
}