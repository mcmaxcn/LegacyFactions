package com.massivecraft.factionsuuid.cmd;

import com.massivecraft.factionsuuid.Permission;
import com.massivecraft.factionsuuid.TL;
import com.massivecraft.factionsuuid.scoreboards.FScoreboard;

public class CmdSB extends FCommand {

    public CmdSB() {
        this.aliases.add("sb");
        this.aliases.add("scoreboard");
        this.permission = Permission.SCOREBOARD.node;
        this.senderMustBePlayer = true;
    }

    @Override
    public void perform() {
        boolean toggleTo = !fme.showScoreboard();
        FScoreboard board = FScoreboard.get(fme);
        if (board == null) {
            me.sendMessage(TL.COMMAND_TOGGLESB_DISABLED.toString());
        } else {
            me.sendMessage(TL.TOGGLE_SB.toString().replace("{value}", String.valueOf(toggleTo)));
            board.setSidebarVisibility(toggleTo);
        }
        fme.setShowScoreboard(toggleTo);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SCOREBOARD_DESCRIPTION;
    }
}