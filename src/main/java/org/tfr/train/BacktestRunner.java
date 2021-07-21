package org.tfr.train;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.tfr.train.core.TradingSnapshot;
import org.tfr.train.strategies.DataTestStrategy;

import java.io.File;
import java.io.IOException;

public class BacktestRunner {

    public static void main(String[] params) throws IOException {
        // You create the strategy you want to test here, and then you run it's backtest() method to see the result
        DataTestStrategy st = new DataTestStrategy();
//        st.backtest("/Users/sshakhkalamov/work/data/datafortest-1.txt"); //add your path to data file here
        st.backtest("/home/alexey/dev/temp/datafortest-1.txt"); //add your path to data file here


        final TimeSeries series = new TimeSeries("Backtest");

        final XYDataset dataset = new TimeSeriesCollection(series);
        JFreeChart timechart = ChartFactory.createTimeSeriesChart(
                "Backtest",
                "Time",
                "Pnl",
                dataset,
                false,
                false,
                false);
        int width = 560;   /* Width of the image */
        int height = 370;  /* Height of the image */
        File timeChart = new File("backtest.jpeg");

        TradingSnapshot tradingSnapshot = new TradingSnapshot();
        st.getTradingSnapshot().getTrades().forEach((e) -> {
            tradingSnapshot.addTrade(e);
            try {
                series.add(new FixedMillisecond(e.getMoment()), new Double(tradingSnapshot.getMargin() - tradingSnapshot.getTotalFee()));
            } catch (Exception exc) {
                //ignore
            }
        });
        ChartUtils.saveChartAsJPEG(timeChart, timechart, width, height);
        System.out.println("Final pnl: " + (tradingSnapshot.getMargin() - tradingSnapshot.getTotalFee()) + ", final position: " + tradingSnapshot.getPosition());
    }
}
