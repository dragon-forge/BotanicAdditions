package org.zeith.botanicadds.tiles;

import com.google.common.base.Predicates;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.blocks.BlockManaTesseract;
import org.zeith.botanicadds.init.TilesBA;
import org.zeith.botanicadds.world.WorldTesseractData;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.properties.PropertyInt;
import org.zeith.hammerlib.tiles.TileSyncableTickable;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.item.BotaniaItems;

import java.util.List;
import java.util.Optional;

public class TileManaTesseract
		extends TileSyncableTickable
		implements Wandable, ManaReceiver, SparkAttachable, ManaPool
{
	@NBTSerializable("mana")
	public int _mana;
	
	@NBTSerializable("max_mana")
	public int _maxMana = 100;
	
	@NBTSerializable("channel")
	public String channelReadable;
	
	@NBTSerializable("owner_name")
	public String channelOwnerName;
	
	@NBTSerializable("private")
	public boolean isPrivate;
	
	public GameProfile owner;
	
	public final PropertyInt maxManaData = new PropertyInt(DirectStorage.create(v -> _maxMana = v, () -> _maxMana));
	public final PropertyInt manaData = new PropertyInt(DirectStorage.create(v -> _mana = v, () -> _mana));
	
	public TileManaTesseract(BlockPos pos, BlockState state)
	{
		super(TilesBA.MANA_TESSERACT, pos, state);
		dispatcher.registerProperty("mana", manaData);
		dispatcher.registerProperty("max_mana", maxManaData);
	}
	
	@Override
	public CompoundTag writeNBT(CompoundTag nbt)
	{
		nbt = super.writeNBT(nbt);
		if(owner != null) nbt.put("OwnerGP", NbtUtils.writeGameProfile(new CompoundTag(), owner));
		return nbt;
	}
	
	@Override
	public void readNBT(CompoundTag nbt)
	{
		if(nbt.contains("OwnerGP", Tag.TAG_COMPOUND)) owner = NbtUtils.readGameProfile(nbt.getCompound("OwnerGP"));
		super.readNBT(nbt);
	}
	
	public WorldTesseractData.TesseractMode getMode()
	{
		return getBlockState().getValue(BlockManaTesseract.MODE);
	}
	
	public String getChannel()
	{
		if(isPrivate)
		{
			if(owner == null) return null;
			return "pri/" + owner.getId().toString() + "/" + channelReadable;
		}
		
		return "pub/" + channelReadable;
	}
	
	public boolean hasChannel()
	{
		return !StringUtil.isNullOrEmpty(getChannel());
	}
	
	@Override
	public void update()
	{
		if(isOnClient() || level == null) return;
		var td = WorldTesseractData.forServer(level).orElse(null);
		if(td == null) return;
		
		var mode = getMode();
		var channel = getChannel();
		
		var srv = level.getServer();
		if(srv != null && owner != null)
		{
			var sp = srv.getPlayerList().getPlayer(owner.getId());
			if(sp != null)
			{
				owner = sp.getGameProfile();
				channelOwnerName = owner.getName();
			}
		}
		
		// Update mana from pool
		if(hasChannel())
		{
			manaData.setInt(td.getMana(channel));
			maxManaData.setInt(td.getMaxManaPerChannel(channel));
		} else
		{
			manaData.setInt(0);
			maxManaData.setInt(1_000_000);
			level.destroyBlock(worldPosition, true);
		}
		
		if(atTickRate(3) && mode.shouldEmitMana())
		{
			for(Direction dir : Direction.values())
			{
				var remoteBE = level.getBlockEntity(worldPosition.relative(dir));
				if(remoteBE != null && !(remoteBE instanceof TileManaTesseract))
				{
					var manaRec = remoteBE.getCapability(BotaniaForgeCapabilities.MANA_RECEIVER, dir.getOpposite()).orElse(null);
					if(manaRec != null && !manaRec.isFull())
					{
						int take = td.takeMana(channel, 1000, IFluidHandler.FluidAction.EXECUTE);
						manaRec.receiveMana(take);
					}
				}
			}
		}
	}
	
	public ItemStack storeData(ItemStack base)
	{
		if(channelReadable != null && !channelReadable.isBlank())
			base.getOrCreateTag().putString("Channel", channelReadable);
		base.getOrCreateTag().putBoolean("Private", isPrivate);
		return base;
	}
	
	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side)
	{
		if(player != null && player.isShiftKeyDown())
		{
			var mode = getBlockState().getValue(BlockManaTesseract.MODE);
			var modes = WorldTesseractData.TesseractMode.values();
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockManaTesseract.MODE, modes[(mode.ordinal() + 1) % modes.length]));
		}
		
		return true;
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
		return _mana;
	}
	
	@Override
	public int getMaxMana()
	{
		return _maxMana;
	}
	
	@Override
	public boolean isFull()
	{
		return !hasChannel() || !getMode().shouldAcceptMana() || getCurrentMana() >= getMaxMana();
	}
	
	@Override
	public void receiveMana(int mana)
	{
		if(!hasChannel() || !isOnServer()) return;
		
		var channel = getChannel();
		
		if(mana < 0)
			WorldTesseractData.forServer(level)
					.ifPresent(data ->
					{
						data.takeMana(channel, -mana, IFluidHandler.FluidAction.EXECUTE);
						manaData.setInt(data.getMana(channel));
					});
		else if(mana > 0)
			WorldTesseractData.forServer(level)
					.ifPresent(data ->
					{
						data.storeMana(channel, mana, IFluidHandler.FluidAction.EXECUTE);
						manaData.setInt(data.getMana(channel));
					});
	}
	
	@Override
	public boolean canReceiveManaFromBursts()
	{
		return isOnServer() && getMode().shouldAcceptMana();
	}
	
	@Override
	public boolean canAttachSpark(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getAvailableSpaceForMana()
	{
		return getMode().shouldAcceptMana()
				? getMaxMana() - getCurrentMana()
				: 0;
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
	public boolean isOutputtingPower()
	{
		return false;
	}
	
	@Override
	public Optional<DyeColor> getColor()
	{
		return Optional.empty();
	}
	
	@Override
	public void setColor(Optional<DyeColor> color)
	{
	}
	
	LazyOptional<Wandable> wandableCap = LazyOptional.of(() -> this);
	LazyOptional<ManaReceiver> manaReceiverCap = LazyOptional.of(() -> this);
	LazyOptional<SparkAttachable> sparkCap = LazyOptional.of(() -> this);
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(BotaniaForgeCapabilities.WANDABLE == cap) return BotaniaForgeCapabilities.WANDABLE.orEmpty(cap, wandableCap);
		if(BotaniaForgeCapabilities.MANA_RECEIVER == cap) return BotaniaForgeCapabilities.MANA_RECEIVER.orEmpty(cap, manaReceiverCap);
		if(BotaniaForgeCapabilities.SPARK_ATTACHABLE == cap) return BotaniaForgeCapabilities.SPARK_ATTACHABLE.orEmpty(cap, sparkCap);
		return super.getCapability(cap, side);
	}
	
	public static class WandHud
			implements WandHUD
	{
		private final TileManaTesseract tess;
		
		public WandHud(TileManaTesseract tess)
		{
			this.tess = tess;
		}
		
		@Override
		public void renderHUD(PoseStack ms, Minecraft mc)
		{
			var tess = new ItemStack(this.tess.getBlockState().getBlock());
			String name = tess.getHoverName().getString();
			
			var saturation = (float) (Math.sin(Math.toRadians(System.currentTimeMillis() % 3600L / 10D)) + 1F) / 2F * 0.25F;
			BotaniaAPIClient.instance().drawSimpleManaHUD(ms, Mth.hsvToRgb(193 / 360F, 0.5F + saturation, 1F), this.tess.getCurrentMana(),
					this.tess.getMaxMana(), name);
			
			int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 11;
			int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + 30;
			
			int u = this.tess.getMode().shouldEmitMana() ? 22 : 0;
			int v = 38;
			
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			RenderSystem.setShaderTexture(0, HUDHandler.manaBar);
			RenderHelper.drawTexturedModalRect(ms, x, y, u, v, 22, 15);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			
			mc.getItemRenderer().renderAndDecorateItem(new ItemStack(BotaniaItems.spark), x - 20, y);
			mc.getItemRenderer().renderAndDecorateItem(tess, x + 26, y);
			
			int modeColor = 0x22AA22;
			String mode = "public";
			
			if(this.tess.isPrivate)
			{
				modeColor = 0xAA2222;
				mode = "private";
			}
			
			var modeCom = Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner.mode." + mode);
			if(this.tess.isPrivate && !StringUtil.isNullOrEmpty(this.tess.channelOwnerName))
				modeCom = modeCom.append(" (").append(this.tess.channelOwnerName).append(")");
			var comp = Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner.mode",
					modeCom.withStyle(Style.EMPTY.withColor(modeColor))
			);
			
			y += 20;
			x = mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width(comp) / 2;
			mc.font.drawShadow(ms, comp, x, y, 0x00A56B);
			
			var comp2 = Component.literal(this.tess.channelReadable);
			
			y += mc.font.lineHeight + 2;
			x = mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width(comp2) / 2;
			mc.font.drawShadow(ms, comp2, x, y, 0x00A56B);
		}
	}
}