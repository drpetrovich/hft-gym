package org.tfr.train;

import org.tfr.train.strategies.DataTestStrategy;

public class BacktestRunner {

    public static void main(String[] params) {
        // You create the strategy you want to test here, and then you run it's backtest() method to see the result
        DataTestStrategy st = new DataTestStrategy();
        st.backtest("/Users/sshakhkalamov/work/data/datafortest-1.txt"); //add your path to data file here
    }
}
