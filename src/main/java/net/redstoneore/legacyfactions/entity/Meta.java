package net.redstoneore.legacyfactions.entity;

import net.redstoneore.legacyfactions.Factions;

public class Meta {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static transient Meta instance = new Meta();
	public static Meta get() { return instance; }
	
	// -------------------------------------------------- //
	// FIELDS
	// -------------------------------------------------- //
	
	public double configVersion = Conf.version;
	
	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //

	public void load() {
		Factions.get().getPersist().loadOrSaveDefault(instance, Meta.class, "database/meta");
	}

	public void save() {
		Factions.get().getPersist().save(instance, "database/meta");
	}
	
}
