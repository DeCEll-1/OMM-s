package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import org.apache.log4j.Logger;

public class omm_FighterWeaponReplacerFromShipWeaponSlotID extends BaseHullMod {

    private static final Logger log = Global.getLogger(omm_FighterWeaponReplacerFromShipWeaponSlotID.class);

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

        for (WeaponAPI weapon : ship.getAllWeapons()) {//gets weapons of the ship
//                throw new RuntimeException("amogus");
            for (FighterLaunchBayAPI launchBay : ship.getLaunchBaysCopy()) {//gets launch bays of the ship
                for (WeaponAPI wingWeapon : launchBay.getShip().getAllWeapons()) {//gets weapons on the leader wing from launch bay
                    if (weapon.getSlot().isSystemSlot() || weapon.getSlot().isDecorative() || weapon.getSlot().isBuiltIn() || weapon.getSlot().isStationModule()) {
                    } else {
                        if (weapon.getSlot().getId().equals(wingWeapon.getSlot().getId())) {//check if any of the wings weapon slots ids are equal to any of the ships slot ids
//                        for (ShipAPI wingMember : launchBay.getWing().getWingMembers()) {//gets the list of members in wing
                            launchBay.getShip().getMutableStats().getVariant().addWeapon(weapon.getSlot().getId(), weapon.getId());//replaces the weapons of fighters
//                        }
                        }
                    }
                }
            }
        }


    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        try {
            if (ship != null) {
                this.applyEffectsAfterShipCreation(ship, ship.getId());
                for (FighterLaunchBayAPI launchBay : ship.getLaunchBaysCopy()) {
                    launchBay.getWing().orderReturn(ship);
                }
            }
        } catch (Exception ex) {
            return;
        }
    }


}
