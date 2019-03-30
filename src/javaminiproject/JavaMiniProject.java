package javaminiproject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Fuel Cost Calculator made with JavaFX.
 * Uses fuel price, fuel efficiency and distance 
 * to calculate cost.
 * 
 * @author Aditeya Viju Govind
 */
public class JavaMiniProject extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Labels
        Label distanceLabel = new Label("Distance          (Miles)");
        Label fuelEfficiencyLabel = new Label("Fuel Efficiency (MPG)");
        Label fuelRBLabel = new Label("Fuel");

        // TextFields for input
        TextField distanceTF = new TextField();
        TextField fuelEfficiencyTF = new TextField();

        // Buttons for calculation and resetting
        Button calculate = new Button("Calculate");
        Button reset = new Button("Reset");
        reset.setStyle("-fx-base: ee2211;");

        // Buttons added to HBox
        HBox buttons = new HBox(calculate, reset);
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        // RadioButtons and a ToggleGroup
        RadioButton octane = new RadioButton("98 Octane");
        RadioButton diesel = new RadioButton("Diesel");
        ToggleGroup fuelRB = new ToggleGroup();
        fuelRB.getToggles().add(octane);
        fuelRB.getToggles().add(diesel);

        // RadioButtons and Label added to VBox
        VBox radioButtons = new VBox(fuelRBLabel, octane, diesel);
        radioButtons.setSpacing(5);
        
        // result in stored in a scrollPane
        Text result = new Text("Result");
        ScrollPane resultBox = new ScrollPane(result);
        resultBox.setPrefHeight(100);

        // GridPane spacing and padding
        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        // Adding elements to GridPane
        root.add(distanceLabel, 0, 0, 1, 1);
        root.add(distanceTF, 1, 0, 1, 1);
        root.add(fuelEfficiencyLabel, 0, 1, 2, 1);
        root.add(fuelEfficiencyTF, 1, 1, 1, 1);
        root.add(radioButtons, 1, 2, 1, 1);
        root.add(new Separator(), 0, 3, 3, 1);
        root.add(resultBox, 0, 4, 3, 1);
        root.add(new Separator(), 0, 5, 3, 1);
        root.add(buttons, 0, 6, 3, 1);

        // Reset Button functionality
        reset.setOnAction((event) -> {
            distanceTF.clear();
            fuelEfficiencyTF.clear();

            try {
                fuelRB.getSelectedToggle().setSelected(false);
            } catch (NullPointerException e) {
            }

            result.setText("Result");
        });

        // Calculation button functionality done with BigDecimal and DecimalFormat
        calculate.setOnAction((ActionEvent event) -> {
            double distance = getValueFromTextField(distanceTF, "distance");

            if (distance == 0) {
                distanceTF.clear();
                return;
            }

            double efficiency = getValueFromTextField(fuelEfficiencyTF, "fuel efficiency");

            if (efficiency == 0) {
                fuelEfficiencyTF.clear();
                return;
            }

            double rate = getValueFromRadioButton(fuelRB);
            if (rate == 0) {
                return;
            }

            BigDecimal distanceBD = new BigDecimal(distance);
            BigDecimal efficiencyBD = new BigDecimal(efficiency);
            BigDecimal efficiencyInMPL = efficiencyBD.divide(BigDecimal.valueOf(4.54609), 5, RoundingMode.HALF_UP);
            BigDecimal rateBD = new BigDecimal(rate);

            BigDecimal costBD = distanceBD.multiply(rateBD.divide(efficiencyInMPL, MathContext.DECIMAL32));

            DecimalFormat twoDP = new DecimalFormat("#.##");

            result.setText("Result\n"
                    + "\nDistance = " + distance + " miles"
                    + "\nFuel Efficiency = " + efficiency + " MPG"
                    + "\nFuel Rate = " + rate + " £/L"
                    + "\nTrip Cost = £ " + twoDP.format(costBD.doubleValue()));
        });

        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

        primaryStage.setTitle("Fuel Cost Calculator");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method is used to get which RadioButton has been selected and return the selected value.
     * It will show an Alert if a RadioButton isn't selected.
     * 
     * @param toggleGroup the toggle group containing the RadioButtons
     * @return returns a double as the fuel price or return 0 if a RadioButton isn't selected
     */
    public static double getValueFromRadioButton(ToggleGroup toggleGroup) {
        double rate = 0;

        try {
            RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();

            switch (rb.getText()) {
                case "98 Octane":
                    rate = 1.03;
                    break;
                case "Diesel":
                    rate = 1.05;
                    break;
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please Select a Fuel!", ButtonType.OK);

            alert.showAndWait();
            return 0;
        }

        return rate;
    }

     /**
     * This method is used to get the double value input from a TextField. It displays
     * an Alert if a double value is not entered.
     * 
     * @param textField TextField to get the double value
     * @param name name of the TextField for the Alert
     * @return returns a double value acquired from the TextField
     */
    public static double getValueFromTextField(TextField textField, String name) {
        String s = textField.getText();
        double d = 0;

        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Input for " + name + " is wrong!\n"
                    + "Please type a number!", ButtonType.OK);

            alert.showAndWait();
            return 0;
        }

        if (d <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Input for " + name + " is wrong!\n"
                    + "Number should be positive", ButtonType.OK);

            alert.showAndWait();
            return 0;
        }

        return d;
    }

    /**
     * main method
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }

}
