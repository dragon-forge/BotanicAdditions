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
	Rarity GAIASTEEL_RARITY = Rarity.create("BotanicAdditionsGaiaSteel", style -> style.withColor(0xFF666D));
	
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
	ItemMaterial GAIASTEEL_INGOT = new ItemMaterial(baseProperties().rarity(GAIASTEEL_RARITY), MaterialType.INGOT, "gaiasteel");
	
	@RegistryName("gaiasteel_nugget")
	ItemMaterial GAIASTEEL_NUGGET = new ItemMaterial(baseProperties().rarity(GAIASTEEL_RARITY), MaterialType.NUGGET, "gaiasteel");
	
	@RegistryName("aura_ring_gaia")
	ItemGaiaAuraRing AURA_RING_GAIA = new ItemGaiaAuraRing();
	
	@RegistryName("mana_ring_gaia")
	ItemGaiaManaBand MANA_RING_GAIA = new ItemGaiaManaBand();
	
	@RegistryName("mana_stealer_sword")
	ItemManaStealerSword MANA_STEALER_SWORD = new ItemManaStealerSword(baseProperties().durability(3000).rarity(GAIASTEEL_RARITY));
	
	static Item newItem()
	{
		return new Item(baseProperties());
	}
	
	static Item newItem(UnaryOperator<Item.Properties> props)
	{
		return new Item(props.apply(baseProperties()));
	}
	
	static Item.Properties baseProperties()
	{
		return new Item.Properties().tab(BotanicAdditions.TAB);
	}
}