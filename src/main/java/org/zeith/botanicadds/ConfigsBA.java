package org.zeith.botanicadds;

import org.zeith.hammerlib.api.config.*;

@Config
public class ConfigsBA
		implements IConfigRoot
{
	public static ConfigHolder<ConfigsBA> INSTANCE = new ConfigHolder<>();
	
	@Config.ConfigEntry(entry = "Client", comment = "Visual tweaks of the mod")
	public final ConfigBAClient client = new ConfigBAClient();
	
	@Config.ConfigEntry(entry = "Gameplay", comment = "Tweaks to gameplay and balance")
	public final ConfigBAGameplay gameplay = new ConfigBAGameplay();
	
	public static class ConfigBAClient
			implements IConfigStructure
	{
		@Config.ConfigEntry(entry = "Max Energizera Particle Soft", comment =
				"Soft max value for the energizera's bolt particle count.\n" +
				"When this limit will be exceeded, bolts will be simplified into a straight line to save on performance.\n" +
				"Setting to 0 will make all bolts simplified."
		)
		@Config.IntEntry(min = 0, value = 10)
		public int energizeraMaxBoltCountSoft;
		
		@Config.ConfigEntry(entry = "Max Energizera Particle Hard", comment =
				"Hard max value for the energizera's bolt particle count.\n" +
				"When this limit will be reached, no new bolts will be spawned.\n" +
				"Setting to 0 will disable bolts completely."
		)
		@Config.IntEntry(min = 0, value = 100)
		public int energizeraMaxBoltCountHard;
	}
	
	public static class ConfigBAGameplay
			implements IConfigStructure
	{
		@Config.ConfigEntry(entry = "Energizera Rate", comment = "How much FE will be converted into a single unit of mana?")
		@Config.IntEntry(min = 1, value = 10)
		public int energizeraRate;
		
		@Config.ConfigEntry(entry = "Energizera Max Mana Pull", comment =
				"How much FE (in mana equivalent) will be attempted to be extracted from nearby tiles?\n" +
				"1 mana will mean 10 FE extracted (default configs) per operation.\n" +
				"Bigger numbers make the flower pull more FE per operation and thus go on cooldown for longer, saving on server performance."
		)
		@Config.IntEntry(min = 1, value = 5, max = 1000)
		public int energizeraMaxPull;
	}
}
