package org.tfr.train.strategies;

import org.tfr.train.core.Deal;
import org.tfr.train.core.MarketStrategy;
import org.tfr.train.core.OrdersAggregation;

import java.util.List;

public class DataTestStrategy extends MarketStrategy {

    // This is an example of strategy implementation

    // In order to implement you own strategy, you have to override orderBookReceived and
    // dealReceived classes of base MarketStrategy class, which are called whenever backtest
    // receives new orderbook or deal. You can also add some custom methods here, but they
    // won't be connected to the backtest, only to the inner logic of the strategy

    private static final double TRADING_AMOUNT = 10;
    private static final int BUY_DIRECTION = 1;
    private static final int SELL_DIRECTION = 2;

    private Long buyId = null; // id of the last buy order placed
    private Long sellId = null; // id of the last sell order placed

    private long countOfDeals = 0;
    private double sumPriceDelta = 0.;
    private double lastPrice = -1.;
    @Override
    public void orderBookReceived(long moment, List<OrdersAggregation> aggregations, boolean isSnapshot) {
        if (countOfDeals > 200) {
            // If countOfDeals <= 200, we don't trust them
            double position = tradingSnapshot.getPosition();
            if (sumPriceDelta > 0. && position >= 0) {
                // If Mean of derivative positive, we suppose, that price increases and want to sell
                if (sellId != null) {
                    // deleting old order
                    market.delOrder(sellId);
                    sellId = null;
                }
                sellId = market.addOrder(
                        SELL_DIRECTION,
                        Math.max(position, TRADING_AMOUNT),
                        lastPrice + sumPriceDelta / (countOfDeals * (1 + position))
                );
                // divided (1 + position), because if we have more, we can't risk
            }
            if (sumPriceDelta < 0. && position <= 0) {
                // If Mean of derivative negative, we suppose, that price decreases and want to buy
                if (buyId != null) {
                    market.delOrder(buyId);
                    buyId = null;
                }
                buyId = market.addOrder(
                        BUY_DIRECTION,
                        Math.max(-position, TRADING_AMOUNT),
                        lastPrice + sumPriceDelta / (countOfDeals * (1 - position)));
            }
        }
    }

    @Override
    public void dealReceived(long moment, Deal deal) {
        if (lastPrice > 0.) {
            sumPriceDelta += deal.getPrice() - lastPrice;
            ++countOfDeals;
        }
        lastPrice = deal.getPrice(); // We want to calculate Mean of Derivative
    }
}