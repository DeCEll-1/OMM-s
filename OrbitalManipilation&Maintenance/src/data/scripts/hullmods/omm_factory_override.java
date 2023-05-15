package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.MilitarizedSubsystems;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class omm_factory_override extends BaseHullMod {

    private static final String HULLMOD_ID = "omm_factory_override";
    private static final Map coom = new HashMap();

    static {
        coom.put(HullSize.FIGHTER, 100f);
        coom.put(HullSize.FRIGATE, 100f);
        coom.put(HullSize.DESTROYER, 100f);
        coom.put(HullSize.CRUISER, 100f);
        coom.put(HullSize.CAPITAL_SHIP, 100f);
        coom.put(HullSize.DEFAULT, 100f);
    }
    //This above is kinda important, you have to define HullSize.FIGHTER and HullSize.DEFAULT because for some reason people are spawning old precursor fighters and the mod is randomly summoning these cringe gargoyles and CTDing the game. If you don't want them to get the bonus, I would just set it to 0f or something...
    private static float FLUX_DISSIPATION_PERCENT = 10.0F;

    private static float ARMOR_BONUS = 10.0F;

    public static float FLUX_CAPACITY_PERCENT = 10.0F;

    public static float HULL_PERCENT = 10.0F;

    public static float ARMOR_PERCENT = 5.0F;

    private static float MANEUVER_PERCENT = 10.0F;

    private static float PD_RANGE = 30.0F;

    public static float FIGHTER_DAMAGE_BONUS = 10.0F;

    public static float MISSILE_DAMAGE_BONUS = 10.0F;

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        float mult = MilitarizedSubsystems.getEffectMult(stats);
        if (stats.getVariant().hasHullMod("militarized_subsystems")) {

            stats.getFluxDissipation().modifyPercent(id, FLUX_DISSIPATION_PERCENT * mult);
            stats.getEffectiveArmorBonus().modifyFlat(id, ARMOR_BONUS * mult);
        }
        if (stats.getVariant().hasHullMod("assault_package")) {
            stats.getHullBonus().modifyPercent(id, HULL_PERCENT * mult);
            stats.getArmorBonus().modifyPercent(id, ARMOR_PERCENT * mult);
            stats.getFluxCapacity().modifyPercent(id, FLUX_CAPACITY_PERCENT * mult);
        }
        if (stats.getVariant().hasHullMod("escort_package")) {
            stats.getDamageToFighters().modifyFlat(id, FIGHTER_DAMAGE_BONUS / 100.0F * mult);
            stats.getDamageToMissiles().modifyFlat(id, MISSILE_DAMAGE_BONUS / 100.0F * mult);
            stats.getBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE * mult);
            stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE * mult);
            stats.getAcceleration().modifyPercent(id, MANEUVER_PERCENT * mult);
            stats.getDeceleration().modifyPercent(id, MANEUVER_PERCENT * mult);
            stats.getTurnAcceleration().modifyPercent(id, MANEUVER_PERCENT * 2.0F * mult);
            stats.getMaxTurnRate().modifyPercent(id, MANEUVER_PERCENT * mult);

        }
    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
//        if (!"freitag_corporation".equals(Misc.getCommissionFactionId())) {
//            ship.getVariant().removeMod("omm_factory_override");
//        }
//        if (ship.getVariant().hasHullMod("CHM_commission")) {
//            ship.getVariant().removeMod("CHM_commission");
//        }
    }

    public String getDescriptionParam(int index, HullSize hullSize) {

        return null;

    }

    //Oh these are cool colors below introduced in 0.95a, to match with your tech type and stuff. Just nice to have!
    public Color getBorderColor() {
        return new Color(255, 255, 255, 100);
    }

    public Color getNameColor() {
        return new Color(255, 166, 0, 255);
    }
}
