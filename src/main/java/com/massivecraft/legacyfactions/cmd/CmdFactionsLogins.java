package com.massivecraft.legacyfactions.cmd;

import com.massivecraft.legacyfactions.Permission;
import com.massivecraft.legacyfactions.TL;

public class CmdFactionsLogins extends FCommand {

    public CmdFactionsLogins() {
        super();
        this.aliases.add("login");
        this.aliases.add("logins");
        this.aliases.add("logout");
        this.aliases.add("logouts");
        this.senderMustBePlayer = true;
        this.senderMustBeMember = true;
        this.permission = Permission.MONITOR_LOGINS.node;
    }

    @Override
    public void perform() {
        boolean monitor = fme.isMonitoringJoins();
        fme.msg(TL.COMMAND_LOGINS_TOGGLE, String.valueOf(!monitor));
        fme.setMonitorJoins(!monitor);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_LOGINS_DESCRIPTION;
    }
}