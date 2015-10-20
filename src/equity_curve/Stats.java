package equity_curve;

import java.util.ArrayList;

//
public class Stats {
    private double mean = 0;
    private double variance =0;
    private double stdDev =0;           // standard deviation
    private double sharpeRatio =0;
    private double maxDrawdown  = 0;    // in percent
    private double tradeCount =0;       // number of trades
    private double winRatio =0;         // win ratio, in percent
    private double avgLossTrade =0;     // average loss trade, in percent
    private double avgWinTrade =0;      // average win trade, in percent
    private int maxConsLosses =0;       // max consecutive losses
    private int maxConsWins =0;         // max consecutive wins
    private double largestLoss =0 ;     // in percent
    private double largestGain =0;      // in percent
    private double pReturn = 0;         // total percent return

    // Constructor
    public Stats() { mean=0; stdDev=0; sharpeRatio=0; maxDrawdown = 0;
       tradeCount =0; winRatio =0; avgLossTrade=0; avgLossTrade=0;
       avgWinTrade=0; maxConsLosses=0; maxConsWins=0; largestLoss=0;
       largestGain=0; pReturn=0; }

    // setStats
    // parameters: ArrayList of values, risk free rate
    public void setStats( ArrayList yList, double riskFreeRate ) {
        double sum = 0;
        // highest peak
        double peak = (double) yList.get(0);
        // peak's valley
        double valley = (double) yList.get(0);
        // set mean
        this.mean = calcMean(yList);
        ArrayList lossList = new ArrayList<Double>();   // to calc avg loss
        ArrayList gainList = new ArrayList<Double>();
        boolean lastWasGain = false;                    // was last a gain
        int winCount =0;                                // counts wins to get win ratio
        int consLosses =0;                              // consecutive losses
        int consGains =0;

        // iterate yList to get stats
        for( int i = 0; i < yList.size(); i++ ) {

            // for variance: sum of the squares of the differences from the mean
            sum += Math.pow( ((double)yList.get(i) - this.mean), 2 );

            // Drawdown
            if ( (double)yList.get(i) > peak )              // if new peak
                peak = valley = (double) yList.get(i);
            else if ( (double)yList.get(i) < valley ) {     // else if new valley
                valley = (double) yList.get(i);
                // check if new max drawdown
                if ((peak / valley - 1) * 100 > this.maxDrawdown)
                    this.maxDrawdown = (peak / valley - 1) * 100;
            }

            // compare gains vs losses:
            if( i != 0 ) {  // if not first value
                // if gain
                if( (double) yList.get(i) > (double) yList.get(i - 1) ) {
                    winCount++;
                    consGains++;
                    consLosses =0; // reset consecutive losses
                    // set value to list of gains
                    gainList.add(((double)yList.get(i)/(double)yList.get(i-1)-1)*100);
                    if( lastWasGain == true ) {
                        if( consGains > this.maxConsWins )
                            this.maxConsWins = consGains;
                    }
                    lastWasGain = true;
                    // largest gain:
                    if( ((double)yList.get(i)/(double)yList.get(i-1)-1)*100
                            > this.largestGain )
                        this.largestGain = ((double)yList.get(i)/(double)yList.get(i-1)-1)*100;
                }
                else {  // considered a loss
                    consLosses++;
                    consGains =0;
                    lossList.add(((double) yList.get(i) / (double) yList.get(i - 1) - 1) * 100);
                    if( lastWasGain == false ) {
                        if( consLosses > this.maxConsLosses )
                            this.maxConsLosses = consLosses;
                    }
                    lastWasGain = false;
                    // largest loss:
                    if( ((double)yList.get(i)/(double)yList.get(i-1)-1)*100
                            < this.largestLoss )
                        this.largestLoss = ((double)yList.get(i)/(double)yList.get(i-1)-1)*100;
                }
            }
        }

        // variance
        this.variance = sum / yList.size();
        // standard Deviation
        this.stdDev = Math.sqrt(variance);
        // sharpe ratio = (Mean portfolio return - Risk-free rate)
        //   divided by Standard deviation of portfolio return
        this.sharpeRatio = ( this.mean - riskFreeRate ) / this.stdDev;
        // number of trades
        this.tradeCount = yList.size();
        // no dividing by zero: win ratio, avg loss trade, avg win trade
        if( this.tradeCount != 0) {
            this.winRatio = winCount / this.tradeCount * 100;
            this.avgLossTrade = calcMean(lossList);
            this.avgWinTrade = calcMean(gainList);
        }
        // portfolio return
        this.pReturn = ((double)yList.get(yList.size()-1)/ (double)yList.get(0) -1) *100;
    }

    // Getters
    public double getMean() { return mean; }
    public double getVariance() { return variance; }
    public double getStdDev() { return stdDev; }
    public double getSharpeRatio() { return sharpeRatio; }
    public double getMaxDrawdown() { return maxDrawdown; }
    public double getTradeCount() { return tradeCount; }
    public double getWinRatio() { return winRatio; }
    public double getAvgLossTrade() { return avgLossTrade; }
    public double getAvgWinTrade() { return avgWinTrade; }
    public int getMaxConsLosses() { return maxConsLosses; }
    public int getMaxConsWins() { return maxConsWins; }
    public double getLargestLoss() { return largestLoss; }
    public double getLargestGain() { return largestGain; }
    public double getpReturn() { return pReturn; }


    // calcMean calculates and returns mean
    private double calcMean(ArrayList yList) {
        double sum =0;
        for( int i=0; i< yList.size(); i++ )
            sum += (double)yList.get(i);
        // no dividing by zero
        if( yList.size() > 0 )
            return sum/yList.size();
        else return 0;
    }
}

