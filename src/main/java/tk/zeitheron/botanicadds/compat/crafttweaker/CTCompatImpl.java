package tk.zeitheron.botanicadds.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import tk.zeitheron.botanicadds.compat.crafttweaker.core.ICTCompat;

import java.util.LinkedList;

public class CTCompatImpl implements ICTCompat
{
	private static final LinkedList<IAction> lateActions = new LinkedList<>();
	
	@Override
	public void onLoadComplete()
	{
		lateActions.forEach(CraftTweakerAPI::apply);
		lateActions.clear();
	}
	
	@Override
	public void init()
	{
		lateActions.forEach(CraftTweakerAPI::apply);
		lateActions.clear();
	}
	
	@Override
	public void addLateAction(Object action)
	{
		lateActions.addLast((IAction) action);
	}
}