package org.tfr.train.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MarketStrategy implements MarketListener {

    protected static final Logger logger = LoggerFactory.getLogger(MarketStrategy.class);

    protected final MarketEmulator market = new MarketEmulator(this);
    protected final TradingSnapshot tradingSnapshot = new TradingSnapshot();

    @Override
    public void executionReceived(Execution execution) {
        tradingSnapshot.addTrade(execution);
    }

    public void backtest(String historyPath) {
        try {
            HistoryProvider provider = new HistoryProvider(market, historyPath);
            logger.info("Backtesting started");
            provider.provide();
            logger.info("Backtest results: " + tradingSnapshot);
        } catch (Exception e) {
            logger.error("Backtest failed!", e);
        }
    }

}
