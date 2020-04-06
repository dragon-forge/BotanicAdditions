package tk.zeitheron.botanicadds.compat.crafttweaker.core;

public interface ICTCompat
{
	void onLoadComplete();
	
	void init();
	
	void addLateAction(Object action);
}