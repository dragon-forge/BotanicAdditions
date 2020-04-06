package tk.zeitheron.botanicadds.items;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Nonnull;

import tk.zeitheron.botanicadds.net.PacketLC;
import com.zeitheron.hammercore.net.HCNet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class ItemManaStealerSword extends ItemSword implements ILensEffect, IManaUsingItem
{
	public static final ToolMaterial MANA_STEALING = EnumHelper.addToolMaterial("BA_MANA_STEALING", 0, 3000, 1F, 11, 60);
	
	private static final String TAG_ATTACKER_USERNAME = "attackerUsername";
	
	private static final int MANA_PER_DAMAGE = 200;
	
	public ItemManaStealerSword()
	{
		super(MANA_STEALING);
		setTranslationKey("mana_stealer_sword");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void leftClick(PlayerInteractEvent.LeftClickEmpty evt)
	{
		if(!evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this)
			HCNet.INSTANCE.sendToServer(new PacketLC());
	}
	
	@SubscribeEvent
	public void attackEntity(AttackEntityEvent evt)
	{
		if(!evt.getEntityPlayer().world.isRemote)
			trySpawnBurst(evt.getEntityPlayer());
	}
	
	public void trySpawnBurst(EntityPlayer player)
	{
		if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == this && player.getCooledAttackStrength(0) == 1)
		{
			IManaBurst burst = getBurst(player, player.getHeldItemMainhand());
			player.world.spawnEntity((Entity) burst);
			ToolCommons.damageItem(player.getHeldItemMainhand(), 1, player, MANA_PER_DAMAGE);
			player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.terraBlade, SoundCategory.PLAYERS, 0.4F, 1.4F);
		}
	}
	
	public IManaBurst getBurst(EntityPlayer player, ItemStack stack)
	{
		IManaBurst burst = null;
		
		try
		{
			burst = (IManaBurst) Class.forName("vazkii.botania.common.entity.EntityManaBurst").getDeclaredConstructor(EntityPlayer.class, EnumHand.class).newInstance(player, EnumHand.MAIN_HAND);
		} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		
		float motionModifier = 7F;
		
		burst.setColor(0xA03537);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(MANA_PER_DAMAGE / 4);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);
		burst.setMotion(((Entity) burst).motionX * motionModifier, ((Entity) burst).motionY * motionModifier, ((Entity) burst).motionZ * motionModifier);
		
		ItemStack lens = stack.copy();
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, player.getName());
		burst.setSourceLens(lens);
		
		return burst;
	}
	
	public int getManaPerDamage()
	{
		return MANA_PER_DAMAGE;
	}
	
	@Override
	public void apply(ItemStack stack, BurstProperties props)
	{
	}
	
	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack)
	{
		if(pos != null && pos.typeOfHit == Type.ENTITY)
		{
			String attacker = ItemNBTHelper.getString(burst.getSourceLens(), TAG_ATTACKER_USERNAME, "");
			EntityPlayer owner = ((Entity) burst).world.getPlayerEntityByName(attacker);
			
			if(pos.entityHit instanceof EntityPlayer)
			{
				EntityPlayer victim = (EntityPlayer) pos.entityHit;
				int extracted = ManaItemHandler.requestMana(stack, victim, 1000 + (int) Math.round(Math.random() * 1000), false);
				ManaItemHandler.requestMana(stack, victim, ManaItemHandler.dispatchMana(stack, owner, extracted, true), true);
				victim.world.playSound(null, victim.posX, victim.posY, victim.posZ, ModSounds.enchanterFade, SoundCategory.PLAYERS, 0.4F, 1.4F);
			}
			
			if(owner != null)
				owner.world.playSound(null, owner.posX, owner.posY, owner.posZ, ModSounds.enchanterForm, SoundCategory.PLAYERS, 0.4F, 1.4F);
		}
		return dead;
	}
	
	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack)
	{
		EntityThrowable entity = (EntityThrowable) burst;
		AxisAlignedBB axis = new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).grow(1);
		List<EntityLivingBase> entities = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, axis);
		String attacker = ItemNBTHelper.getString(burst.getSourceLens(), TAG_ATTACKER_USERNAME, "");
		
		for(EntityLivingBase living : entities)
		{
			if(living instanceof EntityPlayer && (living.getName().equals(attacker) || FMLCommonHandler.instance().getMinecraftServerInstance() != null && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled()))
				continue;
			
			if(living.hurtTime == 0)
			{
				int cost = MANA_PER_DAMAGE / 3;
				int mana = burst.getMana();
				if(mana >= cost)
				{
					burst.setMana(mana - cost);
					float damage = 4F + MANA_STEALING.getAttackDamage();
					if(!burst.isFake() && !entity.world.isRemote)
					{
						EntityPlayer player = living.world.getPlayerEntityByName(attacker);
						living.attackEntityFrom(player == null ? DamageSource.MAGIC : DamageSource.causePlayerDamage(player), damage);
						collideBurst(burst, new RayTraceResult(living), false, true, stack);
						entity.setDead();
						break;
					}
				}
			}
		}
	}
	
	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, @Nonnull ItemStack par2ItemStack)
	{
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 4 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}
	
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, @Nonnull EntityLivingBase par3EntityLivingBase)
	{
		if(usesMana(par1ItemStack))
			ToolCommons.damageItem(par1ItemStack, 1, par3EntityLivingBase, getManaPerDamage());
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World world, IBlockState state, @Nonnull BlockPos pos, @Nonnull EntityLivingBase entity)
	{
		if(usesMana(stack) && state.getBlockHardness(world, pos) != 0F)
			ToolCommons.damageItem(stack, 1, entity, getManaPerDamage());
		return true;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5)
	{
		if(!world.isRemote && player instanceof EntityPlayer && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) player, getManaPerDamage() * 2, true))
			stack.setItemDamage(stack.getItemDamage() - 1);
	}
	
	@Override
	public boolean usesMana(ItemStack stack)
	{
		return true;
	}
}