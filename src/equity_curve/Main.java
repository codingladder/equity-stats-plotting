package equity_curve;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collections;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        // csv file path
        String title = "2014 Equity Curve";
        String fileName = "E:/__EURUSD/trading_2014.csv";

        VBox root = new VBox();                                 // VBox to place into
        DataManager myDataManager = new DataManager(fileName);  // open file
        Data myData = myDataManager.read();                     // read file & get returned data

        /// setup chart properties:
        NumberAxis xAxis = new NumberAxis();
        // set y axis constraints: add 0.0001 to max value and subtract 0.0001 from min
        NumberAxis yAxis
                = new NumberAxis((double) Collections.min(myData.getyPercentList())
                - 0.0001, (double) Collections.max(myData.getyPercentList()) + 0.0001, 5);
        LineChart<Number, Number> myChart = new LineChart<>(xAxis, yAxis);
        myChart.setTitle(title);
        xAxis.setLabel("Trade");
        yAxis.setLabel("% Return");
        XYChart.Series series = new XYChart.Series();
        series.setName("Equity");
        // iterate percentage values: set values to chart series
        for (int i = 0; i < myData.getyPercentList().size(); i++)
            series.getData().add(new XYChart.Data(i, myData.getyPercentList().get(i)));
        myChart.getData().add(series);

        // Stats object computes and stores stats of data
        Stats myStats = new Stats();
        // setStats parameters: list, risk free rate
        myStats.setStats(myData.getyValueList(), 2.0);

        // Setup Gridpane & format to display stats
        GridPane textPane = new GridPane();
        textPane.add(new Text(""), 0, 0); // column=0 row=0
        textPane.add(new Text("Return: "), 0, 1);
        textPane.add(new Text(myData.getPrecision(myStats.getpReturn(), 1) + "%"), 1, 1); // column=0 row=1
        textPane.add(new Text("Max Drawdown:  "), 0, 2);
        textPane.add(new Text(myData.getPrecision(myStats.getMaxDrawdown(), 1) + "%\t"), 1, 2);
        textPane.add(new Text("Sharpe Ratio:  "), 0, 3);
        textPane.add(new Text(myData.getPrecision(myStats.getSharpeRatio(), 1) + "%"), 1, 3);
        textPane.add(new Text("\tWin Ratio:  "), 2, 1);
        textPane.add(new Text(myData.getPrecision(myStats.getWinRatio(), 1) + "% \t"), 3, 1);
        textPane.add(new Text("\tAvg Loss:   "), 2, 2);
        textPane.add(new Text(myData.getPrecision(myStats.getAvgLossTrade(), 1) + "%"), 3, 2);
        textPane.add(new Text("\tAvg Gain:   "), 2, 3);
        textPane.add(new Text(myData.getPrecision(myStats.getAvgWinTrade(), 1) + "%"), 3, 3);
        textPane.add(new Text("\tStd Dev: "), 4, 1);
        textPane.add(new Text(myData.getPrecision(myStats.getStdDev(), 1) + "%"), 5, 1);
        textPane.add(new Text("\tLargest Loss:   "), 4, 2);
        textPane.add(new Text(myData.getPrecision(myStats.getLargestLoss(), 1) + "%    \t"), 5, 2);
        textPane.add(new Text("\tLargest Gain:   "), 4, 3);
        textPane.add(new Text(" " + myData.getPrecision(myStats.getLargestGain(), 1) + "%"), 5, 3);
        textPane.add(new Text("\tTotal Trades: "), 6, 1);
        textPane.add(new Text(Integer.toString(myData.getyValueList().size())), 7, 1);
        textPane.add(new Text("\tConsecutive Losses:  "), 6, 2);
        textPane.add(new Text(" " + Integer.toString(myStats.getMaxConsLosses())), 7, 2);
        textPane.add(new Text("\tConsecutive Gains:  "), 6, 3);
        textPane.add(new Text(" " + Integer.toString(myStats.getMaxConsWins())), 7, 3);
        textPane.add(new Text(""), 0, 5);

        // style effects for chart
        myChart.setPadding(new Insets(30, 50, 0, 0));
        myChart.setStyle(
                "-fx-background-color: whitesmoke; " +
                        "-fx-background-insets: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);"
        );

        // set style effect for text Gridpane
        textPane.setStyle(
                "-fx-background-color: whitesmoke; " +
                        "-fx-background-insets: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);"
        );
        textPane.setAlignment(Pos.CENTER);
        GridPane.setHgrow(textPane, Priority.ALWAYS);

        // Menu bar
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open File");
        MenuItem exit = new MenuItem("Exit");
        file.getItems().addAll(openFile, exit);
        menuBar.getMenus().addAll(file);

        // add children
        root.getChildren().addAll(menuBar, myChart, textPane);
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, 900, 600));
        // allow VBox to expand
        VBox.setVgrow(myChart, Priority.ALWAYS);
        primaryStage.show();
    }
}
