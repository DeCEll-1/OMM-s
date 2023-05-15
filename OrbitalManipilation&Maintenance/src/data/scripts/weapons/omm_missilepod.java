package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponGroupAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.magiclib.util.MagicRender;
import java.awt.Color;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public class omm_missilepod implements EveryFrameWeaponEffectPlugin {

    private SpriteAPI sprite;
    private Vector2f size;
    private boolean isWeaponSwappedmissile = false;
    private ShipAPI SHIP;
    private ShipAPI FIGHTER;
    public IntervalUtil timer = new IntervalUtil(3F, 20F);

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        this.SHIP = weapon.getShip();
        if (engine.isPaused()) {
            return;
        }
        if (sprite == null) {
            displaySprite();
        }

        List<WeaponGroupAPI> weapons = this.SHIP.getWeaponGroupsCopy();
        List<WeaponAPI> list = this.SHIP.getAllWeapons();
        List<FighterWingAPI> dronewings = this.SHIP.getAllWings();
        if (this.SHIP.getOriginalOwner() == 0 || this.SHIP.getOriginalOwner() == 1) { //check for refit screen

            for (FighterWingAPI fighterWingAPI : dronewings) {
                if (!fighterWingAPI.getWingId().equals("omm_missilepod_wing")) {   //name is the built-in drone wing
                    continue;
                }
                Vector2f mousepos = this.SHIP.getMouseTarget();
                FIGHTER = fighterWingAPI.getLeader();
                List<WeaponAPI> droneweps = FIGHTER.getAllWeapons();
                Vector2f dronepos = FIGHTER.getLocation();
                float angle = VectorUtils.getAngle(dronepos, mousepos);

                for (WeaponAPI dronewep : droneweps) {
                    if (dronewep.getSlot().getId().equals("smlmissileslot")) {

                        WeaponGroupAPI Group = FIGHTER.getWeaponGroupFor(weapon);
                        FIGHTER.getVariant().assignUnassignedWeapons();
                        float diff = MathUtils.getShortestRotation(dronewep.getCurrAngle(), angle);
                        float maxVel = amount * dronewep.getTurnRate();
                        diff = MathUtils.clamp(diff, -maxVel, maxVel);
                        dronewep.setCurrAngle(diff + dronewep.getCurrAngle());     //aims the drone weapon

                        float diffdrone = MathUtils.getShortestRotation(FIGHTER.getFacing(), angle);
                        float maxVeldrone = amount * FIGHTER.getMaxTurnRate();
                        diffdrone = MathUtils.clamp(diffdrone, -maxVeldrone, maxVeldrone);
                        FIGHTER.setFacing(diffdrone + FIGHTER.getFacing());        //sets facing of the drone

                        ShipAPI player = Global.getCombatEngine().getPlayerShip();
                        if (player == this.SHIP && !FIGHTER.isLanding() && !FIGHTER.isLiftingOff()) {
                            MagicRender.singleframe(sprite, dronewep.getLocation(), size, dronewep.getCurrAngle(), Color.WHITE, false, CombatEngineLayers.FIGHTERS_LAYER);
                            FIGHTER.resetDefaultAI();
                        }
                        if (Mouse.isButtonDown(0)) {
                            FIGHTER.giveCommand(ShipCommand.FIRE, mousepos, 0);           //clicky left drone shooty
                        }
                        if (player != this.SHIP) {
                            sprite = null;
                        }
                        for (WeaponAPI weaponAPI : list) {
//                if (weaponAPI.getId().equals("omm_weaponpoddeco")) {                  //decorative looks like drone
//                    weaponAPI.getSprite().setColor(new Color(255, 255, 255, 0));
//                }
                            if (weaponAPI.getSlot().getId().equals("smlmissileslot")) {                //slot has same name as on drone !important!
//                    weaponAPI.getSprite().setColor(new Color(255, 255, 255, 0));
                            }
                            for (int i = 0; i < weapons.size(); i++) {
                                if (weaponAPI.getSlot().getId().equals("smlmissileslot")) {
                                    weaponAPI.disable(true);
                                    this.SHIP.removeWeaponFromGroups(weaponAPI);                   //removes the weapons swap "interface" from weapon groups
                                }
                            }
//                if (weaponAPI.getBarrelSpriteAPI() != null) {
//                    weaponAPI.getBarrelSpriteAPI().setColor(new Color(255, 255, 255, 0));
//                }
                        }
                        continue;
                    }
                }

            }
        }

        this.timer.randomize();                                                        //randomize interval to stagger drone refit

        this.timer.advance(amount);
        if (!this.timer.intervalElapsed()) {
            return;
        }
        if (isWeaponSwappedmissile) {
            return;
        }
        if (!isWeaponSwappedmissile) {

            if (this.SHIP != null) {
                List<FighterWingAPI> list1 = this.SHIP.getAllWings();
                for (FighterWingAPI fighterWingAPI : list1) {
                    if (!fighterWingAPI.getWingId().equals("omm_missilepod_wing")) {
                        continue;
                    }

                    {
                        this.FIGHTER = fighterWingAPI.getLeader();
                        MutableShipStatsAPI mutableShipStatsAPI = this.FIGHTER.getMutableStats();
                        ShipVariantAPI shipVariantAPI = mutableShipStatsAPI.getVariant().clone();
                        this.FIGHTER.getFleetMember().setVariant(shipVariantAPI, false, true);
                        mutableShipStatsAPI.getVariant().clearSlot("smlmissileslot");
                        if (this.SHIP.getVariant().getWeaponSpec("smlmissileslot") != null) {
                            mutableShipStatsAPI.getVariant().addWeapon("smlmissileslot", this.SHIP.getVariant().getWeaponId("smlmissileslot"));
                            mutableShipStatsAPI.getVariant().getWeaponSpec("smlmissileslot").addTag("FIRE_WHEN_INEFFICIENT");
                            fighterWingAPI.orderReturn(this.FIGHTER);

                            this.isWeaponSwappedmissile = true;

                        }

                    }
                }
            }
        }
    }

    private void displaySprite() {
        sprite = Global.getSettings().getSprite("misc", "omm_aiming_laser");
        size = new Vector2f(sprite.getWidth(), sprite.getHeight());
    }

}
