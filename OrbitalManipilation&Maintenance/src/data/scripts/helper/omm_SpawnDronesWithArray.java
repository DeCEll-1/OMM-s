package data.scripts.helper;

import cmu.drones.systems.ForgeSpec;
import cmu.misc.CombatUI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.sun.org.apache.bcel.internal.generic.Select;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;
import cmu.drones.systems.DroneSystem;
import sun.awt.windows.WGlobalCursorManager;
import org.lazywizard.lazylib.combat.CombatUtils;

import java.io.Console;
import java.util.List;

public class omm_SpawnDronesWithArray {

    /**
     * @param ship    ships that will fighter spawn on
     * @param drones  the list of drones to add
     * @param weapons the list of weapons to add on the drone <br>
     *                <b>ONE DRONE FOR ONE WEAPON <br> 1:1 RATİO</b> <br>
     *                might increase the ration if i get autistic enough, and yeah you can only change one (1) slot from the fighter
     */
    public void SpawnDronesWithArrayOnShip(ShipAPI ship, List<String> drones, List<WeaponAPI> weapons) {

        int niyazi = 0;

        for (String wingVariantName : drones) {


            spawnDrone(ship, wingVariantName, weapons.get(niyazi));
            niyazi++;


        }
    }

    /**
     * spawns fighters
     *
     * @param ship           ships that will fighter spawn on
     * @param droneVariantID the drone variant id
     * @param weapon         the weapon to use<br>
     * @param quantity       how many fighters
     */
    public void SpawnDronesWithArrayOnShip(ShipAPI ship, String droneVariantID, WeaponAPI weapon, int quantity) {

        while (quantity != 0) {
            spawnDrone(ship, droneVariantID, weapon);
            quantity--;
        }

    }

    /**
     * only works with one weapon slot might increase it more if i get autistic enough
     *
     * @param ship      ships that will fighter spawn on
     * @param variantId the variant of the fighter NOT the id
     * @param weapon    the weapon fighter will use, the weapon slot needs to be named WS WeaponForWing
     */
    public void spawnDrone(ShipAPI ship, String variantId, WeaponAPI weapon) {
        ShipAPI wing = CombatUtils.spawnShipOrWingDirectly(variantId, FleetMemberType.FIGHTER_WING, FleetSide.PLAYER, 100f, ship.getLocation(), ship.getFacing());


//        for (WeaponAPI SelectedWeapon : wing.getAllWeapons()) {
//            if (SelectedWeapon.getSlot().getId().equals("WS WeaponForWing")) {
        wing.getMutableStats().getVariant().addWeapon("WS WeaponForWing", weapon.getId());
//            }
//        }

        //this whole ass comment was from tomato but i found out ^^ so i havent used it (still thank you tomato :people_hugging:)
        //        CombatFleetManagerAPI manager = Global.getCombatEngine().getFleetManager(ship.getOwner());
//        boolean suppress = manager.isSuppressDeploymentMessages();
//        manager.setSuppressDeploymentMessages(true);
//
//
//        if (ship.getAllWeapons().isEmpty()) {
//            ship.getAllWeapons().add(Global.getCombatEngine().createFakeWeapon(ship, "hammer"));
//        }
//
//        WeaponSlotAPI launch = ship.getAllWeapons().get(0).getSlot();
//        float angle = MathUtils.clampAngle(launch.getAngle() + ship.getFacing());
//
//        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.FIGHTER_WING, spec);
//        ShipAPI drone = manager.spawnFleetMember(member, new Vector2f(launch.computePosition(ship)), angle, 0f);
//
//        FighterWingAPI wing = (FighterWingAPI) drone;
//
//        drone.setAnimatedLaunch();
//
//
//        Vector2f vel = new Vector2f(ship.getVelocity());
//        Vector2f n = new Vector2f(ship.getVelocity());
//        VectorUtils.rotate(n, angle);
//        drone.getVelocity().set(Vector2f.add(vel, n, vel));
//
//        drone.setOwner(ship.getOwner());
//
//        manager.setSuppressDeploymentMessages(suppress);

//        DroneSystem.droneSpawnCallback(drone, this, droneSystem);
    }


}
