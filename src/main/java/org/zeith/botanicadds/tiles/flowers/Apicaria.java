package org.zeith.botanicadds.tiles.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.FlowerHUD;
import org.zeith.botanicadds.init.FlowersBA;
import org.zeith.botanicadds.init.TagsBA;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.Objects;
import java.util.Optional;

@FlowerHUD
public class Apicaria
		extends FunctionalFlowerBlockEntity
{
	public boolean hasOvergrownSoil;
	
	public Apicaria(BlockPos pos, BlockState state)
	{
		super(FlowersBA.APICARIA_TYPE, pos, state);
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		hasOvergrownSoil = overgrowth;
		
		var radius = getRange();
		
		if(getMana() > 150 && ticksExisted % 2 == 0)
		{
			var bees = level.getEntitiesOfClass(Mob.class,
					new AABB(worldPosition).inflate(radius),
					b -> b.getType().is(TagsBA.EntityTypes.BEES) && b.blockPosition().closerThan(worldPosition, radius)
			);
			
			for(int i = 0; i < bees.size() && getMana() > 150; i++)
			{
				var bee = bees.get(i);
				if(bee.getTarget() instanceof Player)
				{
					bee.setTarget(null);
					if(bee instanceof NeutralMob nm)
						nm.stopBeingAngry();
					addMana(-150);
				}
			}
		}
	}
	
	@Override
	public boolean isOvergrowthAffected()
	{
		return false;
	}
	
	public int getRange()
	{
		return hasOvergrownSoil ? 16 : 8;
	}
	
	@Override
	public int getMaxMana()
	{
		return 600;
	}
	
	@Override
	public int getColor()
	{
		return 0xFFA500;
	}
	
	@Override
	public void addMana(int mana)
	{
		super.addMana(mana);
		if(mana < 0) sync();
	}
	
	@Override
	public @Nullable RadiusDescriptor getRadius()
	{
		return new RadiusDescriptor.Circle(worldPosition, getRange());
	}
	
	public static boolean findActiveApicariaAndUseMana(Level level, BlockPos pos)
	{
		return findActiveApicaria(level, pos).map(a ->
		{
			if(!level.isClientSide)
			{
				boolean wasFromBlock = false;
				
				var stackTrace = Thread.currentThread().getStackTrace();
				for(int i = 4; i < Math.min(stackTrace.length, 16); i++)
				{
					StackTraceElement element = stackTrace[i];
					try
					{
						var callerClass = Block.class.getClassLoader().loadClass(element.getClassName());
						if(!CampfireBlock.class.isAssignableFrom(callerClass) && Block.class.isAssignableFrom(callerClass))
						{
							wasFromBlock = true;
							break;
						}
					} catch(ClassNotFoundException e)
					{
					}
				}
				
				if(wasFromBlock)
				{
					a.addMana(-150);
				}
			}
			return true;
		}).orElse(false);
	}
	
	public static Optional<Apicaria> findActiveApicaria(Level level, BlockPos pos)
	{
		return BlockPos.betweenClosedStream(BoundingBox.fromCorners(pos.offset(-16, -6, -16), pos.offset(16, 2, 16)))
				.map(pos0 ->
				{
					if(level.getBlockEntity(pos0) instanceof Apicaria a && a.worldPosition.closerThan(pos, a.getRange()) && a.getMana() >= 150)
						return a;
					return null;
				})
				.filter(Objects::nonNull)
				.findFirst();
	}
}