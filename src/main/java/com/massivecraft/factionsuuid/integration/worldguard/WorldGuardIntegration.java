package com.massivecraft.factionsuuid.integration.worldguard;

import org.bukkit.Bukkit;

import com.massivecraft.factionsuuid.Factions;
import com.massivecraft.factionsuuid.integration.Integration;

public class WorldGuardIntegration extends Integration {
	
	private static WorldGuardIntegration i = new WorldGuardIntegration();
	public static WorldGuardIntegration get() { return i; }
	
	private String pluginName = "WorldGuard";
	
	@Override
	public boolean isEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled(this.pluginName);
	}

	@Override
	public void init() {
		WorldGuardEngine.init(Factions.get());
	}
	
}