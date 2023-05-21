package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.IntelDataAPI;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.util.IntervalUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class omm_algaefarm extends BaseHullMod {
    //from SKR_farm from seeker, ofc i didnt copy pasted the whole thing i just did this while looking at it :alpha:
    private final IntervalUtil timer = new IntervalUtil(1.0f, 1.0f);

    private static int LastDay = 0;//stuff might not work if this is changed
    private boolean WorkOutsideOfHyperSpace = true;//comment line 33 to not generate fuel outside of hyperspace
    private static float DurationOfFloatingText = 0.5f;//sets the durationn of the floating text // i think its in seconds
    private static Color ColorOfFloatingText = new Color(150, 150, 0, 255);//color of floating text

    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        if (!member.isMothballed()) {
            this.timer.advance(amount);

            int CurrDay = Global.getSector().getClock().getDay();

            if (this.timer.intervalElapsed() && member.getFleetData() != null && member.getFleetCommander().isPlayer() && CurrDay > LastDay) {
                if (!member.getFleetData().getFleet().isInHyperspace()) {
                    WorkOutsideOfHyperSpace = false;
                }


                LastDay = CurrDay;


                float size = (float) GetSizeAsNumber(member.getHullSpec().getHullSize());

                int CraftedFuel = 0;

                //region
                if (WorkOutsideOfHyperSpace) {
                    if (size == 1) {//frigate
                        CraftedFuel = 1;//change for frigate
                        Global.getSector().getPlayerFleet().getCargo().addCommodity("fuel", CraftedFuel);
                    } else if (size == 2) {//destroyer
                        CraftedFuel = 2;//change for destroyer
                        Global.getSector().getPlayerFleet().getCargo().addCommodity("fuel", CraftedFuel);
                    } else if (size == 3) {//cruiser
                        CraftedFuel = 3;//change for cruiser
                        Global.getSector().getPlayerFleet().getCargo().addCommodity("fuel", CraftedFuel);
                    } else if (size == 4) {//capital
                        CraftedFuel = 4;//change for capital
                        Global.getSector().getPlayerFleet().getCargo().addCommodity("fuel", CraftedFuel);
                    }
                }
                //endregion

                int CraftedSupplies = 0;

                //region
                if (size == 1) {//frigate
                    CraftedSupplies = 1;//change for frigate
                    Global.getSector().getPlayerFleet().getCargo().addCommodity("supplies", CraftedSupplies);
                } else if (size == 2) {//destroyer
                    CraftedSupplies = 2;//change for destroyer
                    Global.getSector().getPlayerFleet().getCargo().addCommodity("supplies", CraftedSupplies);
                } else if (size == 3) {//cruiser
                    CraftedSupplies = 3;//change for cruiser
                    Global.getSector().getPlayerFleet().getCargo().addCommodity("supplies", CraftedSupplies);
                } else if (size == 4) {//capital
                    CraftedSupplies = 4;//change for capital
                    Global.getSector().getPlayerFleet().getCargo().addCommodity("supplies", CraftedSupplies);
                }
                //endregion

//                Global.getSector().getCampaignUI().addMessage(CraftedSupplies + " Supplies Crafted\n" + CraftedFuel + " Fuel Crafted");//unused

                IntelInfoPlugin intel = new BaseIntelPlugin();


                member.getFleetData().getFleet().addFloatingText(CraftedSupplies + " Supplies Crafted\n" + CraftedFuel + " Fuel Crafted", ColorOfFloatingText, DurationOfFloatingText);

            }

        }

    }

    private int GetSizeAsNumber(ShipAPI.HullSize size) {
        int mult = 0;
        switch (size) {
            case CAPITAL_SHIP:
                mult++;
            case CRUISER:
                mult++;
            case DESTROYER:
                mult++;
            case FRIGATE:
                mult++;
            default:
                ;
                break;
        }
        return mult;
    }


}
