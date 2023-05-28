package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.combat.entities.Ship;
import org.apache.log4j.Logger;

public class omm_FighterWeaponReplacerFromShipWeaponSlotID extends BaseHullMod {

    public boolean DoOnce = true;


    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {


    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {

//        try {


            for (WeaponAPI weapon : ship.getAllWeapons()) {//gets weapons of the ship
//                throw new RuntimeException("amogus");
//                for (FighterLaunchBayAPI launchBay : ship.getLaunchBaysCopy()) {//gets launch bays of the ship //unused


                for (FighterWingAPI wing : ship.getAllWings()) {//gets the wings
                    for (WeaponAPI wingWeapon : wing.getLeader().getAllWeapons()) {//gets the weapon of the fighter

                        for (WeaponAPI shipWeapon : ship.getAllWeapons()) {//gets the weapon of the ship

                            if (shipWeapon.getSlot().getId().equals(wingWeapon.getSlot().getId())) {//checks if the ids are the same
//                            wing.getLeader().getMutableStats().getVariant().clearSlot(wingWeapon.getId());//removes the weapon
                                wing.getLeader().getMutableStats().getVariant().addWeapon(wingWeapon.getSlot().getId(), shipWeapon.getId());//adds the weapon

                                wing.getLeader().getFleetMember().setVariant(wing.getLeader().getVariant(), false, true);
                            }

                        }

                    }

                }

//                }

        }
        //  } catch (Exception ex) {
        //          return;
        //     }


        //        try {
//
//            if (ship != null) {
////                this.applyEffectsAfterShipCreation(ship, ship.getId());
//                for (FighterLaunchBayAPI launchBay : ship.getLaunchBaysCopy()) {
//
//                    for (ShipAPI wing : launchBay.getWing().getWingMembers()) {
//                        launchBay.land(wing);
//                    }
//
//                }
//            }
//        } catch (Exception ex) {
//            return;
//        }
    }


}
