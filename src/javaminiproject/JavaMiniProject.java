package javaminiproject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
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
        Label resultLabel = new Label("Result");

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
        RadioButton octane = new RadioButton("98 Octane 1.03 £/L");
        RadioButton diesel = new RadioButton("Diesel        1.05 £/L");
        ToggleGroup fuelRB = new ToggleGroup();
        fuelRB.getToggles().add(octane);
        fuelRB.getToggles().add(diesel);

        // RadioButtons and Label added to VBox
        VBox radioButtons = new VBox(fuelRBLabel, octane, diesel);
        radioButtons.setSpacing(5);

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
        root.add(resultLabel, 0, 4, 1, 1);
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

            resultLabel.setText("Result");
        });

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
            String cost = twoDP.format(costBD.doubleValue());
            resultLabel.setText("Result"
                    + "\nDistance = " + distance + " miles"
                    + "\nFuel Efficiency = " + efficiency + " MPG"
                    + "\nFuel Rate = " + rate + " £/L"
                    + "\nTrip Cost = £ " + cost);
        });

        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

        primaryStage.setTitle("Fuel Cost Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static double getValueFromRadioButton(ToggleGroup toggleGroup) {
        double rate = 0;

        try {
            RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();

            switch (rb.getText()) {
                case "98 Octane 1.03 £/L":
                    rate = 1.03;
                    break;
                case "Diesel        1.05 £/L":
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

    public static void main(String[] args) {
        launch(args);
    }

}
