package org.zeith.botanicadds.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.items.*;
import org.zeith.botanicadds.util.MaterialType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

import java.util.function.UnaryOperator;

@SimplyRegister
public interface ItemsBA
{
	@RegistryName("rune_tp")
	Item RUNE_TP = newItem();
	@RegistryName("rune_energy")
	Item RUNE_ENERGY = newItem();
	@RegistryName("gaia_shard")
	Item GAIA_SHARD = newItem(p -> p.rarity(Rarity.RARE));
	
	@RegistryName("mana_lapis")
	ItemMaterial MANA_LAPIS = new ItemMaterial(MaterialType.GEM, "mana_lapis");
	
	@RegistryName("elven_lapis")
	ItemMaterial ELVEN_LAPIS = new ItemMaterial(MaterialType.GEM, "elven_lapis");
	
	@RegistryName("gaiasteel_ingot")
	ItemMaterial GAIASTEEL_INGOT = new ItemMaterial(MaterialType.INGOT, "gaiasteel");
	
	@RegistryName("gaiasteel_nugget")
	ItemMaterial GAIASTEEL_NUGGET = new ItemMaterial(MaterialType.NUGGET, "gaiasteel");
	
	@RegistryName("aura_ring_gaia")
	ItemGaiaAuraRing AURA_RING_GAIA = new ItemGaiaAuraRing();
	
	@RegistryName("mana_ring_gaia")
	ItemGaiaManaBand MANA_RING_GAIA = new ItemGaiaManaBand();
	
	static Item newItem()
	{
		return new Item(new Item.Properties().tab(BotanicAdditions.TAB));
	}
	
	static Item newItem(UnaryOperator<Item.Properties> props)
	{
		return new Item(props.apply(new Item.Properties().tab(BotanicAdditions.TAB)));
	}
}