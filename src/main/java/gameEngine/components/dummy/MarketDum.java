package gameEngine.components.dummy;

import gameEngine.components.Market;

import java.util.ArrayList;
import java.util.List;

public class MarketDum implements Market {

    private List<String> marketInfo,tradedGood;

    public MarketDum() {
        marketInfo = new ArrayList<>();

        marketInfo.add("marketplace information");
    }

    @Override
    public List<String> tradeGood(String shipId, String good, int quantity, String tradeAction) {
        this.tradedGood = new ArrayList<>();
        this.tradedGood.add("Traded good details");
        return tradedGood;
    }

    @Override
    public List<String> getMarketplaceInfo(String location) {
        return marketInfo;
    }

}
