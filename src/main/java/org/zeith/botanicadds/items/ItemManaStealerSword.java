package org.zeith.botanicadds.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.DistExecutor;
import org.zeith.botanicadds.net.PacketLeftClickManaStealerSword;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.net.Network;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.item.SparkEntity;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.LensEffectItem;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.impl.mana.ManaItemHandlerImpl;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelSwordItem;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

public class ItemManaStealerSword
		extends ManasteelSwordItem
		implements LensEffectItem, IRegisterListener
{
	public static final Tier MANA_STEALING = new ForgeTier(0, 3000, 1F, 11, 60, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(BotaniaTags.Items.INGOTS_TERRASTEEL));
	
	private static final String TAG_OWNER = "Owner";
	private static final String TAG_OWNER_ID = "Id";
	private static final int MANA_PER_DAMAGE = 200;
	
	public ItemManaStealerSword(Properties props)
	{
		super(MANA_STEALING, 0, 0, props);
	}
	
	@Override
	public void onPostRegistered()
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
				MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.LeftClickEmpty e) ->
						Network.sendToServer(new PacketLeftClickManaStealerSword())
				)
		);
		
		MinecraftForge.EVENT_BUS.addListener(this::attackEntity);
	}
	
	private void attackEntity(AttackEntityEvent e)
	{
		if(e.getEntity() instanceof ServerPlayer sp)
			trySpawnBurst(sp);
	}
	
	public void trySpawnBurst(ServerPlayer player)
	{
		if(player.getMainHandItem().is(this) && player.getAttackStrengthScale(0) == 1)
		{
			var burst = getBurst(player, player.getMainHandItem());
			player.level.addFreshEntity(burst.entity());
			player.getMainHandItem().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.terraBlade, SoundSource.PLAYERS, 1F, 1F);
		}
	}
	
	public ManaBurst getBurst(ServerPlayer player, ItemStack stack)
	{
		ManaBurstEntity burst = new ManaBurstEntity(player);
		
		float motionModifier = 7F;
		
		burst.setColor(0xA03537);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(MANA_PER_DAMAGE / 4);
		burst.setManaLossPerTick(1F);
		burst.setGravity(0F);
		burst.setDeltaMovement(burst.getDeltaMovement().scale(motionModifier));
		
		ItemStack lens = stack.copy();
		lens.getOrCreateTagElement(TAG_OWNER).putUUID(TAG_OWNER_ID, player.getUUID());
		burst.setSourceLens(lens);
		
		return burst;
	}
	
	// Burst lens START
	
	@Override
	public void apply(ItemStack stack, BurstProperties props, Level level)
	{
	}
	
	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack)
	{
		int cost = 2000 + (int) Math.round(Math.random() * 2000);
		
		if(!shouldKill && pos instanceof EntityHitResult er && er.getEntity() instanceof ManaSpark spark)
		{
			Entity victim = (Entity) spark;
			var attacker = burst.getSourceLens().getOrCreateTagElement(TAG_OWNER).getUUID(TAG_OWNER_ID);
			Player owner = burst.entity().level.getPlayerByUUID(attacker);
			if(owner == null) return false;
			int manaToPut = Math.min(spark.getAttachedManaReceiver().getCurrentMana(), cost);
			spark.getAttachedManaReceiver().receiveMana(-manaToPut);
			ManaItemHandlerImpl.INSTANCE.dispatchMana(stack, owner, manaToPut, true);
			owner.level.playSound(null, victim.position().x, victim.position().y, victim.position().z, BotaniaSounds.enchanterFade, SoundSource.PLAYERS, 0.4F, 1.4F);
			owner.level.playSound(null, owner.position().x, owner.position().y, owner.position().z, BotaniaSounds.enchanterForm, SoundSource.PLAYERS, 0.4F, 1.4F);
			return true;
		}
		
		if(pos != null && pos.getType() == HitResult.Type.ENTITY && pos instanceof EntityHitResult er && shouldKill)
		{
			var attacker = burst.getSourceLens().getOrCreateTagElement(TAG_OWNER).getUUID(TAG_OWNER_ID);
			Player owner = burst.entity().level.getPlayerByUUID(attacker);
			if(owner == null) return true;
			
			if(er.getEntity() instanceof Player victim)
			{
				int extracted = ManaItemHandlerImpl.INSTANCE.requestMana(stack, victim, cost, false);
				ManaItemHandlerImpl.INSTANCE.requestMana(stack, victim, ManaItemHandlerImpl.INSTANCE.dispatchMana(stack, owner, extracted, true), true);
				victim.level.playSound(null, victim.position().x, victim.position().y, victim.position().z, BotaniaSounds.enchanterFade, SoundSource.PLAYERS, 0.4F, 1.4F);
			}
			
			owner.level.playSound(null, owner.position().x, owner.position().y, owner.position().z, BotaniaSounds.enchanterForm, SoundSource.PLAYERS, 0.4F, 1.4F);
		}
		
		return shouldKill;
	}
	
	@Override
	public void updateBurst(ManaBurst burst, ItemStack stack)
	{
		ThrowableProjectile entity = burst.entity();
		AABB axis = new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(1);
		List<LivingEntity> entities = entity.level.getEntitiesOfClass(LivingEntity.class, axis);
		Entity thrower = entity.getOwner();
		
		for(LivingEntity living : entities)
		{
			if(living == thrower || living instanceof Player livingPlayer && thrower instanceof Player throwingPlayer
					&& !throwingPlayer.canHarmPlayer(livingPlayer))
				continue;
			
			if(living.hurtTime == 0)
			{
				int cost = MANA_PER_DAMAGE / 3;
				int mana = burst.getMana();
				if(mana >= cost)
				{
					burst.setMana(mana - cost);
					float damage = 4F + BotaniaAPI.instance().getTerrasteelItemTier().getAttackDamageBonus();
					if(!burst.isFake() && !entity.level.isClientSide)
					{
						DamageSource source = DamageSource.MAGIC;
						if(thrower instanceof Player player)
							source = DamageSource.playerAttack(player);
						else if(thrower instanceof LivingEntity livingEntity)
							source = DamageSource.mobAttack(livingEntity);
						
						living.hurt(source, damage);
						
						collideBurst(burst, new EntityHitResult(living), false, true, stack);
						
						entity.discard();
						
						break;
					}
				}
			}
		}
	}
	
	@Override
	public boolean doParticles(ManaBurst burst, ItemStack stack)
	{
		return true;
	}
	
	// Burst lens END
}