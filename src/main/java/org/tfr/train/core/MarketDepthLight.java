package org.tfr.train.core;

import java.util.*;

public class MarketDepthLight implements MarketDepth {

    private Double bestBid;
    private Double bestAsk;
    private final TreeMap<Double, OrdersAggregation> bids = new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<Double, OrdersAggregation> asks = new TreeMap<>();

    @Override
    public boolean addLevels(List<OrdersAggregation> ordersAggregations) {
        boolean spreadChanged = false;

        for (OrdersAggregation order : ordersAggregations) {
            if (order.getDirection() == 1) {
                if (order.getVolume() == 0) {
                    bids.remove(order.getPrice());
                    if (bestBid != null && order.getPrice() == bestBid) {
                        Iterator<Map.Entry<Double, OrdersAggregation>> iterator = bids.entrySet().iterator();
                        bestBid = iterator.hasNext() ? iterator.next().getKey() : null;
                        spreadChanged = true;
                    }
                }
            } else {
                if (order.getVolume() == 0) {
                    asks.remove(order.getPrice());
                    if (bestAsk != null && order.getPrice() == bestAsk) {
                        Iterator<Map.Entry<Double, OrdersAggregation>> iterator = asks.entrySet().iterator();
                        bestAsk = iterator.hasNext() ? iterator.next().getKey() : null;
                        spreadChanged = true;
                    }
                }
            }
        }

        for (OrdersAggregation order : ordersAggregations) {
            if (order.getDirection() == 1) {
                if (order.getVolume() > 0) {
                    bids.put(order.getPrice(), order);
                    if (bestBid == null || order.getPrice() > bestBid) {
                        bestBid = order.getPrice();
                        spreadChanged = true;
                    }
                }
            } else {
                if (order.getVolume() > 0) {
                    asks.put(order.getPrice(), order);
                    if (bestAsk == null || order.getPrice() < bestAsk) {
                        bestAsk = order.getPrice();
                        spreadChanged = true;
                    }
                }
            }
        }
        return spreadChanged;
    }

    @Override
    public MarketDepth refresh() {
        return new MarketDepthLight();
    }

    @Override
    public boolean isEmpty() {
        return bestAsk == null || bestBid == null;
    }

    @Override
    public Double getBestBid() {
        return bestBid;
    }

    @Override
    public Double getBestAsk() {
        return bestAsk;
    }

    @Override
    public int getSize() {
        return bids.size() + asks.size();
    }

    @Override
    public OrdersAggregation getBid(double key) {
        return bids.get(key);
    }

    @Override
    public OrdersAggregation getAsk(double key) {
        return asks.get(key);
    }

    @Override
    public Iterator<OrdersAggregation> getAskLevels() {
        return new Iterator<OrdersAggregation>() {

            private final Iterator<Map.Entry<Double, OrdersAggregation>> iterator = asks.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public OrdersAggregation next() {
                return iterator.next().getValue();
            }
        };
    }

    @Override
    public Iterator<OrdersAggregation> getBidLevels() {
        return new Iterator<OrdersAggregation>() {
            private final Iterator<Map.Entry<Double, OrdersAggregation>> iterator = bids.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public OrdersAggregation next() {
                return iterator.next().getValue();
            }
        };
    }

    @Override
    public boolean hasBid(double key) {
        return bids.containsKey(key);
    }

    @Override
    public boolean hasAsk(double key) {
        return asks.containsKey(key);
    }


    @Override
    public String toString() {
        return "MarketDepth{" +
                "bestBid=" + bestBid +
                ", bestAsk=" + bestAsk +
                '}';
    }

}
