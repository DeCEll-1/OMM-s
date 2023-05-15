package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.ommGen;
import exerelin.campaign.SectorManager;

public class FreitagCorporation_ModPlugin extends BaseModPlugin {

    public static final boolean isExerelin;

    static {
        boolean foundExerelin;
        if (Global.getSettings().getModManager().isModEnabled("nexerelin")) {
            foundExerelin = true;
        } else {
            foundExerelin = false;
        }
        isExerelin = foundExerelin;
    }

    @Override
    public void onNewGame() {
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getCorvusMode()) {
            new ommGen().generate(Global.getSector());
        }
    }
}
