package org.treez.javafxd3.d3.arrays;


import org.treez.javafxd3.d3.arrays.foreach.ForEachCallback;
import org.treez.javafxd3.d3.arrays.foreach.ForEachCallbackWrapper;
import org.treez.javafxd3.d3.arrays.foreach.NumericForEachCallback;
import org.treez.javafxd3.d3.core.Value;

import javafx.scene.web.WebEngine;

/**
 * 
 * 
 */
public class Callbacks {

    /**
     * Return a callback that return true only if the element is greater than the given value.
     * 
     * @param than
     * @return
     */
    public static ForEachCallback<Boolean> greaterThan(final double than, WebEngine webEngine) {
        return new ForEachCallbackWrapper<>(Double.class, webEngine, (value)->{
        	 System.out.println("received " + value + " > " + than + " : "
                     + (value > than));
             return value > than;
        });
        
        
       
    }

    /**
     * Return a callback that add the given int to each numeric element
     * 
     * @param i the number to add
     * @return the callback
     */
    public static NumericForEachCallback add(final int i, WebEngine webEngine) {
        return new NumericForEachCallback() {
            @Override
            public Double forEach(final Object thisArg, final Object element, final int index, final Object array) {
            	
            	Value value = (Value) element;
                return value.asDouble() + i;
            }
        };
    }
}
