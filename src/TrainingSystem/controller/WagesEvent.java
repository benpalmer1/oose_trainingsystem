/*
 * FILE:        WagesEvent.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Modifies the simulation wages by +/-5%
 *              Changes every property individually to increase or decrease the wages.
 */

package TrainingSystem.controller;
import java.util.*;
import TrainingSystem.model.*;

public class WagesEvent implements IEvent
{
    // propName is unused for a wages event.
    @Override
    public void change(List<Property> propList, String propName, double modifier)
    {
        for(Property p : propList)
        {
            if(p instanceof Company)
            {
                ((Company)p).updateWages(modifier);
            }
            else
            {
                ((BusinessUnit)p).updateWages(modifier);
            }
        }
    }  
}
