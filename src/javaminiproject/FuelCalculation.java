package javaminiproject;

import java.io.Serializable;

/**
 * Fuel Calculation class used to transfer parameters and answer over the
 * network.
 *
 * @author Aditeya Viju Govind
 */
public class FuelCalculation implements Serializable {

    private final double distance;
    private final double efficiency;

    private final String fuelType;
    private double fuelPricePerLitre;

    private double totalCost;

    public FuelCalculation(double distance, double efficiency, String fuelType) {
        this.distance = distance;
        this.efficiency = efficiency;
        this.fuelType = fuelType;
    }

    // setters
    public void setFuelPricePerLitre(double fuelPricePerLitre) {
        this.fuelPricePerLitre = fuelPricePerLitre;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    // getters
    public double getDistance() {
        return distance;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public String getFuelType() {
        return fuelType;
    }

    public double getFuelPricePerLitre() {
        return fuelPricePerLitre;
    }

    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Used to get the CSV format of the parameters and answer.
     *
     * @return parameters and cost in CSV format
     */
    public String toCSV() {
        return distance + "," + efficiency + "," + fuelPricePerLitre + "," + totalCost;
    }

    @Override
    public String toString() {
        return "Trip Distance = " + distance + " miles"
                + "\nCar's Fuel Efficiency = " + efficiency + " MPG"
                + "\nCost of Fuel per litre = " + fuelPricePerLitre + " £/L"
                + "\nFinal Fuel Cost = £ " + totalCost;
    }

}
