package tk.zeitheron.botanicadds.blocks.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Predicates;
import tk.zeitheron.botanicadds.init.BlocksBA;
import tk.zeitheron.botanicadds.utils.ILastManaAknowledgedTile;
import com.zeitheron.hammercore.tile.TileSyncableTickable;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class TileManaTesseract extends TileSyncableTickable implements ISparkAttachable, IManaPool, ILastManaAknowledgedTile
{
	public static final Map<UUID, Int2ObjectMap<List<TileManaTesseract>>> TESSERACTS = new HashMap<>();
	
	public static void addTesseract(TileManaTesseract tile)
	{
		List<TileManaTesseract> l = getTesseracts(tile);
		if(!l.contains(tile))
			l.add(tile);
	}
	
	public static void removeTesseract(TileManaTesseract tile)
	{
		List<TileManaTesseract> l = getTesseracts(tile);
		if(l.contains(tile))
		{
			l.add(tile);
			if(l.isEmpty())
			{
				Int2ObjectMap<List<TileManaTesseract>> channels = TESSERACTS.get(tile.owner);
				if(channels == null)
					TESSERACTS.put(tile.owner, channels = new Int2ObjectArrayMap<>());
				channels.remove(tile.channel);
			}
		}
	}
	
	public static List<TileManaTesseract> getTesseracts(TileManaTesseract tile)
	{
		// Prevent client-side bothering
		if(tile.world.isRemote)
			return new ArrayList<>();
		
		Int2ObjectMap<List<TileManaTesseract>> channels = TESSERACTS.get(tile.owner);
		if(channels == null)
			TESSERACTS.put(tile.owner, channels = new Int2ObjectArrayMap<>());
		List<TileManaTesseract> l = channels.get(tile.channel);
		if(l == null)
			channels.put(tile.channel, l = new ArrayList<>());
		return l;
	}
	
	public static final int MANA_CAP = 50000;
	
	public int mana;
	public EnumDyeColor color = EnumDyeColor.WHITE;
	public UUID owner = UUID.randomUUID();
	
	public int channel;
	public int lastKnownMana = -1;
	
	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}
	
	public void setLink(int channel)
	{
		this.channel = channel;
	}
	
	public static int computeHash(ItemStack stack)
	{
		return stack.copy().splitStack(1).serializeNBT().hashCode();
	}
	
	@Override
	public void tick()
	{
		if(!ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid())
		{
			ManaNetworkEvent.addPool(this);
			addTesseract(this);
		}
		
		// Share mana
		if(atTickRate(20) && !world.isRemote)
		{
			List<TileManaTesseract> tesses = getTesseracts(this);
			tesses.removeIf(t -> t.isInvalid());
			if(!tesses.isEmpty() && tesses.get(0) == this)
			{
				long mana = 0L;
				int pools = tesses.size();
				for(TileManaTesseract t : tesses)
					mana += t.mana;
				int per = (int) (mana / pools);
				for(TileManaTesseract t : tesses)
				{
					t.mana = per;
					if(t.mana != per)
						t.sendChangesToNearby();
				}
				this.mana += mana - per * pools;
			}
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Mana", mana);
		nbt.setByte("Color", (byte) color.getMetadata());
		nbt.setUniqueId("Owner", owner);
		nbt.setInteger("Channel", channel);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		mana = nbt.getInteger("Mana");
		color = EnumDyeColor.byDyeDamage(nbt.getByte("Color"));
		owner = nbt.getUniqueId("Owner");
		channel = nbt.getInteger("Channel");
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		ManaNetworkEvent.removePool(this);
		removeTesseract(this);
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		ManaNetworkEvent.removePool(this);
		removeTesseract(this);
	}
	
	@Override
	public boolean isFull()
	{
		return mana >= MANA_CAP;
	}
	
	@Override
	public void recieveMana(int mana)
	{
		this.mana = Math.min(this.mana + mana, MANA_CAP);
	}
	
	@Override
	public boolean canRecieveManaFromBursts()
	{
		return mana < MANA_CAP;
	}
	
	@Override
	public int getCurrentMana()
	{
		return mana;
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
			return (ISparkEntity) sparks.get(0);
		return null;
	}
	
	@Override
	public int getAvailableSpaceForMana()
	{
		return Math.max(0, MANA_CAP - mana);
	}
	
	@Override
	public boolean areIncomingTranfersDone()
	{
		return !canRecieveManaFromBursts();
	}
	
	@Override
	public boolean isOutputtingPower()
	{
		return false;
	}
	
	@Override
	public EnumDyeColor getColor()
	{
		return color;
	}
	
	@Override
	public void setColor(EnumDyeColor color)
	{
		this.color = color;
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 0b1011);
	}
	
	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res)
	{
		HUDHandler.drawSimpleManaHUD(0x44FF44, getLastKnownMana(), MANA_CAP, BlocksBA.MANA_TESSERACT.getLocalizedName(), res);
		
		int x = res.getScaledWidth() / 2 - 11;
		int y = res.getScaledHeight() / 2 + 30;
		
		String str = "Channel: " + Integer.toString(channel, Character.MAX_RADIX);
		mc.fontRenderer.drawString(str, x - mc.fontRenderer.getStringWidth(str) / 2, y, 0xFFFFFF, true);
		
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}

	@Override
	public int getLastKnownMana()
	{
		return lastKnownMana;
	}

	@Override
	public void setLastKnownMana(int mana)
	{
		lastKnownMana = mana;
	}

	@Override
	public BlockPos pos()
	{
		return getPos();
	}
}