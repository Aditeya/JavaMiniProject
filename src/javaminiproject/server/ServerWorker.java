package javaminiproject.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Socket;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaminiproject.AllFuelCalculations;
import javaminiproject.FuelCalculation;

/**
 * Serves each client sockets request
 *
 * @author Aditeya Viju Govind
 */
public class ServerWorker implements Runnable {

    protected Socket client = null;
    protected ServerClientID serverClientID;

    /**
     * ServerWorker constructor to get client socket and ServerClientID object.
     *
     * @param client Socket to be served
     * @param serverClientID ServerClientID if a clientID is requested;
     */
    public ServerWorker(Socket client, ServerClientID serverClientID) {
        this.client = client;
        this.serverClientID = serverClientID;
    }

    /**
     * Method called by Thread which serves client.
     */
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                PrintWriter id = new PrintWriter(client.getOutputStream(), true);
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                BufferedReader command = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

            switch (command.readLine()) {
                case "clientID":
                    id.println(serverClientID.generateClientID());
                    break;
                case "calculate":
                    int clientID = Integer.parseInt(command.readLine());
                    //Getting FuelCalculation object and setting fuel price
                    FuelCalculation calculation = (FuelCalculation) in.readObject();
                    calculate(calculation);

                    out.writeObject(calculation);
                    writeFuelCalculation(calculation, clientID);
                    break;
                case "showAll":
                    clientID = Integer.parseInt(command.readLine());
                    out.writeObject(getAllFuelCalculations(clientID));
                    break;
                case "delete":
                    clientID = Integer.parseInt(command.readLine());
                    delete(clientID);
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
     * Deletes file specified;
     *
     * @param ID clientID to specify file to delete
     */
    public static synchronized void delete(int ID) {
        File f = new File("resources/fuelCalculations" + ID + ".dat");
        try {
            Files.deleteIfExists(f.toPath());
        } catch (IOException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
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
     * Writes FuelCalculation Object to file, with specified ID.
     *
     * @param calculation FuelCalculation object to write
     * @param ID clientID to specify file to write too
     */
    public static synchronized void writeFuelCalculation(FuelCalculation calculation, int ID) {
        File f = new File("resources/fuelCalculations" + ID + ".dat");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        List<FuelCalculation> calculations = new ArrayList<>();

        try (ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(f))) {
            calculations = (ArrayList<FuelCalculation>) fileReader.readObject();
        } catch (Exception e) {
        }

        try (ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(f))) {
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
     * Reads fuelCalculations.dat, with specified ID, and gets ArrayList which
     * is returned in AllFuelCalculcations Object.
     *
     * @param ID clientID to specify file to read
     * @return AllFuelCalculcations Object
     */
    public static synchronized AllFuelCalculations getAllFuelCalculations(int ID) {
        File f = new File("resources/fuelCalculations" + ID + ".dat");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        List<FuelCalculation> calculations = new ArrayList<>();

        try (ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(f))) {
            calculations = (ArrayList<FuelCalculation>) fileReader.readObject();
        } catch (Exception e) {
        }

        return new AllFuelCalculations(calculations);
    }
}
