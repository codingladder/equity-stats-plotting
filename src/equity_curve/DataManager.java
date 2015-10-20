package equity_curve;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// DataManger reads file and sets data to Data object
public class DataManager {
    private Scanner myScanner = null;

    // Constructor
    public DataManager(String fileName) {
        try {
            this.myScanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file. ");
        }
    }

    //  reads file and returns data
    public Data read() {
        Data data = new Data();

        while( this.myScanner.hasNext() ) {
            // split line into columns
            String[] cols = this.myScanner.nextLine().split(",");
            try {
                // if first column was gain or loss (not 0)
                if (!cols[0].equals("0")) {
                    // set x value
                    data.setxValueList(cols[1]);
                    // set y value
                    data.setyValueList(Double.parseDouble(cols[2]));
                    // set y value as percentage gain/loss from starting capital
                    data.setyPercentList(((Double.parseDouble(cols[2])
                            / (double) data.getyValueList().get(0) - 1) * 100));
                }
            } catch (Exception e) {
                System.out.println(" Exception thrown while reading file or setting data. ");
            }
        }
        this.myScanner.close();
        return data;
    }
}


