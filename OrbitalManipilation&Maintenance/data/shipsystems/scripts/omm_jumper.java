package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import static com.fs.starfarer.api.combat.ShipSystemAPI.SystemState.ACTIVE;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dark.shaders.distortion.DistortionShader;
import org.dark.shaders.distortion.RippleDistortion;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lazywizard.lazylib.combat.entities.SimpleEntity;
import org.lwjgl.util.vector.Vector2f;

public class omm_jumper extends BaseShipSystemScript {

    private static Map mag = new HashMap();

    static {
        mag.put(ShipAPI.HullSize.FIGHTER, 0.15f);
        mag.put(ShipAPI.HullSize.FRIGATE, 0.15f);
        mag.put(ShipAPI.HullSize.DESTROYER, 0.15f);
        mag.put(ShipAPI.HullSize.CRUISER, 0.33f);
        mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.33f);
    }
    protected Object STATUSKEY1 = new Object();

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {

        if (state == ShipSystemStatsScript.State.OUT) {
            stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
        } else {
            stats.getEntity().setCollisionClass(CollisionClass.FIGHTER);
            stats.getMaxSpeed().modifyFlat(id, 3500f * effectLevel);
            stats.getAcceleration().modifyFlat(id, 8000f * effectLevel);
        }
        effectLevel = 1f;

        float mult = (Float) mag.get(ShipAPI.HullSize.CRUISER);
        if (stats.getVariant() != null) {
            mult = (Float) mag.get(stats.getVariant().getHullSize());
        }
        stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
        stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);
        stats.getEmpDamageTakenMult().modifyMult(id, 1f - (1f - mult) * effectLevel);

        ShipAPI ship = null;
        boolean player = false;
        if (stats.getEntity() instanceof ShipAPI) {

            player = ship == Global.getCombatEngine().getPlayerShip();
        }
        if (ship == null) {
            return;
        }
        if (player) {
            ShipSystemAPI system = ship.getSystem();
            if (system != null) {
                float percent = (1f - mult) * effectLevel * 100;
                Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY1,
                        system.getSpecAPI().getIconSpriteName(), system.getDisplayName(),
                        (int) Math.round(percent) + "% less damage taken", false);
            }
        }
        if (ship.getSystem().getCooldownRemaining() < 0.2f) {
            return;
        }
        //   if (AIUtils.canUseSystemThisFrame(ship)) {

        List<ShipAPI> targetsToRepulse = AIUtils.getNearbyEnemies(ship, 10);
        boolean containATrueTarget = false;
        for (ShipAPI target : targetsToRepulse) {
            if (!target.isFighter()) {

                containATrueTarget = true;
                break;
            }
        }
        if (containATrueTarget) {

            Vector2f dir = new Vector2f();
            Vector2f positionRepulse = MathUtils.getPointOnCircumference(ship.getLocation(), ship.getCollisionRadius(), ship.getFacing());

            SimpleEntity entity = new SimpleEntity(positionRepulse);
            for (ShipAPI target2 : targetsToRepulse) {
                Vector2f.sub(target2.getLocation(), positionRepulse, dir);
                CombatUtils.applyForce(target2, dir, 1 * (1 + ship.getSystem().getChargeActiveDur() - ship.getSystem().getCooldownRemaining()));
            }

            for (int i = 0; i < 5; i++) {

            }

            CustomRippleDistortion(positionRepulse, new Vector2f(0, 0), 200, 3f, false, 0f, 360f, 0.5f, 0f, 0.5f, 0.5f, 1f, 0f);

        }
    }

    public void unapply(MutableShipStatsAPI stats, String id) {

        stats.getEntity().getVelocity().set(0, 0);
        stats.getEntity().setCollisionClass(CollisionClass.SHIP);
        
        stats.getMaxSpeed().unmodify(id);
        stats.getMaxTurnRate().unmodify(id);
        stats.getTurnAcceleration().unmodify(id);
        stats.getAcceleration().unmodify(id);
        stats.getDeceleration().unmodify(id);
        stats.getHullDamageTakenMult().unmodify(id);
        stats.getArmorDamageTakenMult().unmodify(id);
        stats.getEmpDamageTakenMult().unmodify(id);
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        if (index == 0) {
            return new ShipSystemStatsScript.StatusData("increased engine power", false);
        }
        return null;
    }

    public static void CustomRippleDistortion(Vector2f loc, Vector2f vel, float size, float intensity, boolean flip, float angle, float arc, float edgeSmooth, float fadeIn, float last, float fadeOut, float growthTime, float shrinkTime) {

        RippleDistortion ripple = new RippleDistortion(loc, vel);

        ripple.setIntensity(intensity);
        ripple.setSize(size);
        ripple.setArc(angle - arc / 2, angle + arc / 2);
        if (edgeSmooth != 0) {
            ripple.setArcAttenuationWidth(edgeSmooth);
        }
        ripple.flip(flip);
        if (fadeIn != 0) {
            ripple.fadeInIntensity(fadeIn);
        }
        ripple.setLifetime(last);
        if (fadeOut != 0) {
            ripple.setAutoFadeIntensityTime(fadeOut);
        }
        if (growthTime != 0) {
            ripple.fadeInSize(growthTime);
        }
        if (shrinkTime != 0) {
            ripple.setAutoFadeSizeTime(shrinkTime);
        }
        ripple.setFrameRate(60);
        DistortionShader.addDistortion(ripple);

    }
}
