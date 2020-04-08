package tk.zeitheron.botanicadds.init;

import net.minecraft.item.Item;
import tk.zeitheron.botanicadds.items.ItemGaiaAuraRing;
import tk.zeitheron.botanicadds.items.ItemManaStealerSword;
import tk.zeitheron.botanicadds.items.ItemOD;
import tk.zeitheron.botanicadds.items.ItemTerraProtector;

public class ItemsBA
{
	public static final Item RUNE_TP = new Item().setTranslationKey("rune_tp");
	public static final Item RUNE_ENERGY = new Item().setTranslationKey("rune_energy");
	public static final Item MANA_LAPIS = new Item().setTranslationKey("mana_lapis");
	public static final Item ELVEN_LAPIS = new Item().setTranslationKey("elven_lapis");
	public static final Item GAIA_SHARD = new Item().setTranslationKey("gaia_shard");
	public static final Item GAIASTEEL_INGOT = new ItemOD("gaiasteel_ingot", "ingotGaiasteel");
	public static final ItemTerraProtector TERRA_PROTECTOR = new ItemTerraProtector();
	public static final ItemGaiaAuraRing RING_AURA_GAIA = new ItemGaiaAuraRing();
	public static final ItemManaStealerSword MANA_STEALER_SWORD = new ItemManaStealerSword();
}