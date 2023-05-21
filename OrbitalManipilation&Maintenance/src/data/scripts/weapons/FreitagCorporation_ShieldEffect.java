package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

public class FreitagCorporation_ShieldEffect implements EveryFrameWeaponEffectPlugin {

    private boolean runOnce = false;

    // The weapon who run is the head.
    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) {
            return;
        }
        if (!runOnce) {
            runOnce = true;
            ShipAPI ship = weapon.getShip();
            ShieldAPI shield = ship.getShield();
            if (shield == null) {
                return;
            }
            float radius = ship.getHullSpec().getShieldSpec().getRadius();
            String inner;
            String outer;
            if (radius >= 256.0f) {
                inner = "graphics/fx/FreitagCorporation_shields256.png";
                outer = "graphics/fx/FreitagCorporation_shields256ring.png";
            } else if (radius >= 128.0f) {
                inner = "graphics/fx/FreitagCorporation_shields128.png";
                outer = "graphics/fx/FreitagCorporation_shields128ring.png";
            } else {
                inner = "graphics/fx/FreitagCorporation_shields64.png";
                outer = "graphics/fx/FreitagCorporation_shields64ring.png";
            }
            shield.setRadius(radius, inner, outer);
            shield.setRingRotationRate(shield.getInnerRotationRate());


        }


    }

}
