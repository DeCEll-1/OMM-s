package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.IntervalUtil;

public class omm_no_missile_reload extends BaseHullMod {
  public static String NO_MR_DATA_KEY = "omm_no_reload_data_key";
  
  public static class PeriodicMissileReloadData {
    IntervalUtil interval = new IntervalUtil(1.0F, 250.0F);
  }
  
  public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {}
  
  public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
    return null;
  }
  
  public void advanceInCombat(ShipAPI ship, float amount) {
    super.advanceInCombat(ship, amount);
    if (!ship.isAlive())
      return; 
    CombatEngineAPI engine = Global.getCombatEngine();
    String key = String.valueOf(NO_MR_DATA_KEY) + "_" + ship.getId();
    PeriodicMissileReloadData data = (PeriodicMissileReloadData)engine.getCustomData().get(key);
    if (data == null) {
      data = new PeriodicMissileReloadData();
      engine.getCustomData().put(key, data);
    } 
    data.interval.advance(amount);
    if (data.interval.intervalElapsed())
      for (WeaponAPI w : ship.getAllWeapons()) {
        if (w.getType() != WeaponAPI.WeaponType.MISSILE)
          continue; 
        if (w.usesAmmo() && w.getAmmo() < w.getMaxAmmo())
          w.setAmmo(w.getMaxAmmo()); 
      }  
  }
}
