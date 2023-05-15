package data.missions.FreitagCorporation_classic;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

    @Override
    public void defineMission(MissionDefinitionAPI api) {

        // Set up the fleets
        api.initFleet(FleetSide.PLAYER, "HSS", FleetGoal.ATTACK, false);
        api.initFleet(FleetSide.ENEMY, "ISS", FleetGoal.ATTACK, true);

        // Set a blurb for each fleet
        api.setFleetTagline(FleetSide.PLAYER, Factions.TRITACHYON);
        api.setFleetTagline(FleetSide.ENEMY, Factions.INDEPENDENT);

        // These show up as items in the bulleted list under 
        // "Tactical Objectives" on the mission detail screen
        api.addBriefingItem("Win.");

        // Set up the player's fleet
        api.addToFleet(FleetSide.PLAYER, "omm_euphausia_Standard", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_crevette_Standard", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_ecrevisse_Standard", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_euphausia_Standard", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_sesarma_tanker", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_sesarma_personnel", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_sesarma_freighter", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_nathantia_Standard", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_sandhopper_Standard", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_sandhopper_Standard", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "omm_limnopilos_Standard", FleetMemberType.SHIP, true);

        api.addToFleet(FleetSide.PLAYER, "lasher_CS", FleetMemberType.SHIP, "Taskmaster", false);
        api.addToFleet(FleetSide.ENEMY, "condor_Support", FleetMemberType.SHIP, false);

        api.addToFleet(FleetSide.ENEMY, "eagle_xiv_Elite", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "falcon_xiv_Escort", FleetMemberType.SHIP, false);

        //api.addToFleet(FleetSide.ENEMY, "enforcer_Assault", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "heron_Strike", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Taskmaster", false);
        // Set up the map.

        // Set up the map.
        float width = 5000f;
        float height = 10000f;
        api.initMap((float) -width / 2f, (float) width / 2f, (float) -height / 2f, (float) height / 2f);

        api.addPlanet(0, 0, 400f, "barren", 200f, true);
        api.addRingAsteroids(0, 0, 30, 32, 32, 48, 200);
        // add objectives
        float minX = -width / 2;
        float minY = -height / 2;
        api.addObjective(minX + width * 0.25f + 2000f, minY + height * 0.5f,
                "sensor_array");
        api.addObjective(minX + width * 0.75f - 2000f, minY + height * 0.5f,
                "comm_relay");
        api.addObjective(minX + width * 0.33f + 2000f, minY + height * 0.4f,
                "nav_buoy");
        api.addObjective(minX + width * 0.66f - 2000f, minY + height * 0.6f,
                "nav_buoy");
    }

}
