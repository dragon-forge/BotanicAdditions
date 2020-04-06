package tk.zeitheron.botanicadds.compat.crafttweaker.modules;

import com.zeitheron.hammercore.utils.ArrayHelper;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tk.zeitheron.botanicadds.api.GaiaPlateRecipes;
import tk.zeitheron.botanicadds.compat.crafttweaker.CraftTweakerCompat;
import tk.zeitheron.botanicadds.compat.crafttweaker.core.BaseAction;
import tk.zeitheron.botanicadds.compat.crafttweaker.core.InputHelper;

import java.util.stream.Collectors;

@ZenClass("mods.botanicadds.GaiaPlate")
@ZenRegister
public class GaiaPlate
{
	@ZenMethod
	public static void add(IItemStack output, int mana, IItemStack... inputs)
	{
		Object[] inputsObj = ArrayHelper.transform(inputs, InputHelper::toStack, ItemStack[]::new);
		CraftTweakerCompat.compat().addLateAction(new Add(new GaiaPlateRecipes.RecipeGaiaPlate(InputHelper.toStack(output), mana, inputsObj)));
	}

	@ZenMethod
	public static void remove(IItemStack output)
	{
		ItemStack out = InputHelper.toStack(output);
		CraftTweakerCompat.compat().addLateAction(new Remove(out));
	}

	private static final class Add
			extends BaseAction
	{
		private Add(GaiaPlateRecipes.RecipeGaiaPlate rec)
		{
			super("GaiaPlate", () -> GaiaPlateRecipes.gaiaRecipes.add(rec));
		}
	}

	private static final class Remove
			extends BaseAction
	{
		private Remove(ItemStack rec)
		{
			super("GaiaPlate", () -> GaiaPlateRecipes.gaiaRecipes
					.stream()
					.filter(r -> rec.isItemEqual(r.getOutput()))
					.collect(Collectors.toList())
					.forEach(GaiaPlateRecipes.gaiaRecipes::remove));
		}
	}
}