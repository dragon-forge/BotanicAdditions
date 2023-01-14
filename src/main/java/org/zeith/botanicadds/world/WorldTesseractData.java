package org.zeith.botanicadds.world;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.Util;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.zeith.botanicadds.BotanicAdditions;

import java.util.*;
import java.util.function.Supplier;

public class WorldTesseractData
		extends SavedData
{
	public static final String TESSERACTS = BotanicAdditions.MOD_ID + "_tesseracts";
	
	protected final Object2IntArrayMap<String> channelStorage = new Object2IntArrayMap<>();
	
	public WorldTesseractData()
	{
	}
	
	public int getMana(String channel)
	{
		return channelStorage.getInt(channel);
	}
	
	public void setMana(String channel, int mana)
	{
		if(mana <= 0L) channelStorage.removeInt(channel);
		else channelStorage.put(channel, mana);
		setDirty();
	}
	
	public int getMaxManaPerChannel(String channel)
	{
		return 1_000_000; // One mana pool worth of mana per channel.
	}
	
	public int storeMana(String channel, int amount, IFluidHandler.FluidAction action)
	{
		int mn = getMana(channel);
		int accept = Math.min(getMaxManaPerChannel(channel) - mn, Math.abs(amount));
		if(accept > 0 && action.execute()) setMana(channel, mn + accept);
		return accept;
	}
	
	public int takeMana(String channel, int amount, IFluidHandler.FluidAction action)
	{
		int mn = getMana(channel);
		int extract = Math.min(mn, Math.abs(amount));
		if(extract > 0 && action.execute()) setMana(channel, mn - extract);
		return extract;
	}
	
	public WorldTesseractData(CompoundTag tag)
	{
		var chStLst = tag.getList("Mana", Tag.TAG_COMPOUND);
		for(var i = 0; i < chStLst.size(); ++i)
		{
			var e = chStLst.getCompound(i);
			channelStorage.put(e.getString("Channel"), e.getInt("Mana"));
		}
	}
	
	@Override
	public CompoundTag save(CompoundTag tag)
	{
		var chStLst = new ListTag();
		for(var e : channelStorage.object2IntEntrySet())
		{
			var ct = new CompoundTag();
			ct.putString("Channel", e.getKey());
			ct.putLong("Mana", e.getIntValue());
			chStLst.add(ct);
		}
		tag.put("Mana", chStLst);
		
		return tag;
	}
	
	public static Optional<WorldTesseractData> forServer(Level level)
	{
		if(!(level instanceof ServerLevel sl)) return Optional.empty();
		var over = sl.getServer().overworld();
		var wtd = over.getDataStorage().get(WorldTesseractData::new, TESSERACTS);
		if(wtd == null) over.getDataStorage().set(TESSERACTS, wtd = new WorldTesseractData());
		return Optional.of(wtd);
	}
	
	public enum TesseractMode
			implements StringRepresentable
	{
		SEND("send"),
		RECEIVE("receive");
		
		final String key;
		
		TesseractMode(String key)
		{
			this.key = key;
		}
		
		public boolean shouldAcceptMana()
		{
			return this == RECEIVE;
		}
		
		public boolean shouldEmitMana()
		{
			return this == SEND;
		}
		
		@Override
		public String getSerializedName()
		{
			return key;
		}
		
		private static final Supplier<Map<String, TesseractMode>> LOOKUP_TABLE = Suppliers.memoize(() -> Util.make(new HashMap<>(), m ->
		{
			for(var v : values())
				m.put(v.getSerializedName(), v);
		}));
		
		public static TesseractMode bySerializedName(String key)
		{
			return LOOKUP_TABLE.get().get(key);
		}
	}
}