package net.redstoneore.legacyfactions.lang;

import net.redstoneore.legacyfactions.Factions;
import net.redstoneore.legacyfactions.entity.FPlayer;

public class LangBuilder {

	public LangBuilder(String message) {
		this.message = message;
	}
	
	private String message;
	
	public LangBuilder replace(String find, Object replace) {
		this.message = this.message.replace(find, replace.toString());
		return this;
	}
	
	public String toString() {
		return this.message;
	}
	
	public LangBuilder parse() {
		this.message = Factions.get().getTextUtil().parse(this.message);
		return this;
	}
	
	public LangBuilder sendTo(FPlayer fplayer) {
		fplayer.sendMessage(this.message);
		return this;
	}
	
	public LangBuilder sendToParsed(FPlayer fplayer) {
		fplayer.sendMessage(Factions.get().getTextUtil().parse(this.message));
		return this;
	}
	
}
