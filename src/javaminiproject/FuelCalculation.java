package javaminiproject;

import java.io.Serializable;

/**
 * Fuel Calculation class used to transfer parameters and answer, over the
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

    /**
     * Blank FuelCalculation constructor
     */
    public FuelCalculation() {
        this.distance = 0;
        this.efficiency = 0;
        this.fuelType = null;
    }

    /**
     * FuelCalculation constructor with required parameters to calculate cost at
     * server
     *
     * @param distance distance vehicle travels in Miles
     * @param efficiency Efficiency of vehicle in Miles/Gallon
     * @param fuelType Type of fuel used
     */
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
     * Used to get the CSV format of the parameters and answer using
     * StringBuilder.
     *
     * @return parameters and cost in CSV format
     */
    public String toCSV() {
        StringBuilder s = new StringBuilder("");

        s.append(distance);
        s.append(",");
        s.append(efficiency);
        s.append(",");
        s.append(fuelPricePerLitre);
        s.append(",");
        s.append(totalCost);

        return s.toString();
    }

    /**
     * Gets the parameters and answer of a FuelCalculation using StringBuilder.
     *
     * @return String containing parameters and answer 
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("");

        s.append("Trip Distance = ");
        s.append(distance);
        s.append(" miles");

        s.append("\nCar's Fuel Efficiency = ");
        s.append(efficiency);
        s.append(" MPG");

        s.append("\nCost of Fuel per litre = ");
        s.append(fuelPricePerLitre);
        s.append(" £/L");

        s.append("\nFinal Fuel Cost = £ ");
        s.append(totalCost);

        return s.toString();
    }

}
