package org.zeith.botanicadds.tiles;

import com.google.common.base.Predicates;
import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.crafting.RecipeGaiaPlate;
import org.zeith.botanicadds.init.*;
import org.zeith.botanicadds.util.SparkUtil;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.properties.PropertyInt;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;
import java.util.function.Supplier;

public class TileGaiaPlate
		extends TileSyncableTickable
		implements SparkAttachable, ManaReceiver
{
	public static final Supplier<IMultiblock> MULTIBLOCK = Suppliers.memoize(() ->
			PatchouliAPI.get().makeMultiblock(new String[][] {
							{
									"___",
									"_P_",
									"___"
							},
							{
									"RLR",
									"L0L",
									"RLR"
							}
					},
					'P', BlocksBA.GAIA_PLATE,
					'R', PatchouliAPI.get().strictBlockMatcher(BlocksBA.DREAMROCK),
					'0', PatchouliAPI.get().strictBlockMatcher(BlocksBA.DREAMROCK),
					'L', PatchouliAPI.get().tagMatcher(BlocksBA.ELVEN_LAPIS_BLOCK.blockTag)
			));
	
	@NBTSerializable
	private int mana;
	
	public final PropertyInt currentMana = new PropertyInt(DirectStorage.create(v -> mana = v, () -> mana));
	
	public TileGaiaPlate(BlockPos pos, BlockState state)
	{
		super(TilesBA.GAIA_PLATE, pos, state);
		dispatcher.registerProperty("mana", currentMana);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(isOnServer())
		{
			boolean removeMana = true;
			if(hasValidPlatform())
			{
				List<ItemStack> items = getItems();
				SimpleContainer inv = getInventory();
				RecipeGaiaPlate recipe = getCurrentRecipe(inv);
				if(recipe != null)
				{
					removeMana = false;
					ManaSpark spark = getAttachedSpark();
					SparkUtil.startRequestingMana(this, spark);
					
					if(mana > 0)
					{
						VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
						int proportion = Float.floatToIntBits(this.getCompletion());
						XplatAbstractions.INSTANCE.sendToNear(level, worldPosition, new BotaniaEffectPacket(EffectType.TERRA_PLATE, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), proportion));
					}
					
					if(mana >= recipe.getMana())
					{
						ItemStack result = recipe.assemble(inv);
						
						for(ItemStack item : items)
						{
							item.setCount(0);
						}
						
						ItemEntity item = new ItemEntity(level, (double) worldPosition.getX() + 0.5, (double) worldPosition.getY() + 0.2, (double) worldPosition.getZ() + 0.5, result);
						item.setDeltaMovement(Vec3.ZERO);
						level.addFreshEntity(item);
						level.playSound(null, item.getX(), item.getY(), item.getZ(), BotaniaSounds.terrasteelCraft, SoundSource.BLOCKS, 1.0F, 1.0F);
						mana = 0;
						level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
						VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
					}
				}
			}
			
			if(removeMana)
			{
				receiveMana(-1000);
			}
		}
	}
	
	private List<ItemStack> getItems()
	{
		List<ItemEntity> itemEntities = this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition, this.worldPosition.offset(1, 1, 1)), EntitySelector.ENTITY_STILL_ALIVE);
		List<ItemStack> stacks = new ArrayList();
		Iterator var3 = itemEntities.iterator();
		
		while(var3.hasNext())
		{
			ItemEntity entity = (ItemEntity) var3.next();
			if(!entity.getItem().isEmpty())
			{
				stacks.add(entity.getItem());
			}
		}
		
		return stacks;
	}
	
	private SimpleContainer getInventory()
	{
		List<ItemStack> items = this.getItems();
		return new SimpleContainer(flattenStacks(items));
	}
	
	private static ItemStack[] flattenStacks(List<ItemStack> items)
	{
		int i = 0;
		
		{
			ItemStack item;
			for(Iterator<ItemStack> var3 = items.iterator(); var3.hasNext(); i += item.getCount())
				item = var3.next();
		}
		
		if(i > 64)
		{
			return new ItemStack[0];
		} else
		{
			ItemStack[] stacks = new ItemStack[i];
			int j = 0;
			
			for(ItemStack item : items)
			{
				if(item.getCount() > 1)
				{
					ItemStack temp = item.copy();
					temp.setCount(1);
					
					for(int count = 0; count < item.getCount(); ++count)
					{
						stacks[j] = temp.copy();
						++j;
					}
				} else
				{
					stacks[j] = item;
					++j;
				}
			}
			
			return stacks;
		}
	}
	
	private @Nullable RecipeGaiaPlate getCurrentRecipe(SimpleContainer items)
	{
		return items.isEmpty() ? null : this.level.getRecipeManager().getRecipeFor(RecipeTypesBA.GAIA_PLATE, items, this.level).orElse(null);
	}
	
	private boolean isActive()
	{
		return this.getCurrentRecipe(this.getInventory()) != null;
	}
	
	private boolean hasValidPlatform()
	{
		return MULTIBLOCK.get().validate(this.level, this.getBlockPos().below()) != null;
	}
	
	@Override
	public Level getManaReceiverLevel()
	{
		return this.getLevel();
	}
	
	@Override
	public BlockPos getManaReceiverPos()
	{
		return this.getBlockPos();
	}
	
	@Override
	public int getCurrentMana()
	{
		return this.mana;
	}
	
	@Override
	public boolean isFull()
	{
		RecipeGaiaPlate recipe = this.getCurrentRecipe(this.getInventory());
		return recipe == null || this.getCurrentMana() >= recipe.getMana();
	}
	
	@Override
	public void receiveMana(int mana)
	{
		this.mana = Math.max(0, this.mana + mana);
		this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
	}
	
	@Override
	public boolean canReceiveManaFromBursts()
	{
		return this.isActive();
	}
	
	@Override
	public boolean canAttachSpark(ItemStack stack)
	{
		return true;
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
		return !this.isActive();
	}
	
	@Override
	public int getAvailableSpaceForMana()
	{
		RecipeGaiaPlate recipe = this.getCurrentRecipe(this.getInventory());
		return recipe == null ? 0 : Math.max(0, recipe.getMana() - this.getCurrentMana());
	}
	
	public float getCompletion()
	{
		RecipeGaiaPlate recipe = this.getCurrentRecipe(this.getInventory());
		return recipe == null ? 0.0F : (float) this.getCurrentMana() / (float) recipe.getMana();
	}
	
	public int getComparatorLevel()
	{
		int val = (int) ((double) this.getCompletion() * 15.0);
		if(this.getCurrentMana() > 0)
		{
			val = Math.max(val, 1);
		}
		
		return val;
	}
	
	private final LazyOptional<SparkAttachable> sparkLazy = LazyOptional.of(() -> this);
	private final LazyOptional<ManaReceiver> receiverLazy = LazyOptional.of(() -> this);
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == BotaniaForgeCapabilities.SPARK_ATTACHABLE) return BotaniaForgeCapabilities.SPARK_ATTACHABLE.orEmpty(cap, sparkLazy);
		if(cap == BotaniaForgeCapabilities.MANA_RECEIVER) return BotaniaForgeCapabilities.MANA_RECEIVER.orEmpty(cap, receiverLazy);
		return super.getCapability(cap, side);
	}
}