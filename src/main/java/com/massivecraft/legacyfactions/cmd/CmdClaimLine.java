package com.massivecraft.legacyfactions.cmd;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import com.massivecraft.legacyfactions.FLocation;
import com.massivecraft.legacyfactions.Permission;
import com.massivecraft.legacyfactions.TL;
import com.massivecraft.legacyfactions.entity.Conf;
import com.massivecraft.legacyfactions.entity.Faction;
import com.massivecraft.legacyfactions.event.EventFactionsLandChange;
import com.massivecraft.legacyfactions.event.EventFactionsLandChange.LandChangeCause;

public class CmdClaimLine extends FCommand {

    public static final BlockFace[] axis = {BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST};

    public CmdClaimLine() {

        // Aliases
        this.aliases.add("claimline");
        this.aliases.add("cl");

        // Args
        this.optionalArgs.put("amount", "1");
        this.optionalArgs.put("direction", "facing");
        this.optionalArgs.put("faction", "you");

        this.permission = Permission.CLAIM_LINE.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        // Args
        Integer amount = this.argAsInt(0, 1); // Default to 1

        if (amount > Conf.lineClaimLimit) {
            fme.msg(TL.COMMAND_CLAIMLINE_ABOVEMAX, Conf.lineClaimLimit);
            return;
        }

        String direction = this.argAsString(1);
        BlockFace blockFace;

        if (direction == null) {
            blockFace = axis[Math.round(me.getLocation().getYaw() / 90f) & 0x3];
        } else if (direction.equalsIgnoreCase("north")) {
            blockFace = BlockFace.NORTH;
        } else if (direction.equalsIgnoreCase("east")) {
            blockFace = BlockFace.EAST;
        } else if (direction.equalsIgnoreCase("south")) {
            blockFace = BlockFace.SOUTH;
        } else if (direction.equalsIgnoreCase("west")) {
            blockFace = BlockFace.WEST;
        } else {
            fme.msg(TL.COMMAND_CLAIMLINE_NOTVALID, direction);
            return;
        }

        final Faction forFaction = this.argAsFaction(2, myFaction);
        
        Map<FLocation, Faction> transactions = new HashMap<FLocation, Faction>();
        Location location = me.getLocation();

        for (int i = 0; i < amount; i++) {
        	transactions.put(FLocation.valueOf(location), forFaction);
            location = location.add(blockFace.getModX() * 16, 0, blockFace.getModZ() * 16);
        }
        
        EventFactionsLandChange event = new EventFactionsLandChange(fme, transactions, LandChangeCause.Claim);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        
        for(Entry<FLocation, Faction> claimLocation : event.getTransactions().entrySet()) {
        	if ( ! fme.attemptClaim(claimLocation.getValue(), claimLocation.getKey(), true, event)) {
        		return;
        	}
        }
        
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CLAIMLINE_DESCRIPTION;
    }
}
