package com.massivecraft.factionsuuid.cmd;

import com.massivecraft.factionsuuid.Permission;
import com.massivecraft.factionsuuid.TL;
import com.massivecraft.factionsuuid.entity.Faction;
import com.massivecraft.factionsuuid.integration.vault.VaultEngine;

public class CmdMoneyBalance extends FCommand {

    public CmdMoneyBalance() {
        super();
        this.aliases.add("b");
        this.aliases.add("balance");

        //this.requiredArgs.add("");
        this.optionalArgs.put("faction", "yours");

        this.permission = Permission.MONEY_BALANCE.node;
        this.setHelpShort(TL.COMMAND_MONEYBALANCE_SHORT.toString());

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        Faction faction = myFaction;
        if (this.argIsSet(0)) {
            faction = this.argAsFaction(0);
        }

        if (faction == null) {
            return;
        }
        if (faction != myFaction && !Permission.MONEY_BALANCE_ANY.has(sender, true)) {
            return;
        }

        VaultEngine.sendBalanceInfo(fme, faction);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MONEYBALANCE_DESCRIPTION;
    }

}