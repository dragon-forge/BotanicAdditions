package org.zeith.botanicadds.init;

import net.minecraft.world.item.Item;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.items.ItemMaterial;
import org.zeith.botanicadds.util.MaterialType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface ItemsBA
{
	@RegistryName("rune_tp")
	Item RUNE_TP = newItem();
	@RegistryName("rune_energy")
	Item RUNE_ENERGY = newItem();
	@RegistryName("mana_lapis")
	Item MANA_LAPIS = newItem();
	@RegistryName("elven_lapis")
	Item ELVEN_LAPIS = newItem();
	@RegistryName("gaia_shard")
	Item GAIA_SHARD = newItem();
	
	@RegistryName("gaiasteel_ingot")
	ItemMaterial GAIASTEEL_INGOT = new ItemMaterial(MaterialType.INGOT, "gaiasteel");
	
	@RegistryName("gaiasteel_nugget")
	ItemMaterial GAIASTEEL_NUGGET = new ItemMaterial(MaterialType.NUGGET, "gaiasteel");
	
	static Item newItem()
	{
		return new Item(new Item.Properties().tab(BotanicAdditions.TAB));
	}
}