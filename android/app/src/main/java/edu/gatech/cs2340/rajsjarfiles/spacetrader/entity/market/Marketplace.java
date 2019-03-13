package edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.market;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Random;

import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.market.transaction.MarketTransactionValidator;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.market.transaction.TransactionOrder;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.market.transaction.TransactionResult;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.Events;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.ResourceClassification;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.TechLevel;


/**
 *  Representation of a Planet's Marketplace based on the planet's tech level and trade good's
 *  attributes
 */
public class Marketplace{

    private static Random rand = new Random();

    // EnumMap that store trade goods and its quantity
    private EnumMap<TradeGoods, Item> tradeGoodsInMarketMap;

    private String planetName;
    private TechLevel techLevel;
    private ResourceClassification resource;
    private Events event;

    /**
     * Constructor for marketplace
     *
     * @param pn planet name
     * @param techLevel tech level
     * @param event events
     * @param resource resource class
     */
    public Marketplace(String pn,
                       TechLevel techLevel, Events event, ResourceClassification resource) {

        this.planetName = pn;
        this.techLevel = techLevel;
        this.resource = resource;
        this.event = event;

        tradeGoodsInMarketMap = new EnumMap<>(TradeGoods.class);

        // Build item list for this marketplace based on its tech level.
        for (TradeGoods goods : TradeGoods.values()) {
            // Check minimum tech level to produce certain goods
            if (techLevel.ordinal() >= goods.getMTLP()) {
                Item item = new Item.ItemBuilder(goods)
                        .quantity(calculateQuantity(goods))
                        .price(calculatePrice(goods))
                        .build();
                tradeGoodsInMarketMap.put(goods, item);
            }
        }
    }

    private int calculatePrice(Good good) {
        int basePrice = good.getBasePrice();
        int dynamicPrice = good.getIPL() * (techLevel.ordinal() - good.getMTLP());
        int variancePrice = (basePrice * rand.nextInt(good.getVar())) / 10;
        int finalPrice = basePrice + dynamicPrice +variancePrice;

        if (resource == good.getCR()) {
            finalPrice = finalPrice / 3 * 2;
        }

        if (resource == good.getER()) {
            finalPrice = finalPrice * 3 / 2;
        }

        if (event == good.getIE()) {
            finalPrice = finalPrice * 3;
        }

        return finalPrice;
    }

    private int calculateQuantity(Good good) {
        int maxQuantity = 30;
        int minQuantity = 1;

        if (resource == good.getCR()) {
            minQuantity = 15;
        }

        if (resource == good.getER()) {
            maxQuantity = 15;
        }

        if (event == good.getIE()) {
            minQuantity = minQuantity / 2;
            maxQuantity = maxQuantity / 2;
        }

        return rand.nextInt(maxQuantity - minQuantity) + minQuantity;
    }

    // Validate transaction.
    public TransactionResult validateTransaction(TransactionOrder to) {
        MarketTransactionValidator validator = new MarketTransactionValidator(this);
        return validator.validateNTransaction(to);
    }

    /**
     * Getter for getting tech level of the planet which the marketplace is located.
     *
     * @return techlevel of the planet which the marketplace is located
     */
    public TechLevel getTechLevel() {
        return techLevel;
    }

    /**
     * Getter for whole index of goods that this marketplace has.
     *
     * @return enumMap containing index of goods that this marketplace has.
     */
    public EnumMap<TradeGoods, Item> getTradeGoodsInMarket() {
        return tradeGoodsInMarketMap;
    }

    public Collection<Item> getItems() {
        return tradeGoodsInMarketMap.values();
    }

    public Item getItem(Good good) {
        return tradeGoodsInMarketMap.get((TradeGoods)good);
    }

    public int getMarketPrice(Good good) {
        return tradeGoodsInMarketMap.get(good).getPrice();
    }

    public int getMarketQuantity(Good good) {
        return tradeGoodsInMarketMap.get(good).getPrice();
    }
    @Override
    public String toString() {
        String str = "\n["+planetName+"]";
        str += "\n/////////////////// Goods for buying /////////////////////\n";
        for (Item item: this.getItems()) {
            str += String.format("Name: %-7s\n", item.getGoodName());
            str += String.format("| price: %-5d",item.getPrice());
            str += String.format("| quantity: %-3d", item.getQuantity());
            str += "\n";
        }
        return str;
    }
}