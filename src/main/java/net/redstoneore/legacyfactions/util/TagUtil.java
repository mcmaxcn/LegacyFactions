package net.redstoneore.legacyfactions.util;


import mkremins.fanciful.FancyMessage;
import net.redstoneore.legacyfactions.Factions;
import net.redstoneore.legacyfactions.entity.Conf;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.entity.FactionColl;

import org.bukkit.ChatColor;

import static net.redstoneore.legacyfactions.util.TagReplacerUtil.TagType;

import java.util.ArrayList;
import java.util.List;

public class TagUtil {

    private static final int ARBITRARY_LIMIT = 25000;

    /**
     * Replaces all variables in a plain raw line for a faction
     *
     * @param faction for faction
     * @param line    raw line from config with variables to replace for
     *
     * @return clean line
     */
    public static String parsePlain(Faction faction, String line) {
        for (TagReplacerUtil tagReplacer : TagReplacerUtil.getByType(TagType.FACTION)) {
            if (tagReplacer.contains(line)) {
                line = tagReplacer.replace(line, tagReplacer.getValue(faction, null));
            }
        }
        return line;
    }

    /**
     * Replaces all variables in a plain raw line for a player
     *
     * @param fplayer for player
     * @param line    raw line from config with variables to replace for
     *
     * @return clean line
     */
    public static String parsePlain(FPlayer fplayer, String line) {
        for (TagReplacerUtil tagReplacer : TagReplacerUtil.getByType(TagType.PLAYER)) {
            if (tagReplacer.contains(line)) {
                String rep = tagReplacer.getValue(fplayer.getFaction(), fplayer);
                if (rep == null) {
                    rep = ""; // this should work, but it's not a good way to handle whatever is going wrong
                }
                line = tagReplacer.replace(line, rep);
            }
        }
        return line;
    }

    /**
     * Replaces all variables in a plain raw line for a faction, using relations from fplayer
     *
     * @param faction for faction
     * @param fplayer from player
     * @param line    raw line from config with variables to replace for
     *
     * @return clean line
     */
    public static String parsePlain(Faction faction, FPlayer fplayer, String line) {
        for (TagReplacerUtil tagReplacer : TagReplacerUtil.getByType(TagType.PLAYER)) {
            if (tagReplacer.contains(line)) {
                String value = tagReplacer.getValue(faction, fplayer);
                if (value != null) {
                    line = tagReplacer.replace(line, value);
                } else {
                    return null; // minimal show, entire line to be ignored
                }
            }
        }
        return line;
    }

    /**
     * Scan a line and parse the fancy variable into a fancy list
     *
     * @param faction for faction (viewers faction)
     * @param fme     for player (viewer)
     * @param line    fancy message prefix
     *
     * @return
     */
    public static List<FancyMessage> parseFancy(Faction faction, FPlayer fme, String line) {
        for (TagReplacerUtil tagReplacer : TagReplacerUtil.getByType(TagType.FANCY)) {
            if (tagReplacer.contains(line)) {
                String clean = line.replace(tagReplacer.getTag(), ""); // remove tag
                return getFancy(faction, fme, tagReplacer, clean);
            }
        }
        return null;
    }

    /**
     * Checks if a line has fancy variables
     *
     * @param line raw line from config with variables
     *
     * @return if the line has fancy variables
     */
    public static boolean hasFancy(String line) {
        for (TagReplacerUtil tagReplacer : TagReplacerUtil.getByType(TagType.FANCY)) {
            if (tagReplacer.contains(line)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lets get fancy.
     *
     * @param target Faction to get relate from
     * @param fme    Player to relate to
     * @param prefix First part of the fancy message
     *
     * @return list of fancy messages to send
     */
    protected static List<FancyMessage> getFancy(Faction target, FPlayer fme, TagReplacerUtil type, String prefix) {
        List<FancyMessage> fancyMessages = new ArrayList<FancyMessage>();

        switch (type) {
            case ALLIES_LIST:
                FancyMessage currentAllies = Factions.get().getTextUtil().parseFancy(prefix);
                boolean firstAlly = true;
                for (Faction otherFaction : FactionColl.get().getAllFactions()) {
                    if (otherFaction == target) {
                        continue;
                    }
                    String s = otherFaction.getTag(fme);
                    if (otherFaction.getRelationTo(target).isAlly()) {
                        currentAllies.then(firstAlly ? s : ", " + s);
                        currentAllies.tooltip(tipFaction(otherFaction)).color(fme.getColorTo(otherFaction));
                        firstAlly = false;
                        if (currentAllies.toJSONString().length() > ARBITRARY_LIMIT) {
                            fancyMessages.add(currentAllies);
                            currentAllies = new FancyMessage("");
                        }
                    }
                }
                fancyMessages.add(currentAllies);
                return firstAlly && Conf.showMinimal ? null : fancyMessages; // we must return here and not outside the switch
            case TRUCES_LIST:
                FancyMessage currentTruces = Factions.get().getTextUtil().parseFancy(prefix);
                boolean firstTruce = true;
                for (Faction otherFaction : FactionColl.get().getAllFactions()) {
                    if (otherFaction == target) {
                        continue;
                    }
                    String s = otherFaction.getTag(fme);
                    if (otherFaction.getRelationTo(target).isTruce()) {
                        currentTruces.then(firstTruce ? s : ", " + s);
                        currentTruces.tooltip(tipFaction(otherFaction)).color(fme.getColorTo(otherFaction));
                        firstTruce = false;
                        if (currentTruces.toJSONString().length() > ARBITRARY_LIMIT) {
                            fancyMessages.add(currentTruces);
                            currentTruces = new FancyMessage("");
                        }
                    }
                }
                fancyMessages.add(currentTruces);
                return firstTruce && Conf.showMinimal ? null : fancyMessages; // we must return here and not outside the switch
            case ENEMIES_LIST:
                FancyMessage currentEnemies = Factions.get().getTextUtil().parseFancy(prefix);
                boolean firstEnemy = true;
                for (Faction otherFaction : FactionColl.get().getAllFactions()) {
                    if (otherFaction == target) {
                        continue;
                    }
                    String s = otherFaction.getTag(fme);
                    if (otherFaction.getRelationTo(target).isEnemy()) {
                        currentEnemies.then(firstEnemy ? s : ", " + s);
                        currentEnemies.tooltip(tipFaction(otherFaction)).color(fme.getColorTo(otherFaction));
                        firstEnemy = false;
                        if (currentEnemies.toJSONString().length() > ARBITRARY_LIMIT) {
                            fancyMessages.add(currentEnemies);
                            currentEnemies = new FancyMessage("");
                        }
                    }
                }
                fancyMessages.add(currentEnemies);
                return firstEnemy && Conf.showMinimal ? null : fancyMessages; // we must return here and not outside the switch
            case ONLINE_LIST:
                FancyMessage currentOnline = Factions.get().getTextUtil().parseFancy(prefix);
                boolean firstOnline = true;
                for (FPlayer p : MiscUtil.rankOrder(target.getWhereOnline(true))) {
                    String name = p.getNameAndTitle();
                    currentOnline.then(firstOnline ? name : ", " + name);
                    currentOnline.tooltip(tipPlayer(p)).color(fme.getColorTo(p));
                    firstOnline = false;
                    if (currentOnline.toJSONString().length() > ARBITRARY_LIMIT) {
                        fancyMessages.add(currentOnline);
                        currentOnline = new FancyMessage("");
                    }
                }
                fancyMessages.add(currentOnline);
                return firstOnline && Conf.showMinimal ? null : fancyMessages; // we must return here and not outside the switch
            case OFFLINE_LIST:
                FancyMessage currentOffline = Factions.get().getTextUtil().parseFancy(prefix);
                boolean firstOffline = true;
                for (FPlayer p : MiscUtil.rankOrder(target.getMembers())) {
                    String name = p.getNameAndTitle();
                    if (!p.isOnline()) {
                        currentOffline.then(firstOffline ? name : ", " + name);
                        currentOffline.tooltip(tipPlayer(p)).color(fme.getColorTo(p));
                        firstOffline = false;
                        if (currentOffline.toJSONString().length() > ARBITRARY_LIMIT) {
                            fancyMessages.add(currentOffline);
                            currentOffline = new FancyMessage("");
                        }
                    }
                }
                fancyMessages.add(currentOffline);
                return firstOffline && Conf.showMinimal ? null : fancyMessages; // we must return here and not outside the switch
               default:
                return null;
        }
    }

    /**
     * Parses tooltip variables from config <br> Supports variables for factions only (type 2)
     *
     * @param faction faction to tooltip for
     *
     * @return list of tooltips for a fancy message
     */
    private static List<String> tipFaction(Faction faction) {
        List<String> lines = new ArrayList<String>();
        for (String line : Conf.tooltips.get("list")) {
            lines.add(ChatColor.translateAlternateColorCodes('&', TagUtil.parsePlain(faction, line)));
        }
        return lines;
    }

    /**
     * Parses tooltip variables from config <br> Supports variables for players and factions (types 1 and 2)
     *
     * @param fplayer player to tooltip for
     *
     * @return list of tooltips for a fancy message
     */
    private static List<String> tipPlayer(FPlayer fplayer) {
        List<String> lines = new ArrayList<String>();
        for (String line : Conf.tooltips.get("show")) {
            lines.add(ChatColor.translateAlternateColorCodes('&', TagUtil.parsePlain(fplayer, line)));
        }
        return lines;
    }
}
