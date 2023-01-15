package org.zeith.botanicadds.tiles;

import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.tile.IElvenBrewery;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.mixins.BlockEntityAccessor;
import org.zeith.botanicadds.util.SparkUtil;
import org.zeith.hammerlib.util.java.Cast;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.recipe.BotanicalBreweryRecipe;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.BreweryBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.EntityHelper;

import java.util.List;
import java.util.Optional;

public class TileElvenBrewery
		extends BreweryBlockEntity
		implements SparkAttachable, IElvenBrewery
{
	private static final int CRAFT_EFFECT_EVENT = 0;
	
	protected int manaLastTick = 0;
	
	public TileElvenBrewery(BlockPos pos, BlockState state)
	{
		super(pos, state);
		((BlockEntityAccessor) this).botanicAdditionsSetType(TilesBA.ELVEN_BREWERY);
	}
	
	@Override
	public boolean canAttachSpark(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getAvailableSpaceForMana()
	{
		return Math.max(0, getManaCost() - getCurrentMana());
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
		return !canReceiveManaFromBursts();
	}
	
	@Override
	public void receiveMana(int mana)
	{
		super.receiveMana(mana);
		if(mana != 0) VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}
	
	private final LazyOptional<SparkAttachable> spark = LazyOptional.of(() -> this);
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == BotaniaForgeCapabilities.SPARK_ATTACHABLE) return BotaniaForgeCapabilities.SPARK_ATTACHABLE.orEmpty(cap, spark);
		return super.getCapability(cap, side);
	}
	
	protected void findRecipeElven()
	{
		Optional<BotanicalBreweryRecipe> maybeRecipe = level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.BREW_TYPE, getItemHandler(), level);
		maybeRecipe.ifPresent(recipeBrew ->
		{
			this.recipe = recipeBrew;
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, true));
		});
	}
	
	public static void commonTickElven(Level level, BlockPos worldPosition, BlockState state, TileElvenBrewery self)
	{
		if(self.getCurrentMana() > 0 && self.recipe == null)
		{
			self.findRecipeElven();
			
			if(self.recipe == null)
			{
				self.receiveMana(-self.getCurrentMana());
			}
		}
		
		// Update every tick.
		self.receiveMana(0);
		
		if(!level.isClientSide && self.recipe == null)
		{
			List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1));
			for(ItemEntity item : items)
			{
				if(item.isAlive() && !item.getItem().isEmpty())
				{
					ItemStack stack = item.getItem();
					if(self.addItem(null, stack, null))
					{
						EntityHelper.syncItem(item);
					}
				}
			}
		}
		
		if(self.recipe != null)
		{
			if(!self.recipe.matches(self.getItemHandler(), level))
			{
				self.recipe = null;
				level.setBlockAndUpdate(worldPosition, self.getBlockState().getBlock().defaultBlockState());
			}
			
			if(self.recipe != null)
			{
				if(self.getCurrentMana() != self.manaLastTick)
				{
					int color = self.recipe.getBrew().getColor(self.getItemHandler().getItem(0));
					float r = (color >> 16 & 0xFF) / 255F;
					float g = (color >> 8 & 0xFF) / 255F;
					float b = (color & 0xFF) / 255F;
					for(int i = 0; i < 5; i++)
					{
						WispParticleData data1 = WispParticleData.wisp(0.1F + (float) Math.random() * 0.05F, r, g, b);
						level.addParticle(data1, worldPosition.getX() + 0.7 - Math.random() * 0.4, worldPosition.getY() + 0.9 - Math.random() * 0.2, worldPosition.getZ() + 0.7 - Math.random() * 0.4, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						for(int j = 0; j < 2; j++)
						{
							WispParticleData data = WispParticleData.wisp(0.1F + (float) Math.random() * 0.2F, 0.2F, 0.2F, 0.2F);
							level.addParticle(data, worldPosition.getX() + 0.7 - Math.random() * 0.4, worldPosition.getY() + 0.9 - Math.random() * 0.2, worldPosition.getZ() + 0.7 - Math.random() * 0.4, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						}
					}
				}
				
				if(self.getCurrentMana() >= self.getManaCost() && !level.isClientSide)
				{
					int mana = self.getManaCost();
					self.receiveMana(-mana);
					
					ItemStack output = self.recipe.getOutput(self.getItemHandler().getItem(0));
					ItemEntity outputItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, output);
					level.addFreshEntity(outputItem);
					level.blockEvent(worldPosition, self.getBlockState().getBlock(), CRAFT_EFFECT_EVENT, self.recipe.getBrew().getColor(output));
					
					for(int i = 0; i < self.inventorySize(); i++)
					{
						self.getItemHandler().setItem(i, ItemStack.EMPTY);
					}
				}
			}
		}
		
		int newSignal = 0;
		if(self.recipe != null)
		{
			newSignal++;
		}
		
		if(newSignal != self.signal)
		{
			self.signal = newSignal;
			level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
		}
		
		self.manaLastTick = self.getCurrentMana();
		
		if(self.getManaCost() > 0) SparkUtil.startRequestingMana(self, self.getAttachedSpark());
	}
}
