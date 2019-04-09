package javaminiproject.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import javaminiproject.AllFuelCalculations;
import javaminiproject.AppendingObjectOutputStream;
import javaminiproject.FuelCalculation;

/**
 * Fuel Cost Calculator Server is used to calculate total fuel cost and send
 * back the given parameters and answer.
 *
 * @author Aditeya Viju Govind
 */
public class Server {

    /**
     * Starts Server on port 2000
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("----------Fuel Cost Calculator Server----------");

        try (ServerSocket server = new ServerSocket(2000)) {

            while (true) {
                try (Socket client = server.accept()) {

                    BufferedReader command = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                    switch (command.readLine()) {
                        case "calculate":
                            //Getting FuelCalculation object and setting fuel price
                            FuelCalculation calculation = (FuelCalculation) in.readObject();
                            calculate(calculation);

                            try (AppendingObjectOutputStream fileWriter = new AppendingObjectOutputStream(new FileOutputStream("resources/fuelCostCalculations.csv", true))) {
                                fileWriter.writeObject(calculation);
                            } catch (IOException e) {
                            }

                            out.writeObject(calculation);
                            break;
                        case "showAll":
                            AllFuelCalculations calculations = readPreviousCalculations();
                            out.writeObject(calculations);
                            break;
                    }

                } catch (IOException | NullPointerException | ClassNotFoundException e) {
                }

            }
        } catch (IOException e) {
        }

    }

    /**
     * Reads the Fuel Price from a file and returns it according to requested
     * fuel type.
     *
     * @param fuel fuel type to be read
     * @return double value of fuel price of fuel type
     */
    public static double setFuelPrice(String fuel) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("resources/fuelPrices.txt"))) {

            switch (fuel) {
                case "98 Octane":
                    return Double.parseDouble(fileReader.readLine());
                case "Diesel":
                    fileReader.readLine();
                    return Double.parseDouble(fileReader.readLine());
            }

        } catch (IOException e) {
        }

        return 0;
    }

    public static void calculate(FuelCalculation calculation) {
        calculation.setFuelPricePerLitre(
                setFuelPrice(
                        calculation.getFuelType()));

        // Calculaion of cost
        BigDecimal distanceBD = new BigDecimal(calculation.getDistance());
        BigDecimal efficiencyBD = new BigDecimal(calculation.getEfficiency());
        BigDecimal efficiencyInMPL = efficiencyBD.divide(BigDecimal.valueOf(4.54609), 5, RoundingMode.HALF_UP);
        BigDecimal rateBD = new BigDecimal(calculation.getFuelPricePerLitre());

        BigDecimal costBD = distanceBD.multiply(rateBD.divide(efficiencyInMPL, MathContext.DECIMAL32));

        // Rounding to 2 decimal places
        DecimalFormat twoDP = new DecimalFormat("#.##");

        calculation.setTotalCost(
                Double.parseDouble(
                        twoDP.format(
                                costBD.doubleValue())));
    }

    public static AllFuelCalculations readPreviousCalculations() {

        AllFuelCalculations allPreviousCalculations = new AllFuelCalculations();
        try (ObjectInputStream filereader = new ObjectInputStream(new FileInputStream("resources/fuelCostCalculations.csv"))) {

            while (filereader.available() > 0) {

                FuelCalculation c = (FuelCalculation) filereader.readObject();

                if (c != null) {
                    allPreviousCalculations.add(c);
                    System.out.println(c.toCSV());
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allPreviousCalculations;
    }
}
