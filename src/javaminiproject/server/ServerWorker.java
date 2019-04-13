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
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javaminiproject.AllFuelCalculations;
import javaminiproject.FuelCalculation;

/**
 * Serves each client sockets request
 *
 * @author Aditeya Viju Govind
 */
public class ServerWorker implements Runnable {

    protected Socket client = null;

    /**
     * ServerWorker constructor to get client socket.
     *
     * @param client Socket to be served
     */
    public ServerWorker(Socket client) {
        this.client = client;
    }

    /**
     * Method called by Thread which serves client.
     */
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                BufferedReader command = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

            switch (command.readLine()) {
                case "calculate":
                    //Getting FuelCalculation object and setting fuel price
                    FuelCalculation calculation = (FuelCalculation) in.readObject();
                    calculate(calculation);

                    out.writeObject(calculation);
                    writeFuelCalculation(calculation);
                    break;
                case "showAll":
                    out.writeObject(getAllFuelCalculations());
                    break;
            }

        } catch (IOException | ClassNotFoundException e) {
        }

        try {
            client.close();
        } catch (IOException ex) {
        }
    }

    /**
     * Reads the Fuel Price from a file and returns it according to requested
     * fuel type.
     *
     * @param fuel fuel type to be read
     * @return double value of fuel price of fuel type
     */
    public static synchronized double setFuelPrice(String fuel) {
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
    public static synchronized void writeFuelCalculation(FuelCalculation calculation) {
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

    /**
     * Reads fuelCostCalculations.csv and gets ArrayList which is returned in
     * AllFuelCalculcations Object
     *
     * @return AllFuelCalculcations Object
     */
    public static synchronized AllFuelCalculations getAllFuelCalculations() {
        List<FuelCalculation> calculations = new ArrayList<>();

        try (ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream("resources/fuelCostCalculations.csv"))) {
            calculations = (ArrayList<FuelCalculation>) fileReader.readObject();
        } catch (Exception e) {
        }

        return new AllFuelCalculations(calculations);
    }
}
