package org.tfr.train.core;

import java.util.List;

public interface MarketListener {

    //Market Data

    void dealReceived(long moment, Deal deal);

    void executionReceived(Execution execution);

    void orderBookReceived(long moment, List<OrdersAggregation> ordersAggregation, boolean isSnapshot);

}
