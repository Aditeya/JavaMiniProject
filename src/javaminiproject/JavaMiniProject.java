/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaminiproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * @author adite
 */
public class JavaMiniProject extends Application {

    @Override
    public void start(Stage primaryStage) {

        Label distanceLabel = new Label("Distance          (Miles)");
        Label fuelEfficiencyLabel = new Label("Fuel Efficiency (MPG)");
        Label fuelRBLabel = new Label("Fuel");
        Label resultLabel = new Label("Result");

        TextField distanceTF = new TextField();
        TextField fuelEfficiencyTF = new TextField();

        Button calculate = new Button("Calculate");
        Button reset = new Button("Reset");
        reset.setStyle("-fx-base: ee2211;");

        HBox buttons = new HBox(calculate, reset);
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        RadioButton octane = new RadioButton("98 Octane £ 1.03");
        RadioButton diesel = new RadioButton("Diesel        £ 1.05");
        ToggleGroup fuelRB = new ToggleGroup();
        fuelRB.getToggles().add(octane);
        fuelRB.getToggles().add(diesel);

        VBox radioButtons = new VBox(fuelRBLabel, octane, diesel);
        radioButtons.setSpacing(5);

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        root.add(distanceLabel, 0, 0, 1, 1);
        root.add(distanceTF, 1, 0, 1, 1);
        root.add(fuelEfficiencyLabel, 0, 1, 2, 1);
        root.add(fuelEfficiencyTF, 1, 1, 1, 1);
        root.add(radioButtons, 1, 2, 1, 1);
        root.add(new Separator(), 0, 3, 3, 1);
        root.add(resultLabel, 0, 4, 1, 1);
        root.add(new Separator(), 0, 5, 3, 1);
        root.add(buttons, 0, 6, 3, 1);

        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

        primaryStage.setTitle("Fuel Cost Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
