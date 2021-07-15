package org.tfr.train.core;

import java.util.List;

public interface HistoryConsumer {

    void processDeal(Deal deal);

    void processOrdersAggregation(long moment, List<OrdersAggregation> aggregations, boolean isSnapshot);

}
