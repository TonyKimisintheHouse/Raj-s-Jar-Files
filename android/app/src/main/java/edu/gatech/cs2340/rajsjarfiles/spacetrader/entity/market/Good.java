package edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.market;

import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.PlanetEvents;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.ResourceClassification;

import static edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.market.TradeGoods.FIREARMS;
import static edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.market.TradeGoods.NARCOTICS;

/**
 * Interface for good (tradeGoods, other goods, ship etc)
 */
public interface Good {
    /**
     * @return returns the good's name
     */
    String getName();

    /**
     * @return minimum tech level to produce
     */
    int getMTLP();

    /**
     * @return minimum tech level to use (can sell on a planet)
     */
    int getMTLU();

    /**
     * @return tech level that produces the most of this item
     */
    int getTTP();

    /**
     * @return base price
     */
    int getBasePrice();

    /**
     * @return price increase per level
     */
    int getIPL();

    /**
     * @return variance (max percentage the price can vary)
     */
    int getVar();

    /**
     * @return radical price increase event
     */
    PlanetEvents getIE();

    ResourceClassification getCR();
    ResourceClassification getER();

    /**
     * @return min price offered when trading
     */
    int getMTL();

    /**
     * @return max price offered when trading
     */
    int getMTH();
    boolean isIllegal();

    public static final Good[] ILLEGAL_GOODS
            = new Good[] {FIREARMS, NARCOTICS};
}
