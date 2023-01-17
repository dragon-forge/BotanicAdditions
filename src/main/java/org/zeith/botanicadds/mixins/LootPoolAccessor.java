package org.zeith.botanicadds.mixins;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootPool.class)
public interface LootPoolAccessor
{
	@Invoker("<init>")
	static LootPool createLootPool(LootPoolEntryContainer[] p_165128_,
								   LootItemCondition[] p_165129_,
								   LootItemFunction[] p_165130_,
								   NumberProvider p_165131_,
								   NumberProvider p_165132_,
								   String name)
	{
		throw new UnsupportedOperationException();
	}
	
	@Accessor("entries")
	LootPoolEntryContainer[] getEntries_botanicAdditions();
	
	@Accessor("conditions")
	LootItemCondition[] getConditions_botanicAdditions();
	
	@Accessor("functions")
	LootItemFunction[] getFunctions_botanicAdditions();
}
