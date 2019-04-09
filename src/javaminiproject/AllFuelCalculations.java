package javaminiproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store FuelCalculation objects in an ArrayList
 *
 * @author Aditeya Viju Govind
 */
public class AllFuelCalculations extends FuelCalculation implements Serializable {

    private final List<FuelCalculation> calculations;

    public AllFuelCalculations() {
        calculations = new ArrayList<>();
    }

    public void add(FuelCalculation fc) {
        calculations.add(fc);
    }

    @Override
    public String toString() {
        String s = "";

        for (FuelCalculation f : calculations) {
            s += f + "\n";
        }

        return s;
    }

}
