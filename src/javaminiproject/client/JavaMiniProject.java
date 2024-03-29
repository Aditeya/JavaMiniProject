package javaminiproject.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
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
import javafx.stage.WindowEvent;
import javaminiproject.AllFuelCalculations;
import javaminiproject.FuelCalculation;

/**
 * Fuel Cost Calculator made with JavaFX. Uses fuel price, fuel efficiency and
 * distance to calculate cost.
 *
 * @author Aditeya Viju Govind
 * https://github.com/Aditeya/JavaMiniProject
 */
public class JavaMiniProject extends Application {

    int ID;

    @Override
    public void start(Stage primaryStage) {

        // Client ID request
        try (Socket server = new Socket("localhost", 2000)) {
            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
            PrintWriter command = new PrintWriter(server.getOutputStream(), true);

            ObjectInputStream in = new ObjectInputStream(server.getInputStream());
            BufferedReader request = new BufferedReader(new InputStreamReader(server.getInputStream()));

            command.println("clientID");
            ID = Integer.parseInt(request.readLine());

        } catch (IOException e) {
        }

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
        Button showAll = new Button("Show All Results");
        reset.setStyle("-fx-base: ee2211;");
        showAll.setStyle("-fx-base: royalblue;");

        // Buttons added to HBox
        HBox buttons = new HBox(calculate, reset, showAll);
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
        reset.setOnAction((ActionEvent event) -> {
            distanceTF.clear();
            fuelEfficiencyTF.clear();

            try {
                fuelRB.getSelectedToggle().setSelected(false);
            } catch (NullPointerException e) {
            }

            result.setText("Result");
        });

        //Client Show all request
        showAll.setOnAction(((ActionEvent event) -> {
            try (Socket server = new Socket("localhost", 2000)) {
                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                PrintWriter command = new PrintWriter(server.getOutputStream(), true);

                command.println("showAll");
                command.println(ID);

                AllFuelCalculations calculations = (AllFuelCalculations) in.readObject();

                result.setText("All Results:\n\n" + calculations);
            } catch (IOException | ClassNotFoundException e) {
            } 
        }));

        // Client calculation request
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

            String rate = getValueFromRadioButton(fuelRB);
            if (rate.isEmpty()) {
                return;
            }

            FuelCalculation calculation = new FuelCalculation(distance, efficiency, rate);

            try (Socket server = new Socket("localhost", 2000)) {
                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                PrintWriter command = new PrintWriter(server.getOutputStream(), true);

                command.println("calculate");
                command.println(ID);

                out.writeObject(calculation);
                calculation = (FuelCalculation) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
            }

            result.setText("Result\n\n" + calculation);
        });

        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

        primaryStage.setTitle("Fuel Cost Calculator");
        primaryStage.setResizable(false);
        
        //Client delete file request
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            try (Socket server = new Socket("localhost", 2000)) {
                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                PrintWriter command = new PrintWriter(server.getOutputStream(), true);

                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                BufferedReader request = new BufferedReader(new InputStreamReader(server.getInputStream()));

                command.println("delete");
                command.println(ID);
            } catch (Exception e) {
            }
        });
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method is used to get which RadioButton has been selected and return
     * the selected fuel type as a String. It will show an Alert if a
     * RadioButton isn't selected.
     *
     * @param toggleGroup the toggle group containing the RadioButtons
     * @return returns a String as the fuel type or return an empty string if a
     * RadioButton isn't selected
     */
    public static String getValueFromRadioButton(ToggleGroup toggleGroup) {

        try {
            RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();

            return rb.getText();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please Select a Fuel!", ButtonType.OK);
            alert.showAndWait();
        }

        return "";
    }

    /**
     * This method is used to get the double value input from a TextField. It
     * displays an Alert if a double value is not entered.
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
