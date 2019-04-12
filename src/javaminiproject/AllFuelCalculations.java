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
        StringBuilder s = new StringBuilder("");
        
        for (FuelCalculation f : calculations) {
            s.append(f);
            s.append("\n\n");
        }
        
        s.deleteCharAt(s.length()-1);
        s.deleteCharAt(s.length()-1);
        
        return s.toString();
    }
}
