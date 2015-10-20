package equity_curve;

import java.math.BigDecimal;
import java.util.ArrayList;

// Data object stores x and y values
public class Data {
    private ArrayList xValueList;
    private ArrayList yValueList;
    private ArrayList yPercentList;  // percentages: gain/loss from starting capital

    // Constructor
    Data() {
        this.xValueList = new ArrayList<>();
        this.yValueList = new ArrayList<Double>();
        this.yPercentList = new ArrayList<Double>();
    }

    // Setters
    public void setxValueList(String x) { this.xValueList.add(x); }
    public void setyValueList(Double y) { this.yValueList.add(getPrecision(y,2)); }
    public void setyPercentList(Double y) { this.yPercentList.add(getPrecision(y,1));}

    // Getters
    public ArrayList getxValueList() { return xValueList; }
    public ArrayList getyValueList() { return yValueList; }
    public ArrayList getyPercentList() { return yPercentList; }

    // getPrecision
    // takes double y value and n is precision to be set
    // returns precision set value, rounded up
    public double getPrecision(double y, int n) {
        BigDecimal bigDecimal = new BigDecimal(y);
        return bigDecimal.setScale(n, BigDecimal.ROUND_UP).doubleValue();
    }
}
