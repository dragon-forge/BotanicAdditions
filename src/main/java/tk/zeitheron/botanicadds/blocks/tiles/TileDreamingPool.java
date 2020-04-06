package tk.zeitheron.botanicadds.blocks.tiles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicates;
import tk.zeitheron.botanicadds.IHUDRenderable;
import tk.zeitheron.botanicadds.init.BlocksBA;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IKeyLocked;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;

public class TileDreamingPool extends TileMod implements IManaPool, IKeyLocked, ISparkAttachable, IThrottledPacket, ITickable, IHUDRenderable
{
	public static final Color PARTICLE_COLOR = new Color(0x00C6FF);
	public static final int MAX_MANA = 2000000;
	
	private static final String TAG_MANA = "Mana";
	private static final String TAG_KNOWN_MANA = "KnownMana";
	private static final String TAG_OUTPUTTING = "Outputting";
	private static final String TAG_COLOR = "Color";
	private static final String TAG_CAN_ACCEPT = "CanAccept";
	private static final String TAG_CAN_SPARE = "CanSpare";
	private static final String TAG_FRAGILE = "Fragile";
	private static final String TAG_INPUT_KEY = "InputKey";
	private static final String TAG_OUTPUT_KEY = "OutputKey";
	private static final int CRAFT_EFFECT_EVENT = 0;
	private static final int CHARGE_EFFECT_EVENT = 1;
	
	private boolean outputting = false;
	
	public EnumDyeColor color = EnumDyeColor.WHITE;
	int mana;
	private int knownMana = -1;
	
	private int soundTicks = 0;
	private boolean canAccept = true;
	private boolean canSpare = true;
	public boolean fragile = false;
	boolean isDoingTransfer = false;
	int ticksDoingTransfer = 0;
	
	private String inputKey = "";
	private final String outputKey = "";
	
	private int ticks = 0;
	private boolean sendPacket = false;
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState)
	{
		if(oldState.getBlock() != newState.getBlock())
			return true;
		if(oldState.getBlock() != BlocksBA.DREAMING_POOL || newState.getBlock() != BlocksBA.DREAMING_POOL)
			return true;
		return false;
	}
	
	@Override
	public boolean isFull()
	{
		Block blockBelow = world.getBlockState(pos.down()).getBlock();
		return blockBelow != ModBlocks.manaVoid && getCurrentMana() >= MAX_MANA;
	}
	
	@Override
	public void recieveMana(int mana)
	{
		int old = this.mana;
		this.mana = Math.max(0, Math.min(getCurrentMana() + mana, MAX_MANA));
		if(old != this.mana)
		{
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
			markDispatchable();
		}
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		ManaNetworkEvent.removePool(this);
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		ManaNetworkEvent.removePool(this);
	}
	
	public static int calculateComparatorLevel(int mana, int max)
	{
		int val = (int) ((double) mana / (double) max * 15.0);
		if(mana > 0)
			val = Math.max(val, 1);
		return val;
	}
	
	public static RecipeManaInfusion getMatchingRecipe(@Nonnull ItemStack stack, @Nonnull IBlockState state)
	{
		List<RecipeManaInfusion> matchingNonCatRecipes = new ArrayList<>();
		List<RecipeManaInfusion> matchingCatRecipes = new ArrayList<>();
		
		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes)
		{
			if(recipe.matches(stack))
			{
				if(recipe.getCatalyst() == null)
					matchingNonCatRecipes.add(recipe);
				else if(recipe.getCatalyst() == state)
					matchingCatRecipes.add(recipe);
			}
		}
		
		// Recipes with matching catalyst take priority above recipes with no
		// catalyst specified
		return !matchingCatRecipes.isEmpty() ? matchingCatRecipes.get(0) : !matchingNonCatRecipes.isEmpty() ? matchingNonCatRecipes.get(0) : null;
	}
	
	public boolean collideEntityItem(EntityItem item)
	{
		if(world.isRemote || item.isDead || item.getItem().isEmpty())
			return false;
		
		ItemStack stack = item.getItem();
		
		if(stack.getItem() instanceof IManaDissolvable)
		{
			((IManaDissolvable) stack.getItem()).onDissolveTick(this, stack, item);
		}
		
		if(item.age > 100 && item.age < 130)
			return false;
		
		RecipeManaInfusion recipe = getMatchingRecipe(stack, world.getBlockState(pos.down()));
		
		if(recipe != null)
		{
			int mana = recipe.getManaToConsume();
			if(getCurrentMana() >= mana)
			{
				recieveMana(-mana);
				
				stack.shrink(1);
				
				ItemStack output = recipe.getOutput().copy();
				EntityItem outputItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
				outputItem.age = 105;
				world.spawnEntity(outputItem);
				
				craftingFanciness();
				return true;
			}
		}
		
		return false;
	}
	
	private void craftingFanciness()
	{
		if(soundTicks == 0)
		{
			world.playSound(null, pos, ModSounds.manaPoolCraft, SoundCategory.BLOCKS, 0.4F, 4F);
			soundTicks = 6;
		}
		
		world.addBlockEvent(getPos(), getBlockType(), CRAFT_EFFECT_EVENT, 0);
	}
	
	@Override
	public boolean receiveClientEvent(int event, int param)
	{
		switch(event)
		{
		case CRAFT_EFFECT_EVENT:
		{
			if(world.isRemote)
			{
				for(int i = 0; i < 25; i++)
				{
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();
					Botania.proxy.sparkleFX(pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 0.75, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
				}
			}
			
			return true;
		}
		case CHARGE_EFFECT_EVENT:
		{
			if(world.isRemote)
			{
				if(ConfigHandler.chargingAnimationEnabled)
				{
					boolean outputting = param == 1;
					Vector3 itemVec = Vector3.fromBlockPos(pos).add(0.5, 0.5 + Math.random() * 0.3, 0.5);
					Vector3 tileVec = Vector3.fromBlockPos(pos).add(0.2 + Math.random() * 0.6, 0, 0.2 + Math.random() * 0.6);
					Botania.proxy.lightningFX(outputting ? tileVec : itemVec, outputting ? itemVec : tileVec, 80, world.rand.nextLong(), 0x4400799c, 0x4400C6FF);
				}
			}
			return true;
		}
		default:
			return super.receiveClientEvent(event, param);
		}
	}
	
	@Override
	public void update()
	{
		if(!ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid())
			ManaNetworkEvent.addPool(this);
		
		if(world.isRemote)
		{
			double particleChance = 1F - (double) getCurrentMana() / (double) MAX_MANA * 0.1;
			if(Math.random() > particleChance)
				Botania.proxy.wispFX(pos.getX() + 0.3 + Math.random() * 0.5, pos.getY() + 0.6 + Math.random() * 0.25, pos.getZ() + Math.random(), PARTICLE_COLOR.getRed() / 255F, PARTICLE_COLOR.getGreen() / 255F, PARTICLE_COLOR.getBlue() / 255F, (float) Math.random() / 3F, (float) -Math.random() / 25F, 2F);
			return;
		}
		
		boolean wasDoingTransfer = isDoingTransfer;
		isDoingTransfer = false;
		
		if(soundTicks > 0)
		{
			soundTicks--;
		}
		
		if(sendPacket && ticks % 10 == 0)
		{
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			sendPacket = false;
		}
		
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
		for(EntityItem item : items)
		{
			if(item.isDead)
				continue;
			
			ItemStack stack = item.getItem();
			if(!stack.isEmpty() && stack.getItem() instanceof IManaItem)
			{
				IManaItem mana = (IManaItem) stack.getItem();
				if(outputting && mana.canReceiveManaFromPool(stack, this) || !outputting && mana.canExportManaToPool(stack, this))
				{
					boolean didSomething = false;
					
					int bellowCount = 0;
					if(outputting)
						for(EnumFacing dir : EnumFacing.HORIZONTALS)
						{
							TileEntity tile = world.getTileEntity(pos.offset(dir));
							if(tile != null && tile instanceof TileBellows && ((TileBellows) tile).getLinkedTile() == this)
								bellowCount++;
						}
					int transfRate = 1000 * (bellowCount + 1);
					
					if(outputting)
					{
						if(canSpare)
						{
							if(getCurrentMana() > 0 && mana.getMana(stack) < mana.getMaxMana(stack))
								didSomething = true;
							
							int manaVal = Math.min(transfRate, Math.min(getCurrentMana(), mana.getMaxMana(stack) - mana.getMana(stack)));
							mana.addMana(stack, manaVal);
							recieveMana(-manaVal);
						}
					} else
					{
						if(canAccept)
						{
							if(mana.getMana(stack) > 0 && !isFull())
								didSomething = true;
							
							int manaVal = Math.min(transfRate, Math.min(MAX_MANA - getCurrentMana(), mana.getMana(stack)));
							mana.addMana(stack, -manaVal);
							recieveMana(manaVal);
						}
					}
					
					if(didSomething)
					{
						if(ConfigHandler.chargingAnimationEnabled && world.rand.nextInt(20) == 0)
						{
							world.addBlockEvent(getPos(), getBlockType(), CHARGE_EFFECT_EVENT, outputting ? 1 : 0);
						}
						isDoingTransfer = outputting;
					}
				}
			}
		}
		
		if(isDoingTransfer)
			ticksDoingTransfer++;
		else
		{
			ticksDoingTransfer = 0;
			if(wasDoingTransfer)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
		
		ticks++;
	}
	
	@Override
	public void writePacketNBT(NBTTagCompound cmp)
	{
		cmp.setInteger(TAG_MANA, mana);
		cmp.setBoolean(TAG_OUTPUTTING, outputting);
		cmp.setInteger(TAG_COLOR, color.getMetadata());
		
		cmp.setBoolean(TAG_CAN_ACCEPT, canAccept);
		cmp.setBoolean(TAG_CAN_SPARE, canSpare);
		cmp.setBoolean(TAG_FRAGILE, fragile);
		
		cmp.setString(TAG_INPUT_KEY, inputKey);
		cmp.setString(TAG_OUTPUT_KEY, outputKey);
	}
	
	@Override
	public void readPacketNBT(NBTTagCompound cmp)
	{
		mana = cmp.getInteger(TAG_MANA);
		outputting = cmp.getBoolean(TAG_OUTPUTTING);
		color = EnumDyeColor.byMetadata(cmp.getInteger(TAG_COLOR));
		
		if(cmp.hasKey(TAG_CAN_ACCEPT))
			canAccept = cmp.getBoolean(TAG_CAN_ACCEPT);
		if(cmp.hasKey(TAG_CAN_SPARE))
			canSpare = cmp.getBoolean(TAG_CAN_SPARE);
		fragile = cmp.getBoolean(TAG_FRAGILE);
		
		if(cmp.hasKey(TAG_INPUT_KEY))
			inputKey = cmp.getString(TAG_INPUT_KEY);
		if(cmp.hasKey(TAG_OUTPUT_KEY))
			inputKey = cmp.getString(TAG_OUTPUT_KEY);
		
		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
	}
	
	public void onWanded(EntityPlayer player, ItemStack wand)
	{
		if(player == null)
			return;
		
		if(player.isSneaking())
		{
			outputting = !outputting;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}
		
		if(!world.isRemote)
		{
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writePacketNBT(nbttagcompound);
			nbttagcompound.setInteger(TAG_KNOWN_MANA, getCurrentMana());
			if(player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).connection.sendPacket(new SPacketUpdateTileEntity(pos, -999, nbttagcompound));
		}
		
		world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 0.11F, 1F);
	}
	
	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res)
	{
		{
			ItemStack pool = new ItemStack(BlocksBA.DREAMING_POOL);
			String name = I18n.format(pool.getTranslationKey().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
			int color = 0x4444FF;
			HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);
			
			int x = res.getScaledWidth() / 2 - 11;
			int y = res.getScaledHeight() / 2 + 30;
			
			int u = outputting ? 22 : 0;
			int v = 38;
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			mc.renderEngine.bindTexture(HUDHandler.manaBar);
			RenderHelper.drawTexturedModalRect(x, y, 0, u, v, 22, 15);
			GlStateManager.color(1F, 1F, 1F, 1F);
			
			ItemStack tablet = new ItemStack(ModItems.manaTablet);
			ItemManaTablet.setStackCreative(tablet);
			
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemAndEffectIntoGUI(tablet, x - 20, y);
			mc.getRenderItem().renderItemAndEffectIntoGUI(pool, x + 26, y);
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
			
			GlStateManager.disableLighting();
			GlStateManager.disableBlend();
		}
	}
	
	@Override
	public boolean canRecieveManaFromBursts()
	{
		return true;
	}
	
	@Override
	public boolean isOutputtingPower()
	{
		return outputting;
	}
	
	@Override
	public int getCurrentMana()
	{
		return mana;
	}
	
	@Override
	public String getInputKey()
	{
		return inputKey;
	}
	
	@Override
	public String getOutputKey()
	{
		return outputKey;
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
		List sparks = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if(sparks.size() == 1)
		{
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}
		
		return null;
	}
	
	@Override
	public boolean areIncomingTranfersDone()
	{
		return false;
	}
	
	@Override
	public int getAvailableSpaceForMana()
	{
		int space = Math.max(0, MAX_MANA - getCurrentMana());
		if(space > 0)
			return space;
		else if(world.getBlockState(pos.down()).getBlock() == ModBlocks.manaVoid)
			return MAX_MANA;
		else
			return 0;
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
	
	@Override
	public void markDispatchable()
	{
		sendPacket = true;
	}

	@Override
	public void renderHUDPlz(Minecraft mc, ScaledResolution res)
	{
		ItemStack stack = mc.player.getHeldItemMainhand();
		RecipeManaInfusion recipe = TilePool.getMatchingRecipe(stack, getWorld().getBlockState(getPos().down()));
		if(recipe != null)
		{
			int x = res.getScaledWidth() / 2 - 11;
			int y = res.getScaledHeight() / 2 + 10;
			
			int u = getCurrentMana() >= recipe.getManaToConsume() ? 0 : 22;
			int v = mc.player.getName().equals("haighyorkie") && mc.player.isSneaking() ? 23 : 8;
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			mc.renderEngine.bindTexture(HUDHandler.manaBar);
			RenderHelper.drawTexturedModalRect(x, y, 0, u, v, 22, 15);
			GlStateManager.color(1F, 1F, 1F, 1F);
			
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x - 20, y);
			mc.getRenderItem().renderItemAndEffectIntoGUI(recipe.getOutput(), x + 26, y);
			mc.getRenderItem().renderItemOverlays(mc.fontRenderer, recipe.getOutput(), x + 26, y);
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
			
			GlStateManager.disableLighting();
			GlStateManager.disableBlend();
		}
	}
}