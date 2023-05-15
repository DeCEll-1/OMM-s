package data.scripts.world.subsidiaries;

import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import data.scripts.world.omm_AddMarketplace;
import java.util.ArrayList;
import java.util.Arrays;

public class FreitagHQ {

    public void generate(SectorAPI sector) {
        StarSystemAPI valhalla = sector.getStarSystem("Valhalla");
        CustomCampaignEntityAPI customCampaignEntityAPI1 = valhalla.addCustomEntity("valhalla_salvage_gate", "Destroyed Gate", "salvage_gate", null);
        customCampaignEntityAPI1.setCircularOrbitPointingDown(valhalla.getEntityById("Valhalla"), 220.0F, 3350F, 100.0F);
        customCampaignEntityAPI1.setCustomDescriptionId("salvage_gate");

        CustomCampaignEntityAPI customCampaignEntityAPI2 = valhalla.addCustomEntity("freitag_hq", "Freitag Corporation HQ", "freitag_hq", "freitag_corporation");
        customCampaignEntityAPI2.setCircularOrbit(valhalla.getEntityById("valhalla_salvage_gate"), 90, 35, 30);
        customCampaignEntityAPI2.setCustomDescriptionId("freitag_hq");
        MarketAPI HQmarket = omm_AddMarketplace.addMarketplace("freitag_corporation", (SectorEntityToken) customCampaignEntityAPI2, null, "Freitag Corporation HQ", 4, new ArrayList<>(Arrays.asList( //List of conditions for this method to iterate through and add to the market
                Conditions.INDUSTRIAL_POLITY,
                Conditions.POPULATION_3,
                Conditions.RARE_ORE_ABUNDANT,
                Conditions.NO_ATMOSPHERE,
                Conditions.VERY_COLD,
                Conditions.LOW_GRAVITY,
                Conditions.REGIONAL_CAPITAL,
                Conditions.POLLUTION,
                Conditions.ORE_RICH
        )),
                new ArrayList<>(Arrays.asList( //list of submarkets for this method to iterate through and add to the market. if a military base industry was added to this market, it would be consistent to add a military submarket too
                        Submarkets.SUBMARKET_OPEN,
                        "generic_military",//add a default open market
                        Submarkets.SUBMARKET_STORAGE, //add a player storage market
                        Submarkets.SUBMARKET_BLACK //add a black market
                )),
                new ArrayList<>(Arrays.asList( //list of industries for this method to iterate through and add to the market
                        Industries.POPULATION, //population industry is required for weirdness to not happen
                        Industries.SPACEPORT, //same with spaceport
                        Industries.HIGHCOMMAND,
                        Industries.WAYSTATION,
                        Industries.AQUACULTURE,
                        Industries.HEAVYINDUSTRY,
                        Industries.ORBITALWORKS,
                        Industries.GROUNDDEFENSES,
                        Industries.STARFORTRESS_HIGH
                )),
                true, //if true, the planet will have visual junk orbiting and will play an ambient chatter audio track when the player is nearby
                false //used by the method to make a market hidden like a pirate base, not recommended for generating markets in a core world
        );
        HQmarket.addIndustry(Industries.GROUNDDEFENSES, new ArrayList(Arrays.asList((Object[]) new String[]{Items.DRONE_REPLICATOR})));
    }
}
