package net.redstoneore.legacyfactions.scoreboards.sidebar;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.redstoneore.legacyfactions.entity.Conf;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.scoreboards.FSidebarProvider;

public class FInfoSidebar extends FSidebarProvider {

	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //
	
	public FInfoSidebar(Faction faction) {
		this.faction = faction;
	}
	
	// -------------------------------------------------- //
 	// FIELDS
 	// -------------------------------------------------- //

	private final Faction faction;

	// -------------------------------------------------- //
 	// METHODS
 	// -------------------------------------------------- //
 	
	@Override
	public String getTitle(FPlayer fplayer) {
		return this.faction.getRelationTo(fplayer).getColor() + this.faction.getTag();
	}

	@Override
	public List<String> getLines(FPlayer fplayer) {
		List<String> lines = new ArrayList<>(Conf.scoreboardInfo);
		ListIterator<String> it = lines.listIterator();
		while (it.hasNext()) {
			it.set(replaceTags(this.faction, fplayer, it.next()));
		}
		return lines;
	}
	
}
