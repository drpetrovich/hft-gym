package org.tfr.train.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class HistoryProvider {

    private final static int MAX_DEPTH = 200;
    protected static final Logger logger = LoggerFactory.getLogger(HistoryProvider.class);

    private final MarketEmulator market;

    private final File historyFile;
    private final FileReader historyFileReader;
    private final BufferedReader historyBufferedReader;

    public HistoryProvider(MarketEmulator market, String dataDir) throws FileNotFoundException {
        this.market = market;
        this.historyFile = new File(dataDir);
        this.historyFileReader = new FileReader(historyFile);
        this.historyBufferedReader = new BufferedReader(historyFileReader);
    }

    public void provide() throws IOException {
        String line;
        while ((line = historyBufferedReader.readLine()) != null) {
            String[] lSplitted = line.split(",");
            if (lSplitted[0].equals("1")) {
                // we have received deal
                Deal hDeal = new Deal();
                hDeal.setMoment(Long.parseLong(lSplitted[1]));
                hDeal.setPrice(Double.parseDouble(lSplitted[2]));
                hDeal.setAmount(Double.parseDouble(lSplitted[3]));
                market.processDeal(hDeal);
            } else if (lSplitted[0].equals("0")) {
                // we have received orderbook update
                ArrayList<OrdersAggregation> hOrderbookUpdate = new ArrayList<>();
                for (int i = 0; i < MAX_DEPTH; i++) {
                    OrdersAggregation hOrdersAggregation = new OrdersAggregation();
                    hOrdersAggregation.setDirection(1);
                    hOrdersAggregation.setPrice(Double.parseDouble(lSplitted[2 + 2 * i]));
                    hOrdersAggregation.setVolume(Double.parseDouble(lSplitted[3 + 2 * i]));
                    hOrdersAggregation.setId(i);
                    hOrderbookUpdate.add(hOrdersAggregation);
                }
                for (int i = 0; i < MAX_DEPTH; i++) {
                    OrdersAggregation hOrdersAggregation = new OrdersAggregation();
                    hOrdersAggregation.setDirection(2);
                    hOrdersAggregation.setPrice(Double.parseDouble(lSplitted[402 + 2 * i]));
                    hOrdersAggregation.setVolume(Double.parseDouble(lSplitted[403 + 2 * i]));
                    hOrdersAggregation.setId(200 + i);
                    hOrderbookUpdate.add(hOrdersAggregation);
                }

                market.processOrdersAggregation(Long.parseLong(lSplitted[1]), hOrderbookUpdate, true);
            } else {
                logger.warn("String of illegal type received: {}", line);
            }
        }
        logger.info("Market emulation finished!");
    }

}
