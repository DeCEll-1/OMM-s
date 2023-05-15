package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;

public class omm_drone_weaponry extends BaseHullMod {

    public void applyEffectsBeforeShipCreation(MutableShipStatsAPI stats, String id) {
    stats.getDynamic().getMod("act_as_combat_ship").modifyFlat(id, 1.0F);
    }
}
