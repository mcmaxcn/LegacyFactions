package com.massivecraft.legacyfactions.cmd;

import com.massivecraft.legacyfactions.Relation;

public class CmdRelationEnemy extends FCommandRelation {

    public CmdRelationEnemy() {
        aliases.add("enemy");
        targetRelation = Relation.ENEMY;
    }
}
