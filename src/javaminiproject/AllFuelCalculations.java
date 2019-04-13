package javaminiproject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store FuelCalculation objects in an ArrayList
 *
 * @author Aditeya Viju Govind
 */
public class AllFuelCalculations extends FuelCalculation {

    private final List<FuelCalculation> calculations;

    /**
     * AllFuelCalculations constructor used to add ArrayList of FuelCalculation
     * objects.
     *
     * @param calculations adds ArrayList to object
     */
    public AllFuelCalculations(List<FuelCalculation> calculations) {
        this.calculations = calculations;
    }

    /**
     * AllFuelCalculations constructor used to create object with ArrayList.
     */
    public AllFuelCalculations() {
        calculations = new ArrayList<>();
    }

    /**
     * Adds FuelCalculation object to ArrayList
     *
     * @param f FuelCalculation object to be added
     */
    public void add(FuelCalculation f) {
        calculations.add(f);
    }

    /**
     * Uses StringBuilder to make string of all calculations by calling each
     * objects toString method
     *
     * @return String containing all calculations
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("");

        for (FuelCalculation f : calculations) {
            s.append(f);
            s.append("\n\n");
        }

        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);

        return s.toString();
    }
}
