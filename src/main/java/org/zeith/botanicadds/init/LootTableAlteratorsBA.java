package org.zeith.botanicadds.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.zeith.botanicadds.mixins.LootPoolAccessor;

import java.util.ArrayList;
import java.util.List;

import static org.zeith.hammerlib.core.adapter.LootTableAdapter.alterTable;

public interface LootTableAlteratorsBA
{
	static void init()
	{
		alterTable(new ResourceLocation("botania:gaia_guardian/runes"), table ->
		{
			var runesPool = table.getPool("runes");
			
			if(runesPool == null) return table;
			
			LootPoolAccessor oldPool = (LootPoolAccessor) runesPool;
			
			var entries = new ArrayList<>(List.of(oldPool.getEntries_botanicAdditions()));
			
			entries.add(LootItem.lootTableItem(ItemsBA.RUNE_TP).build());
			entries.add(LootItem.lootTableItem(ItemsBA.RUNE_ENERGY).build());
			
			table.removePool("runes");
			
			table.addPool(LootPoolAccessor.createLootPool(
					entries.toArray(LootPoolEntryContainer[]::new),
					oldPool.getConditions_botanicAdditions(),
					oldPool.getFunctions_botanicAdditions(),
					runesPool.getRolls(),
					runesPool.getBonusRolls(),
					runesPool.getName()
			));
			
			return table;
		});
	}
}