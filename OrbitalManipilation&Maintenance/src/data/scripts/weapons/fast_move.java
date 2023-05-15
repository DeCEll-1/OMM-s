package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import java.awt.Color;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class fast_move implements EveryFrameWeaponEffectPlugin {
  private boolean runOnce = false;
  
  private boolean accel = false;
  
  private boolean turn = false;
  
  private ShipAPI SHIP;
  
  private ShipEngineControllerAPI.ShipEngineAPI thruster;
  
  private ShipEngineControllerAPI EMGINES;
  
  private float time = 0.0F;
  
  private float previousThrust = 0.0F;
  
  private final float FREQ = 0.05F;
  
  private final float SMOOTH_THRUSTING = 0.1F;
  
  private float TURN_RIGHT_ANGLE = 0.0F;
  
  private float THRUST_TO_TURN = 0.0F;
  
  private float NEUTRAL_ANGLE = 0.0F;
  
  private float FRAMES = 0.0F;
  
  private float OFFSET = 0.0F;
  
  private Vector2f size = new Vector2f(8.0F, 74.0F);
  
  public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
    if (!this.runOnce) {
      this.runOnce = true;
      this.SHIP = weapon.getShip();
      this.EMGINES = this.SHIP.getEngineController();
      if (weapon.getAnimation() != null)
        this.FRAMES = weapon.getAnimation().getNumFrames(); 
      for (ShipEngineControllerAPI.ShipEngineAPI e : this.SHIP.getEngineController().getShipEngines()) {
        if (MathUtils.isWithinRange(e.getLocation(), weapon.getLocation(), 2.0F))
          this.thruster = e; 
      } 
      switch (weapon.getSlot().getSlotSize()) {
        case LARGE:
          this.size = new Vector2f(20.0F, 232.0F);
          break;
        case MEDIUM:
          this.size = new Vector2f(12.0F, 140.0F);
          break;
        default:
          this.size = new Vector2f(8.0F, 74.0F);
          break;
      } 
      this.OFFSET = (float)(Math.random() * 3.1415927410125732D);
      this.NEUTRAL_ANGLE = weapon.getSlot().getAngle();
      this.TURN_RIGHT_ANGLE = MathUtils.clampAngle(VectorUtils.getAngle(this.SHIP.getLocation(), weapon.getLocation()));
      this.THRUST_TO_TURN = smooth(MathUtils.getDistance(this.SHIP.getLocation(), weapon.getLocation()) / this.SHIP.getCollisionRadius());
    } 
    if (engine.isPaused() || this.SHIP.getOriginalOwner() == -1)
      return; 
    if (!this.SHIP.isAlive() || (this.thruster != null && this.thruster.isDisabled())) {
      if (weapon.getAnimation() == null)
        return; 
      weapon.getAnimation().setFrame(0);
      this.previousThrust = 0.0F;
      return;
    } 
    this.time += amount;
    if (this.time >= 0.05F) {
      this.time = 0.0F;
      float accelerateAngle = this.NEUTRAL_ANGLE;
      float turnAngle = this.NEUTRAL_ANGLE;
      float thrust = 0.0F;
      if (this.EMGINES.isAccelerating()) {
        accelerateAngle = 180.0F;
        thrust = 1.5F;
        this.accel = true;
      } else if (this.EMGINES.isAcceleratingBackwards()) {
        accelerateAngle = 0.0F;
        thrust = 1.5F;
        this.accel = true;
      } else if (this.EMGINES.isDecelerating()) {
        accelerateAngle = this.NEUTRAL_ANGLE;
        thrust = 0.5F;
        this.accel = true;
      } else {
        this.accel = false;
      } 
      if (this.EMGINES.isStrafingLeft()) {
        if (thrust == 0.0F) {
          accelerateAngle = -90.0F;
        } else {
          accelerateAngle = MathUtils.getShortestRotation(accelerateAngle, -90.0F) / 2.0F + accelerateAngle;
        } 
        thrust = Math.max(1.0F, thrust);
        this.accel = true;
      } else if (this.EMGINES.isStrafingRight()) {
        if (thrust == 0.0F) {
          accelerateAngle = 90.0F;
        } else {
          accelerateAngle = MathUtils.getShortestRotation(accelerateAngle, 90.0F) / 2.0F + accelerateAngle;
        } 
        thrust = Math.max(1.0F, thrust);
        this.accel = true;
      } 
      if (this.EMGINES.isTurningRight()) {
        turnAngle = this.TURN_RIGHT_ANGLE;
        thrust = Math.max(1.0F, thrust);
        this.turn = true;
      } else if (this.EMGINES.isTurningLeft()) {
        turnAngle = MathUtils.clampAngle(180.0F + this.TURN_RIGHT_ANGLE);
        thrust = Math.max(1.0F, thrust);
        this.turn = true;
      } else {
        this.turn = false;
      } 
      if (thrust > 0.0F) {
        Vector2f offset = new Vector2f((weapon.getLocation()).x - (this.SHIP.getLocation()).x, (weapon.getLocation()).y - (this.SHIP.getLocation()).y);
        VectorUtils.rotate(offset, -this.SHIP.getFacing(), offset);
        if (!this.turn && this.FRAMES != 0.0F) {
          thrust(weapon, accelerateAngle, thrust * this.SHIP.getMutableStats().getAcceleration().computeMultMod(), 0.1F);
        } else if (!this.accel && this.FRAMES != 0.0F) {
          thrust(weapon, turnAngle, thrust * this.SHIP.getMutableStats().getTurnAcceleration().computeMultMod(), 0.1F);
        } else {
          float clampedThrustToTurn = this.THRUST_TO_TURN * Math.min(1.0F, Math.abs(this.SHIP.getAngularVelocity()) / 10.0F);
          clampedThrustToTurn = smooth(clampedThrustToTurn);
          float combinedAngle = this.NEUTRAL_ANGLE;
          combinedAngle = MathUtils.clampAngle(combinedAngle + MathUtils.getShortestRotation(this.NEUTRAL_ANGLE, accelerateAngle) + 5F * OFFSET);
          combinedAngle = MathUtils.clampAngle(combinedAngle + clampedThrustToTurn * MathUtils.getShortestRotation(accelerateAngle, turnAngle));
          float combinedThrust = thrust;
          combinedThrust *= (this.SHIP.getMutableStats().getTurnAcceleration().computeMultMod() + this.SHIP.getMutableStats().getAcceleration().computeMultMod()) / 2.0F;
          float offAxis = Math.abs(MathUtils.getShortestRotation(turnAngle, accelerateAngle));
          offAxis = Math.max(0.0F, offAxis - 90.0F);
          offAxis /= 45.0F;
          combinedThrust *= 1.0F - Math.max(0.0F, Math.min(1.0F, offAxis));
          if (this.FRAMES == 0.0F) {
            rotate(weapon, combinedAngle, combinedThrust, 0.05F);
          } else {
            thrust(weapon, combinedAngle, combinedThrust, 0.05F);
          } 
        } 
      } else if (this.FRAMES == 0.0F) {
        rotate(weapon, this.NEUTRAL_ANGLE, 0.0F, 0.1F);
      } else {
        thrust(weapon, this.NEUTRAL_ANGLE, 0.0F, 0.1F);
      } 
    } 
  }
  
  private void rotate(WeaponAPI weapon, float angle, float thrust, float smooth) {
    float aim = angle + this.SHIP.getFacing();
    aim = MathUtils.getShortestRotation(weapon.getCurrAngle(), aim);
    aim = (float)(aim + 5.0D * FastTrig.cos((this.SHIP.getFullTimeDeployed() * 5.0F * thrust + this.OFFSET)));
    aim *= smooth;
    weapon.setCurrAngle(MathUtils.clampAngle(weapon.getCurrAngle() + aim));
  }
  
  private void thrust(WeaponAPI weapon, float angle, float thrust, float smooth) {
    int frame = (int)(Math.random() * (this.FRAMES - 1.0F)) + 1;
    if (frame == weapon.getAnimation().getNumFrames())
      frame = 1; 
    weapon.getAnimation().setFrame(frame);
    SpriteAPI sprite = weapon.getSprite();
    float aim = angle + this.SHIP.getFacing();
    float length = thrust;
    aim = MathUtils.getShortestRotation(weapon.getCurrAngle(), aim);
    length *= Math.max(0.0F, 1.0F - Math.abs(aim) / 90.0F);
    length -= this.previousThrust;
    length *= smooth;
    length += this.previousThrust;
    this.previousThrust = length;
    aim = (float)(aim + 2.0D * FastTrig.cos((this.SHIP.getFullTimeDeployed() * 5.0F * thrust + this.OFFSET)));
    aim *= smooth;
    weapon.setCurrAngle(MathUtils.clampAngle(weapon.getCurrAngle() + aim * 4F * OFFSET));
    float width = length * this.size.x / 2.0F + this.size.x / 2.0F;
    if (weapon.getShip().getVariant().getHullMods().contains("safetyoverrides"))
      length *= 1.25F; 
    float height = length * this.size.y + (float)Math.random() * 3.0F + 3.0F;
    sprite.setSize(width, height);
    sprite.setCenter(width / 2.0F, height / 2.0F);
    length = Math.max(0.0F, Math.min(1.0F, length));
    Color thrustColor = new Color(1.0F, Math.min(1.0F, 0.5F + length / 2.0F), Math.min(1.0F, 0.5F + length / 4.0F));
    if (weapon.getShip().getVariant().getHullMods().contains("safetyoverrides"))
      thrustColor = new Color(1.0F, Math.min(1.0F, 0.5F + length / 20.0F), Math.min(1.0F, 0.5F + length / 1.5F)); 
    sprite.setColor(thrustColor);
  }
  
  public float smooth(float x) {
    return 0.5F - (float)(Math.cos((x * 3.1415927F)) / 2.0D);
  }
}