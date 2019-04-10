package javaminiproject.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javaminiproject.FuelCalculation;

/**
 * Fuel Cost Calculator Server is used to calculate total fuel cost, send back
 * the given parameters & answer and write it to a file.
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

                    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                    //Getting FuelCalculation object and calculate price
                    FuelCalculation calculation = (FuelCalculation) in.readObject();
                    calculate(calculation);

                    writeFuelCalculation(calculation);
                    out.writeObject(calculation);

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

    /**
     * Writes FuelCalculation Object to file.
     *
     * @param calculation FuelCalculation object to write
     */
    public static void writeFuelCalculation(FuelCalculation calculation) {
        List<FuelCalculation> calculations = new ArrayList<>();

        try (ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream("resources/fuelCostCalculations.csv"))) {
            calculations = (ArrayList<FuelCalculation>) fileReader.readObject();
        } catch (Exception e) {
        }

        try (ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream("resources/fuelCostCalculations.csv"))) {
            calculations.add(calculation);
            fileWriter.writeObject(calculations);
        } catch (Exception e) {
        }
    }

    /**
     * Calculates cost and sets fuel price.
     *
     * @param calculation FuelCalculation object result to be calculated
     */
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
}
