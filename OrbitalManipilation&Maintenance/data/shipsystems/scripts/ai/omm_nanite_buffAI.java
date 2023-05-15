package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class omm_nanite_buffAI implements ShipSystemAIScript {

    private ShipSystemAPI system;

    private ShipAPI ship;

    private final IntervalUtil tracker = new IntervalUtil(1.0F, 1.5F);

    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.system = system;
    }

    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        this.tracker.advance(amount);
        if (this.tracker.intervalElapsed()) {
            float activ_range = 1200.0F;
            float activ_chance = 0.0F;
            int ships_hostile = 0;
            for (ShipAPI shp : Global.getCombatEngine().getShips()) {
                if (!shp.isAlive()) {
                    continue;
                }
                if (MathUtils.getDistance((CombatEntityAPI) shp, (CombatEntityAPI) this.ship) <= activ_range) {
                    if (shp.getOwner() == this.ship.getOwner()) {
                        ships_hostile++;
                    }
                    if (shp.getOwner() == this.ship.getOwner() && MathUtils.getDistance((CombatEntityAPI) shp, (CombatEntityAPI) this.ship) <= activ_range) {
                        activ_chance++;
                    }
                }
                float fluxLevel = this.ship.getFluxTracker().getFluxLevel();
                if (activ_chance > 2.0F && !this.system.isActive() && ships_hostile <= 3 && fluxLevel >= 85.0F && (float) Math.random() > 0.25F) {
                    this.ship.useSystem();
                    continue;
                }
                if (activ_chance > 2.0F && !this.system.isActive() && ships_hostile == 0 && fluxLevel >= 60.0F && (float) Math.random() > 0.7F) {
                    this.ship.useSystem();
                    continue;
                }
                if ((activ_chance <= 1.0F || ships_hostile >= 5 || fluxLevel <= 90.0F) && this.system.isActive()) {
                    this.ship.useSystem();
                    continue;
                }
                return;
            }
        }
    }
}
