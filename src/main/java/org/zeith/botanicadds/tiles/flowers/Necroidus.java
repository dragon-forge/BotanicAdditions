package org.zeith.botanicadds.tiles.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.api.FunctionalFlowerHUD;
import org.zeith.botanicadds.init.FlowersBA;
import org.zeith.hammerlib.util.java.Cast;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

@FunctionalFlowerHUD
public class Necroidus
		extends FunctionalFlowerBlockEntity
{
	private static final int RANGE = 3;
	
	public Necroidus(BlockPos pos, BlockState state)
	{
		super(FlowersBA.NECROIDUS_TYPE, pos, state);
	}
	
	@Override
	public void tickFlower()
	{
		super.tickFlower();
		
		if(getMana() >= 100 && ticksExisted % 10 == 0 && !level.isClientSide && redstoneSignal <= 0)
		{
			int slowdown = 0;
			
			for(ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition).inflate(getRange())))
			{
				if(item.getAge() < 19 + slowdown || item.isRemoved()) continue;
				
				ItemStack stack = item.getItem();
				if(stack.isEmpty()) continue;
				
				if(stack.is(Items.SOUL_SAND) || stack.is(Items.WITHER_SKELETON_SKULL))
				{
					BlockPos starting = worldPosition;
					BlockState cs;
					int steps = 0;
					while((cs = level.getBlockState(starting)).getBlock() != Blocks.AIR || !level.getBlockState(starting.above()).is(Blocks.AIR) || !level.getBlockState(starting.above(2)).is(Blocks.AIR))
					{
						if(cs.getBlock() == Blocks.SOUL_SAND)
							break;
						starting = starting.above();
						++steps;
						if(steps > 5)
							return;
					}
					
					BlockPos soulSandPos = null;
					BlockPos skullPos = null;
					
					for(int i = 0; i < 2; ++i)
					{
						BlockPos cp = starting.above(i);
						if(level.getBlockState(cp).isAir())
						{
							soulSandPos = cp;
							break;
						} else if(i == 1)
						{
							BlockPos cp2 = starting.above(2);
							var tile = level.getBlockState(cp2);
							if(tile.is(Blocks.AIR))
								skullPos = cp2;
						}
					}
					
					boolean xValid = true;
					boolean zValid = true;
					
					if(soulSandPos == null)
					{
						for(int i = 0; i < 3; i += 2)
						{
							BlockPos cp = starting.offset(i - 1, 1, 0);
							var bl = level.getBlockState(cp);
							if(!bl.is(Blocks.SOUL_SAND) && !bl.isAir())
							{
								xValid = false;
								break;
							}
						}
						
						for(int i = 0; i < 3; i += 2)
						{
							BlockPos cp = starting.offset(0, 1, i - 1);
							var bl = level.getBlockState(cp);
							if(!bl.is(Blocks.SOUL_SAND) && !bl.isAir())
							{
								zValid = false;
								break;
							}
						}
						
						if(xValid)
							for(int i = 0; i < 3; i += 2)
							{
								BlockPos cp = starting.offset(i - 1, 1, 0);
								var bl = level.getBlockState(cp);
								if(bl.isAir())
								{
									soulSandPos = cp;
									break;
								} else
								{
									BlockPos cp2 = starting.offset(i - 1, 2, 0);
									if(level.getBlockState(cp2).isAir())
										skullPos = cp2;
								}
							}
						else if(zValid)
							for(int i = 0; i < 3; i += 2)
							{
								BlockPos cp = starting.offset(0, 1, i - 1);
								var bl = level.getBlockState(cp);
								if(bl.isAir())
								{
									soulSandPos = cp;
									break;
								} else
								{
									BlockPos cp2 = starting.offset(0, 2, i - 1);
									if(level.getBlockState(cp2).isAir())
										skullPos = cp2;
								}
							}
					}
					
					if(stack.is(Items.SOUL_SAND) && soulSandPos != null)
					{
						stack.shrink(1);
						level.setBlockAndUpdate(soulSandPos, Blocks.SOUL_SAND.defaultBlockState());
						
						var st = level.getBlockState(soulSandPos).getSoundType(level, soulSandPos, null);
						if(level instanceof ServerLevel sl)
							sl.playSound(null, soulSandPos, st.getPlaceSound(), SoundSource.BLOCKS, (st.getVolume() + 1.0F) / 2.0F, st.getPitch() * 0.8F);
						
						addMana(-100);
						sync();
						return;
					} else if(!stack.is(Items.SOUL_SAND) && skullPos != null && (xValid || zValid))
					{
						var state = Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, 0);
						level.setBlockAndUpdate(skullPos, state);
						
						var st = level.getBlockState(skullPos).getSoundType(level, skullPos, null);
						if(level instanceof ServerLevel sl)
							sl.playSound(null, skullPos, st.getPlaceSound(), SoundSource.BLOCKS, (st.getVolume() + 1.0F) / 2.0F, st.getPitch() * 0.8F);
						
						SkullBlockEntity te = Cast.cast(level.getBlockEntity(skullPos), SkullBlockEntity.class);
						if(te == null)
						{
							te = new SkullBlockEntity(skullPos, state);
							level.setBlockEntity(te);
						}
						
						stack.shrink(1);
						
						addMana(-100);
						sync();
						
						WitherSkullBlock.checkSpawn(level, skullPos, te);
						return;
					}
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
		return RANGE;
	}
	
	@Override
	public @Nullable RadiusDescriptor getRadius()
	{
		return RadiusDescriptor.Rectangle.square(this.getEffectivePos(), this.getRange());
	}
	
	@Override
	public int getMaxMana()
	{
		return 700;
	}
	
	@Override
	public int getColor()
	{
		return 0x1B1B1B;
	}
	
	@Override
	public boolean acceptsRedstone()
	{
		return true;
	}
}