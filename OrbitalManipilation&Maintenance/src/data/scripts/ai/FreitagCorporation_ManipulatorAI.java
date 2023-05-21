package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import java.awt.geom.Line2D;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class FreitagCorporation_ManipulatorAI implements ShipSystemAIScript {

    private CombatEngineAPI engine;
    private ShipAPI ship;
    private ShipSystemAPI system;
    private float RANGEOFBURN=1500;

    private float tracker = 0;
    private float trackermax = 0.2f;

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (engine == null) {
            return;
        }

        if (engine.isPaused()) {
            return;
        }
       // if(centerWeapon==null)return;
        
        tracker += amount;

        if (tracker > trackermax) {
            tracker -= trackermax;
            if (ship.getFluxTracker().isOverloadedOrVenting()) {
                return;
            }
            if(ship.getEngineController().isDisabled())return;
            
            if(target==null)return;
            
            boolean targetToManipulate=!this.isWithinArc(target,300) && this.isWithinArc(target,RANGEOFBURN);      
            
          /*  //disable shipsystem because the target is out of the line !
            if(system.isOn() && !targetToManipulate){
                if(system.getCooldownRemaining()>1){
                    ship.getSystem().setCooldownRemaining(1);
                }
                //ship.getSystem().deactivate();
            }*/
            //enable shipsystem because the target is on the line !
            if (!system.isOn() && targetToManipulate) {
                if( AIUtils.canUseSystemThisFrame(ship))ship.useSystem();
            }
        }
    }

    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.system = system;
        this.engine = engine;
        this.tracker = 0;
        this.trackermax = 0.2f;
    }
    
    /**
     * From LazyLib WeaponUtils
     * 
     * Checks if a {@link CombatEntityAPI} is within the arc.
     *
     * @param entity The {@link CombatEntityAPI} to check if ship is
     *               aimed at.     *
     * @return {@code true} if in arc and in range, {@code false} otherwise.
     *
     * @since 1.0
     */
    public boolean isWithinArc(CombatEntityAPI entity, float range)
    {

        // Check if weapon is aimed at any part of the target
        float arc = 5;
        Vector2f target = entity.getLocation();
        Vector2f wep = ship.getLocation();
        Vector2f endArcLeft = MathUtils.getPointOnCircumference(wep, range,
                ship.getFacing() - arc);
        Vector2f endArcRight = MathUtils.getPointOnCircumference(wep, range,
                ship.getFacing() + arc);
        float radSquared = entity.getCollisionRadius() * entity.getCollisionRadius();

        // Check if target is partially in weapon arc
        return (Line2D.ptSegDistSq(
                wep.x,
                wep.y,
                endArcLeft.x,
                endArcLeft.y,
                target.x,
                target.y) <= radSquared)
                || (Line2D.ptSegDistSq(
                wep.x,
                wep.y,
                endArcRight.x,
                endArcRight.y,
                target.x,
                target.y) <= radSquared);
    }

}
