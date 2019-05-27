/*
 * FILE:        WagesEvent.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Modifies a property value by +/-5%.
 *              Changes an individual, specific, property to increase or decrease the value.
 *              This class is constructed by a Factory, depending on the specific type of event.
 */

package TrainingSystem.controller;

import java.util.*;
import TrainingSystem.model.*;

public class ValueEvent implements IEvent
{
    /* 
     * change - Implements the modification to a property value, propName,
     * on the property list propList.
     */
    @Override
    public void change(List<Property> propList, String propName, double modifier)
    {
        Boolean found = false;
        Iterator<Property> iter = propList.iterator();
        while (iter.hasNext() && !found)
        {
            Property prop = iter.next();

            // If the property is directly in the property list.
            if (prop.getName().equals(propName))
            {
                prop.updateValue(modifier);
                found = true;
            }
            else // If not, recurse through the properties owned by the different companies.
            {
                if (prop instanceof Company)
                {
                    Company tempComp = ((Company) prop).ownsCompany(propName);
                    BusinessUnit tempBus = ((Company) prop).ownsBusinessUnit(propName);

                    if (tempComp != null)
                    {
                        tempComp.updateValue(modifier);
                        found = true;
                    }
                    else if (tempBus != null)
                    {
                        tempBus.updateValue(modifier);
                        found = true;
                    }
                }
            }
        }

        // If a property was never found.
        if (!found)
        {
            throw new IllegalArgumentException("Error: Property: \"" + propName + "\" not found.");
        }
    }
}
