package tk.zeitheron.botanicadds.compat.crafttweaker.core;

import java.util.Objects;

import crafttweaker.IAction;

public abstract class BaseAction implements IAction
{
	protected final String name;
	protected final Runnable apply;
	
	protected BaseAction(String name, Runnable apply)
	{
		this.name = name;
		this.apply = apply;
	}
	
	protected String getRecipeInfo()
	{
		return "Unnamed";
	}
	
	@Override
	public String describe()
	{
		return String.format("Altering %s Recipe(s) for %s", name, getRecipeInfo());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(!(obj instanceof BaseAction))
			return false;
		return Objects.equals(((BaseAction) obj).name, name);
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public void apply()
	{
		apply.run();
	}
}