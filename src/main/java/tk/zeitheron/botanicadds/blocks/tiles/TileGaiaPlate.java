package tk.zeitheron.botanicadds.blocks.tiles;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.net.HCNet;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.IItemHandler;
import tk.zeitheron.botanicadds.api.GaiaPlateRecipes;
import tk.zeitheron.botanicadds.init.BlocksBA;
import tk.zeitheron.botanicadds.net.PacketGaiaPlateEffect;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ModSounds;

import java.util.List;

public class TileGaiaPlate
		extends TileMod
		implements ISparkAttachable, ITickable, IItemHandler
{
	private static final BlockPos[] LAPIS_BLOCKS = {
			new BlockPos(1, 0, 0),
			new BlockPos(-1, 0, 0),
			new BlockPos(0, 0, 1),
			new BlockPos(0, 0, -1)
	};
	private static final BlockPos[] LIVINGROCK_BLOCKS = {
			new BlockPos(0, 0, 0),
			new BlockPos(1, 0, 1),
			new BlockPos(1, 0, -1),
			new BlockPos(-1, 0, 1),
			new BlockPos(-1, 0, -1)
	};

	private static final String TAG_MANA = "mana";

	public GaiaPlateRecipes.RecipeGaiaPlate recipe;
	public int mana, maxMana;

	public static MultiblockSet makeMultiblockSet()
	{
		Multiblock mb = new Multiblock();

		for(BlockPos relativePos : LAPIS_BLOCKS)
			mb.addComponent(relativePos, BlocksBA.ELVEN_LAPIS_BLOCK.getDefaultState());
		for(BlockPos relativePos : LIVINGROCK_BLOCKS)
			mb.addComponent(relativePos, BlocksBA.DREAMROCK.getDefaultState());

		mb.addComponent(new BlockPos(0, 1, 0), BlocksBA.GAIA_PLATE.getDefaultState());
		mb.setRenderOffset(new BlockPos(0, 1, 0));

		return mb.makeSet();
	}

	List<EntityItem> groundItems;

	@Override
	public void update()
	{
		if(world.isRemote)
			return;

		groundItems = getItems();
		recipe = null;

		if(recipe != null)
			maxMana = recipe.getMana();
		else if((recipe = areItemsValid(groundItems)) != null)
			maxMana = recipe.getMana();

		boolean removeMana = true;

		if(hasValidPlatform())
		{
			if(recipe != null)
			{
				removeMana = false;
				ISparkEntity spark = getAttachedSpark();
				if(spark != null)
				{
					List<ISparkEntity> sparkEntities = SparkHelper.getSparksAround(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
					for(ISparkEntity otherSpark : sparkEntities)
					{
						if(spark == otherSpark)
							continue;

						if(otherSpark.getAttachedTile() != null && otherSpark.getAttachedTile() instanceof IManaPool)
							otherSpark.registerTransfer(spark);
					}
				}

				if(mana > 0)
				{
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);

					if(world instanceof WorldServer)
					{
						WorldServer ws = (WorldServer) world;

						PacketGaiaPlateEffect packet = new PacketGaiaPlateEffect();
						packet.setPos(getPos());

						for(EntityPlayer player : ws.playerEntities)
						{
							EntityPlayerMP playerMP = (EntityPlayerMP) player;
							if(playerMP.getDistanceSq(pos) < 64 * 64 && ws.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, pos.getX() >> 4, pos.getZ() >> 4))
								HCNet.INSTANCE.sendTo(packet, playerMP);
						}
					}
				}

				if(mana >= maxMana)
				{
					List<EntityItem> items = getItems();
					EntityItem item = items.get(0);
					for(EntityItem otherItem : items)
						if(otherItem != item)
							otherItem.setDead();
						else
							item.setItem(recipe.getOutput().copy());
					world.playSound(null, item.posX, item.posY, item.posZ, ModSounds.terrasteelCraft, SoundCategory.BLOCKS, 1, 1);
					mana = 0;
					world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
				}
			}
		}

		if(removeMana)
			recieveMana(-1000);
	}

	List<EntityItem> getItems()
	{
		return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
	}

	GaiaPlateRecipes.RecipeGaiaPlate areItemsValid(List<EntityItem> items)
	{
		for(GaiaPlateRecipes.RecipeGaiaPlate rgp : GaiaPlateRecipes.gaiaRecipes)
			if(rgp.matches(this))
				return rgp;
		return null;
	}

	boolean hasValidPlatform()
	{
		return checkAll(LAPIS_BLOCKS, BlocksBA.ELVEN_LAPIS_BLOCK) && checkAll(LIVINGROCK_BLOCKS, BlocksBA.DREAMROCK);
	}

	boolean checkAll(BlockPos[] relPositions, Block block)
	{
		for(BlockPos position : relPositions)
			if(!checkPlatform(position.getX(), position.getZ(), block))
				return false;
		return true;
	}

	boolean checkPlatform(int xOff, int zOff, Block block)
	{
		return world.getBlockState(pos.add(xOff, -1, zOff)).getBlock() == block;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp)
	{
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger("maxMana", maxMana);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp)
	{
		mana = cmp.getInteger(TAG_MANA);
		maxMana = cmp.getInteger("maxMana");
	}

	@Override
	public int getCurrentMana()
	{
		return mana;
	}

	@Override
	public boolean isFull()
	{
		return mana >= maxMana;
	}

	@Override
	public void recieveMana(int mana)
	{
		this.mana = Math.max(0, Math.min(maxMana, this.mana + mana));
		world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}

	@Override
	public boolean canRecieveManaFromBursts()
	{
		return recipe != null;
	}

	@Override
	public boolean canAttachSpark(ItemStack stack)
	{
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity)
	{
	}

	@Override
	public ISparkEntity getAttachedSpark()
	{
		List<Entity> sparks = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if(sparks.size() == 1)
		{
			Entity e = sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone()
	{
		return !canRecieveManaFromBursts();
	}

	@Override
	public int getAvailableSpaceForMana()
	{
		return Math.max(0, maxMana - getCurrentMana());
	}

	@Override
	public int getSlots()
	{
		return groundItems.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return groundItems.get(slot).getItem();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}
}