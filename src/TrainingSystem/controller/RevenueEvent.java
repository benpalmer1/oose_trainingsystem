/*
 * FILE:        WagesEvent.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Modifies the revenue of a specific BusinessUnit by +/-5%.
 */
package TrainingSystem.controller;

import java.util.*;
import TrainingSystem.model.*;

public class RevenueEvent implements IEvent
{

    /*
     * change - Change the revenue of a specific business unit by +/- 5%. 
     */
    @Override
    public void change(List<Property> propList, String propName, double modifier)
    {
        BusinessUnit tempBus = null;

        Iterator<Property> iter = propList.iterator();
        while (iter.hasNext() && (tempBus == null))
        {
            Property prop = iter.next();
            
            // If the property is directly in the property list.
            if (prop instanceof BusinessUnit)
            {
                if (prop.getName().equals(propName))
                {
                    ((BusinessUnit) prop).updateRevenue(modifier);
                    tempBus = (BusinessUnit) prop;
                }
            }
            else // prop is a company.
            {
                // Recurse through the BusinessUnits owned by the different companies.
                tempBus = ((Company) prop).ownsBusinessUnit(propName);

                if (tempBus != null)
                {
                    tempBus.updateRevenue(modifier);
                }
            }
        }

        // If a BusinessUnit with that name was never found.
        if (tempBus == null)
        {
            throw new IllegalArgumentException("Error: Business Unit: \"" + propName + "\" not found.");
        }
    }
}
