package org.tfr.train.core;

import java.util.Iterator;
import java.util.List;

public interface MarketDepth {

    boolean addLevels(List<OrdersAggregation> ordersAggregations);

    MarketDepth refresh();

    boolean isEmpty();

    Double getBestBid();

    Double getBestAsk();

    int getSize();

    boolean hasBid(double key);

    boolean hasAsk(double key);

    OrdersAggregation getBid(double key);

    OrdersAggregation getAsk(double key);

    Iterator<OrdersAggregation> getAskLevels();

    Iterator<OrdersAggregation> getBidLevels();

    default String getAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bids:");
        Iterator<OrdersAggregation> levels = getBidLevels();
        while (levels.hasNext()) {
            OrdersAggregation next = levels.next();
            sb.append("\n").append(next.toString());
        }
        levels = getAskLevels();
        sb.append("\nAsks:");
        while (levels.hasNext()) {
            OrdersAggregation next = levels.next();
            sb.append("\n").append(next.toString());
        }
        return sb.toString();
    }

    static MarketDepth createMarketDepth() {
        return new MarketDepthLight();
    }

}
