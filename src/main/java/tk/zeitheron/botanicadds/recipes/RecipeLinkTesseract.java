package tk.zeitheron.botanicadds.recipes;

import tk.zeitheron.botanicadds.blocks.tiles.TileManaTesseract;
import tk.zeitheron.botanicadds.init.BlocksBA;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeLinkTesseract extends ShapelessRecipes
{
	public RecipeLinkTesseract()
	{
		super("", new ItemStack(BlocksBA.MANA_TESSERACT), items());
	}
	
	static final Item tesseract = Item.getItemFromBlock(BlocksBA.MANA_TESSERACT);
	
	private static final NonNullList<Ingredient> items()
	{
		NonNullList<Ingredient> i = NonNullList.withSize(2, Ingredient.EMPTY);
		i.set(0, Ingredient.fromItem(tesseract));
		i.set(1, new IngredientAnything());
		return i;
	}
	
	@Override
	public boolean isDynamic()
	{
		return true;
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		ItemStack other = ItemStack.EMPTY;
		ItemStack tess = ItemStack.EMPTY;
		
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack it = inv.getStackInSlot(i);
			if(!it.isEmpty())
			{
				if(it.getItem() == tesseract && tess.isEmpty())
				{
					tess = it;
					continue;
				}
				if(other.isEmpty())
					other = it;
				else
					return false;
			}
		}
		
		return !other.isEmpty() && !tess.isEmpty();
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack other = ItemStack.EMPTY;
		ItemStack tess = ItemStack.EMPTY;
		
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack it = inv.getStackInSlot(i);
			if(!it.isEmpty())
			{
				if(it.getItem() == tesseract && tess.isEmpty())
				{
					tess = it;
					continue;
				}
				if(other.isEmpty())
					other = it;
				else
					return ItemStack.EMPTY;
			}
		}
		
		if(!other.isEmpty() && !tess.isEmpty())
		{
			ItemStack tess2 = tess.copy();
			if(!tess2.hasTagCompound())
				tess2.setTagCompound(new NBTTagCompound());
			tess2.getTagCompound().setInteger("Channel", TileManaTesseract.computeHash(other));
			tess2.setCount(1);
			return tess2;
		} else
			return ItemStack.EMPTY;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> its = super.getRemainingItems(inv);
		
		int otheri = 0;
		ItemStack other = ItemStack.EMPTY;
		ItemStack tess = ItemStack.EMPTY;
		
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack it = inv.getStackInSlot(i);
			if(!it.isEmpty())
			{
				if(it.getItem() == tesseract && tess.isEmpty())
				{
					tess = it;
					continue;
				}
				if(other.isEmpty())
				{
					other = it;
					otheri = i;
				}
			}
		}
		
		its.set(otheri, other.copy().splitStack(1));
		
		return its;
	}
}