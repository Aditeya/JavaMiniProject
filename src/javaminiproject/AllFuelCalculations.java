package javaminiproject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aditeya Viju Govind
 */
public class AllFuelCalculations extends FuelCalculation {

    private final List<FuelCalculation> calculations;

    public AllFuelCalculations(List<FuelCalculation> calculations) {
        this.calculations = calculations;
    }

    public AllFuelCalculations() {
        calculations = new ArrayList<>();
    }

    public void add(FuelCalculation f) {
        calculations.add(f);
    }

    @Override
    public String toString() {
        String s = "";

        for (FuelCalculation f : calculations) {
            s += f + "\n\n";
        }

        return s;
    }
}
