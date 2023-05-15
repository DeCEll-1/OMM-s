package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class omm_mainframelink extends BaseHullMod {

//    public static float FLUX_FRACTION = 0.5F;
//
//    public static float HARD_FLUX_FRACTION = 0.2F;
//
//    public static String SINK_DATA_KEY = "core_sink_data_key";
//
//    public static class FluxSinkData {
//
//        Map<ShipAPI, Float> dissipation = new LinkedHashMap<ShipAPI, Float>();
//    }
//
//    public void advanceInCombat(ShipAPI ship, float amount) {
//        super.advanceInCombat(ship, amount);
//        if (!ship.isAlive()) {
//            return;
//        }
//        CombatEngineAPI engine = Global.getCombatEngine();
//        String key = String.valueOf(SINK_DATA_KEY) + "_" + ship.getId();
//        FluxSinkData data = (FluxSinkData) engine.getCustomData().get(key);
//        if (data == null) {
//            data = new FluxSinkData();
//            engine.getCustomData().put(key, data);
//            for (FighterWingAPI wings : ship.getAllWings()) {
//                ShipAPI fighter = wings.getLeader();
//                if (!fighter.isAlive()) {
//                    continue;
//                }
//
//                float d = fighter.getMutableStats().getFluxDissipation().getModifiedValue();
//                d *= FLUX_FRACTION;
//                data.dissipation.put(fighter, Float.valueOf(d));
//            }
//        }
//        List<ShipAPI> losses = new ArrayList<ShipAPI>(data.dissipation.keySet());
//        List<ShipAPI> remaining = new ArrayList<ShipAPI>();
//        float totalLiveDissipation = 0.0F;
//            for (FighterWingAPI wings : ship.getAllWings()) {
//                ShipAPI fighter = wings.getLeader();
//                if (!fighter.isAlive()) {
//                    continue;
//                }
//            losses.remove(fighter);
//            remaining.add(fighter);
//            if (data.dissipation.containsKey(fighter)) {
//                totalLiveDissipation += ((Float) data.dissipation.get(fighter)).floatValue();
//            }
//        }
//        float extraDissipation = 0.0F;
//        for (ShipAPI lost : losses) {
//            if (data.dissipation.containsKey(lost)) {
//                extraDissipation += ((Float) data.dissipation.get(lost)).floatValue();
//            }
//        }
//        for (ShipAPI fighter : remaining) {
//            if (!data.dissipation.containsKey(fighter)) {
//                continue;
//            }
//            float currBonus = 0.0F;
//            if (totalLiveDissipation > 0.0F) {
//                currBonus = ((Float) data.dissipation.get(fighter)).floatValue();
//            }
//            float flux = fighter.getCurrFlux();
//            float motherflux = ship.getCurrFlux();
//            float fluxtotal = flux + motherflux;
//            ship.getFluxTracker().increaseFlux(flux, false);
//            fighter.getMutableStats().getFluxDissipation().modifyFlat("shared_flux_sink", currBonus);
//            float hardFluxFraction = 0.0F;
//            float totalDissipation = fighter.getMutableStats().getFluxDissipation().getModifiedValue();
//            if (totalDissipation > 0.0F) {
//                hardFluxFraction = currBonus / totalDissipation * HARD_FLUX_FRACTION;
//            }
//            fighter.getMutableStats().getHardFluxDissipationFraction().modifyFlat("shared_flux_sink", hardFluxFraction);
//        }
//    }
//
//    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize, ShipAPI ship) {
//        if (index == 0) {
//            return Math.round(FLUX_FRACTION * 100.0F) + "%";
//        }
//        if (index == 1) {
//            return Math.round(HARD_FLUX_FRACTION * 100.0F) + "%";
//        }
//        return null;
//    }
}
