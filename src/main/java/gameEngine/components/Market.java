package gameEngine.components;

import java.util.List;

public interface Market {

    List<String> tradeGood(String shipId, String good, int quantity, String tradeAction);

    List<String> getMarketplaceInfo(String location);

}
