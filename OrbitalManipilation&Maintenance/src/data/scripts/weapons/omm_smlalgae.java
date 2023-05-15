package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.input.InputEventAPI;
import org.magiclib.util.MagicRender;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class omm_smlalgae implements EveryFrameWeaponEffectPlugin {

    private boolean isPodDeployed = false;
    private float tracker = 0;
    private final float trackermax = 1f;
    private final float launchPlatformAnyways = 30;
    private float trackerBis = 0;

    //private CombatEntityAPI anchor;
    private SpriteAPI sprite;
    private Vector2f size;

    // The weapon who run is the head.
    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

        if (engine.isPaused()) {
            return;
        }
        if (isPodDeployed) {
            return;
        }
        if (sprite == null) {
            displaySprite();
        }
        MagicRender.singleframe(sprite, weapon.getShip().getLocation(), size, weapon.getShip().getFacing() - 90, Color.WHITE, false, CombatEngineLayers.FIGHTERS_LAYER);

        tracker += amount;

        if (tracker > trackermax) {
            tracker -= trackermax;

            ShipAPI ship = weapon.getShip();

            if (ship != null && ship.isAlive()) {
                if (ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.REACHED_WAYPOINT)) {
                    // Global.getCombatEngine().getCombatUI().addMessage(0, "REACHED_WAYPOINT");
                    spawn(ship, null);
                    return;
                }

                    trackerBis += trackermax;
                    if (trackerBis >= launchPlatformAnyways) {
                        spawn(ship, null);
                    } else {
                    for (BattleObjectiveAPI objective : Global.getCombatEngine().getObjectives()) {
                        // Global.getCombatEngine().getCombatUI().addMessage(0, objective.getImportance()+"");
                        if (MathUtils.isWithinRange(ship, objective, 300)) {
                            //weapon.usesAmmo();
                            spawn(ship, objective);

                            return;
                        }
                    }
                }
            }
        }
    }

    private void displaySprite() {
        sprite = Global.getSettings().getSprite("misc", "omm_algaepod_graphic");
        size = new Vector2f(sprite.getWidth(), sprite.getHeight());
    }

    private void spawn(ShipAPI ship, BattleObjectiveAPI objective) {
        sprite = null;
        size = null;
        isPodDeployed = true;
        DefensePlatformFadeInPlugin defensePlatform = createShipFadeInPlugin("omm_algaepod_Standard", objective, ship, 0.5f, ship.getFacing());
        Global.getCombatEngine().addPlugin(defensePlatform);

    }

    protected DefensePlatformFadeInPlugin createShipFadeInPlugin(final String variantId, BattleObjectiveAPI objective, final ShipAPI source,
            final float fadeInTime, final float angle) {

        return new DefensePlatformFadeInPlugin(variantId, objective, source, fadeInTime, angle);
    }

    public static class DefensePlatformFadeInPlugin extends BaseEveryFrameCombatPlugin {

        float elapsed = 0f;
        ShipAPI shipSpawned = null;
        CollisionClass collisionClass;

        String variantId;
        ShipAPI source;
        float delay;
        float fadeInTime;
        float angle;
        BattleObjectiveAPI objective;

        public DefensePlatformFadeInPlugin(String variantId, BattleObjectiveAPI objective, ShipAPI source, float fadeInTime, float angle) {
            this.variantId = variantId;
            this.source = source;
            this.fadeInTime = fadeInTime;
            this.angle = angle;
            delay = 0;
            this.objective = objective;
        }

        @Override
        public void advance(float amount, List<InputEventAPI> events) {
            if (Global.getCombatEngine().isPaused()) {
                return;
            }

            elapsed += amount;

            CombatEngineAPI engine = Global.getCombatEngine();

            if (shipSpawned == null) {
                float facing = source.getFacing();
                Vector2f loc = new Vector2f(0, 0);
                Vector2f.add(loc, source.getLocation(), loc);
                CombatFleetManagerAPI fleetManager = engine.getFleetManager(source.getOriginalOwner());
                boolean wasSuppressed = fleetManager.isSuppressDeploymentMessages();
                fleetManager.setSuppressDeploymentMessages(true);

                shipSpawned = engine.getFleetManager(source.getOriginalOwner()).spawnShipOrWing(variantId, loc, facing, 0f, source.getOriginalCaptain());

                shipSpawned.cloneVariant();
                shipSpawned.getVariant().addTag(Tags.SHIP_LIMITED_TOOLTIP);

                fleetManager.setSuppressDeploymentMessages(wasSuppressed);
                collisionClass = shipSpawned.getCollisionClass();

                DeployedFleetMemberAPI sourceMember = fleetManager.getDeployedFleetMemberFromAllEverDeployed(source);
                DeployedFleetMemberAPI deployed = fleetManager.getDeployedFleetMemberFromAllEverDeployed(shipSpawned);
                if (sourceMember != null && deployed != null) {
                    Map<DeployedFleetMemberAPI, DeployedFleetMemberAPI> map = fleetManager.getShardToOriginalShipMap();
                    while (map.containsKey(sourceMember)) {
                        sourceMember = map.get(sourceMember);
                    }
                    if (sourceMember != null) {
                        map.put(deployed, sourceMember);
                    }
                }
                Vector2f vec;
                if (objective != null) {
                    vec = new Vector2f(objective.getLocation().x - source.getLocation().x,
                            objective.getLocation().y - source.getLocation().y);
                    vec.normalise(vec);
                    vec.scale(50);

                } else {
                    vec = new Vector2f(source.getVelocity().x, source.getVelocity().y);

                    vec.scale(2);
                }
                this.objective = null;
                //shipSpawned.
                shipSpawned.getVelocity().set(vec.x, vec.y);
            }

            shipSpawned.getVelocity().scale(0.99f);

            shipSpawned.blockCommandForOneFrame(ShipCommand.ACCELERATE);
            shipSpawned.blockCommandForOneFrame(ShipCommand.TURN_LEFT);
            shipSpawned.blockCommandForOneFrame(ShipCommand.TURN_RIGHT);
            shipSpawned.blockCommandForOneFrame(ShipCommand.STRAFE_LEFT);
            shipSpawned.blockCommandForOneFrame(ShipCommand.STRAFE_RIGHT);

            shipSpawned.blockCommandForOneFrame(ShipCommand.USE_SYSTEM);
            shipSpawned.blockCommandForOneFrame(ShipCommand.TOGGLE_SHIELD_OR_PHASE_CLOAK);
            shipSpawned.blockCommandForOneFrame(ShipCommand.FIRE);
            shipSpawned.blockCommandForOneFrame(ShipCommand.PULL_BACK_FIGHTERS);
            shipSpawned.blockCommandForOneFrame(ShipCommand.VENT_FLUX);
            shipSpawned.setHoldFireOneFrame(true);
            shipSpawned.setHoldFire(true);

            shipSpawned.setCollisionClass(CollisionClass.NONE);
            shipSpawned.getMutableStats().getHullDamageTakenMult().modifyMult(this.getClass().getName(), 0f);

            if (!MathUtils.isWithinRange(shipSpawned, source, 0)) {
                shipSpawned.setPhased(false);
                shipSpawned.getVelocity().set(0, 0);
                // shipSpawned.setAlphaMult(1f);
                shipSpawned.setHoldFire(false);
                shipSpawned.setCollisionClass(collisionClass);
                shipSpawned.getMutableStats().getHullDamageTakenMult().unmodifyMult(this.getClass().getName());
                engine.removePlugin(this);
            }
        }
    }

}
