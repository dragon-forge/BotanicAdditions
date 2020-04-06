package tk.zeitheron.botanicadds.utils;

import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.mana.IManaBlock;

public interface ILastManaAknowledgedTile extends IManaBlock
{
	int getLastKnownMana();
	
	void setLastKnownMana(int mana);
	
	BlockPos pos();
}