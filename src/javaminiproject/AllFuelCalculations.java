package javaminiproject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store FuelCalculation objects in an ArrayList
 * @author Aditeya Viju Govind
 */
public class AllFuelCalculations extends FuelCalculation {

    private final List<FuelCalculation> Calculations;

    public AllFuelCalculations() {
        Calculations = new ArrayList<>();
    }

    public void add(FuelCalculation fc){
        Calculations.add(fc);
    }
    
    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        
        for(FuelCalculation f:Calculations){
            toString.append(f);
            toString.append("\n");
        }
        
        return toString.toString();
    }

}
