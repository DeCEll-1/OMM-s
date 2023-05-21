package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lazywizard.lazylib.MathUtils;

public class omm_nanite_buff extends BaseShipSystemScript {

    private static CombatEngineAPI engine = null;

    private static final float RANGE = 200.0F;

    private static final float DISSIPATION_BONUS = +100.0F;

    private static final float REPAIR_BONUS = -50.0F;

    private static final Map<ShipAPI, ShipAPI> buffing = new HashMap<>();

    private static final String staticID = "nanite_buff";

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        if (engine != Global.getCombatEngine()) {
            engine = Global.getCombatEngine();
            buffing.clear();
        }
        ShipAPI host_ship = (ShipAPI) stats.getEntity();
        for (ShipAPI ship : engine.getShips()) {
            if (!ship.isAlive()) {
                continue;
            }
            if (ship == host_ship) {
                continue;
            }
            if (host_ship.getOwner() == ship.getOwner() && MathUtils.getDistance((CombatEntityAPI) ship, (CombatEntityAPI) host_ship) <= 1200.0F) {
                ship.getMutableStats().getFluxDissipation().modifyFlat("nanite_buff", +100.0F);
                ship.getMutableStats().getCombatEngineRepairTimeMult().modifyPercent("nanite_buff", -50.0F);
                ship.getMutableStats().getCombatWeaponRepairTimeMult().modifyPercent("nanite_buff", -50.0F);
                buffing.put(ship, host_ship);
                continue;
            }
//            if (buffing.containsKey(ship) && buffing.get(ship) == host_ship) {
//                ship.getMutableStats().getFluxDissipation().unmodify("nanite_buff");
//                ship.getMutableStats().getCombatEngineRepairTimeMult().unmodify("nanite_buff");
//                ship.getMutableStats().getCombatWeaponRepairTimeMult().unmodify("nanite_buff");
//                buffing.remove(ship);
//            }
        }
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        ShipAPI host_ship = (ShipAPI) stats.getEntity();
        Iterator<Map.Entry<ShipAPI, ShipAPI>> iter = buffing.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<ShipAPI, ShipAPI> entry = iter.next();
            ShipAPI ship = entry.getKey();
            if (entry.getValue() == host_ship) {
                ship.getMutableStats().getFluxDissipation().unmodify("nanite_buff");
                ship.getMutableStats().getCombatEngineRepairTimeMult().unmodify("nanite_buff");
                ship.getMutableStats().getCombatWeaponRepairTimeMult().unmodify("nanite_buff");
                iter.remove();
            }
        }
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        if (index == 0) {
            return new ShipSystemStatsScript.StatusData("improving weapon and engine repair rate and flux dissipation", false);
        }
        return null;
    }
}
