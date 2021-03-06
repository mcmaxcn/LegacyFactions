package net.redstoneore.legacyfactions.cmd;

import net.redstoneore.legacyfactions.Factions;
import net.redstoneore.legacyfactions.Permission;
import net.redstoneore.legacyfactions.Lang;
import net.redstoneore.legacyfactions.entity.Board;
import net.redstoneore.legacyfactions.entity.CommandAliases;
import net.redstoneore.legacyfactions.entity.Conf;
import net.redstoneore.legacyfactions.entity.FactionColl;

public class CmdFactionsWarunclaimall extends FCommand {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static CmdFactionsWarunclaimall instance = new CmdFactionsWarunclaimall();
	public static CmdFactionsWarunclaimall get() { return instance; }
	
	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //

	private CmdFactionsWarunclaimall() {
		this.aliases.addAll(CommandAliases.cmdAliasesWarunclaimall);
		
		this.permission = Permission.MANAGE_WAR_ZONE.getNode();
		this.disableOnLock = true;

		this.senderMustBePlayer = false;
		this.senderMustBeMember = false;
		this.senderMustBeModerator = false;
		this.senderMustBeColeader = false;
		this.senderMustBeAdmin = false;
	}

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //

	@Override
	public void perform() {
		Board.get().unclaimAll(FactionColl.get().getWarZone().getId());
		sendMessage(Lang.COMMAND_WARUNCLAIMALL_SUCCESS);

		if (Conf.logLandUnclaims) {
			Factions.get().log(Lang.COMMAND_WARUNCLAIMALL_LOG.format(fme.getName()));
		}
	}

	@Override
	public String getUsageTranslation() {
		return Lang.COMMAND_WARUNCLAIMALL_DESCRIPTION.toString();
	}

}
