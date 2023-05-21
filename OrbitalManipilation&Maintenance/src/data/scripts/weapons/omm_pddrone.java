package data.scripts.weapons;

import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponGroupAPI;
import com.fs.starfarer.api.util.IntervalUtil;

import java.awt.Color;
import java.util.List;

public class omm_pddrone implements EveryFrameWeaponEffectPlugin {

    private boolean isWeaponSwapped = false;
    private ShipAPI SHIP;
    private ShipAPI FIGHTER;
    public IntervalUtil timer = new IntervalUtil(3F, 20F);

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        this.SHIP = weapon.getShip();
        if (engine.isPaused()) {
            return;
        }
        List<WeaponGroupAPI> weapons = this.SHIP.getWeaponGroupsCopy();
        List<WeaponAPI> list = this.SHIP.getAllWeapons();
        if (this.SHIP.getOriginalOwner() == 0 || this.SHIP.getOriginalOwner() == 1) {
            for (WeaponAPI weaponAPI : list) {
                if (weaponAPI.getId().equals("omm_pddronedeco")) {

                }

                if (weaponAPI.getSlot().getId().equals("pdslot")) {

                    weaponAPI.disable(true);

                }

                for (int i = 0; i < weapons.size(); i++) {
                    if (weaponAPI.getSlot().getId().equals("pdslot")) {

                        this.SHIP.removeWeaponFromGroups(weaponAPI);
                    }
                }

            }

        }

        this.timer.randomize();

        this.timer.advance(amount);
        if (!this.timer.intervalElapsed()) {
            return;
        }
        if (isWeaponSwapped) {
            return;
        }
        if (!isWeaponSwapped) {

            if (this.SHIP != null) {
                List<FighterWingAPI> list1 = this.SHIP.getAllWings();
                for (FighterWingAPI fighterWingAPI : list1) {
                    if (!fighterWingAPI.getWingId().equals("omm_pddrone_wing")) {
                        continue;
                    }

                    {
                        this.FIGHTER = fighterWingAPI.getLeader();
                        MutableShipStatsAPI mutableShipStatsAPI = this.FIGHTER.getMutableStats();
                        ShipVariantAPI shipVariantAPI = mutableShipStatsAPI.getVariant().clone();
                        this.FIGHTER.getFleetMember().setVariant(shipVariantAPI, false, true);
                        mutableShipStatsAPI.getVariant().clearSlot("pdslot");
                        if (this.SHIP.getVariant().getWeaponSpec("pdslot") != null) {
                            mutableShipStatsAPI.getVariant().addWeapon("pdslot", this.SHIP.getVariant().getWeaponId("pdslot"));
                            mutableShipStatsAPI.getVariant().getWeaponSpec("pdslot").addTag("FIRE_WHEN_INEFFICIENT");
                            fighterWingAPI.orderReturn(this.FIGHTER);

                            this.isWeaponSwapped = true;

                        }

                    }
                }
            }
        }
    }
}
