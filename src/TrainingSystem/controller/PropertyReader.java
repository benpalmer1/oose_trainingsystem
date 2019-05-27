/*
 * FILE:        PropertyReader.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Reads files containing the different types of property.
 *              Correctly orders the returned propertyList.
 *              Decided to use lists instead of maps (unlike companies),
 *              to maintain entry order.
 */
package TrainingSystem.controller;

import TrainingSystem.model.*;
import java.util.*;
import java.io.*;

public class PropertyReader<O> extends TrainingFileReader<O>
{

    public static final int NUM_ELEMENTS = 6;
    public static final String HEADER = "Name,Type,Owner,Worth,Revenue,Wages";

    /*
     * parse - reads a property file, and constructs a list of property objets
     * with the correct structure, ensuring that the primary company is the first
     * in the resultant output property list.
     */
    @Override
    @SuppressWarnings("unchecked") // Suppressed warning for "unchecked" cast between O (generic type) and Property.
    protected List<O> parse(BufferedReader lineReader, List<O> propertyList) throws IOException
    {
        char tempType;
        double tempWorth, tempRevenue, tempWages;
        String newLine = lineReader.readLine();
        String tempName, tempData[];
        Property newProperty = null;
        Company tempOwner = null;
		
		if(newLine != null)
		{
	        newLine.replaceAll("\\s", ""); // The header line, remove any whitespace for a check.
	        if (!newLine.equals(HEADER))
	        {
	            throw new IOException("Error: Header file does not match specification: " + HEADER);
	        }
		}
		else
		{
			throw new IOException("Error: Property file does not match specification.");
		}

        newLine = lineReader.readLine();
        while (newLine != null)
        {
            errorCheck(newLine); // If no exception thrown, everything semantically checks outs, business rules are confirmed later.
            tempData = newLine.split("\\s*,\\s*", -1);
            for (int ii = 0; ii < NUM_ELEMENTS; ii++) // Removes accidental whitespace around vals or to allow for file indentation.
            {
                tempData[ii].trim();
                tempData[ii].replace("\t", "");
            }
            tempOwner = null;
            newProperty = null;

            if (!tempData[2].equals(""))
            {
                // Loop to check if the owner already exists and to get the owner object.
                for (O p : propertyList)
                {
                    if (p instanceof Company)
                    {
                        if (((Property) p).getName().equals(tempData[2]))
                        {
                            tempOwner = (Company) ((Property) p);
                        }
                    }
                }
                if (tempOwner == null && !tempData[2].equals(""))
                {
                    throw new IOException("Error: Property has specified owner, however owner company does not yet exist.");
                }
                // End of owner-related checks.
            }

            // Check to confirm that the property does not already exist in the file.
            for (O p : propertyList)
            {
                if (p instanceof Company)
                {
                    if (((Property) p).getName().equals(tempData[0]))
                    {
                        throw new IOException("Error: Property already exists in the file.");
                    }
                }
                if (p instanceof BusinessUnit)
                {
                    if (((Property) p).getName().equals(tempData[0]))
                    {
                        throw new IOException("Error: Property already exists in the file.");
                    }
                }
            }

            if (tempData[1].equals("C"))
            {
                tempName = tempData[0];
                tempWorth = Double.parseDouble(tempData[3]);

                newProperty = new Company(tempName, tempWorth, tempOwner, 'C', new BankAccount());

            }
            else // Property is a BusinessUnit.
            {
                tempName = tempData[0];
                tempWorth = Double.parseDouble(tempData[3]);
                tempRevenue = Double.parseDouble(tempData[4]);
                tempWages = Double.parseDouble(tempData[5]);

                newProperty = new BusinessUnit(tempName, tempWorth, tempOwner, 'B', tempRevenue, tempWages);
            }

            propertyList.add((O) newProperty); // "unchecked" cast here.

            // Read the next line in the file.
            newLine = lineReader.readLine();
        }

        // Add each property to the owner object as appropriate.
        for (O p : propertyList)
        {
            if (p instanceof Company)
            {
                if (((Property) p).getOwner() != null) // If the company has an owner.
                {
                    ((Property) p).getOwner().addCompany((Company) (((Property) p)));
                }
            }

            if (p instanceof BusinessUnit)
            {
                if (!((Property) p).getName().equals("")) // If the business unit has an owner.
                {
                    for (O pp : propertyList) // Find the company for the relevant business unit.
                    {
                        if (pp instanceof Company) // If the property is a company.
                        {
                            if (pp.equals(((Property) p).getOwner())) // If the Company equals the owner's name.
                            {
                                ((Company) ((Property) pp)).addBusinessUnit((BusinessUnit) ((Property) p)); // Add the B.U to that company.
                            }
                        }

                    }
                }
            }
        }

        /* Remove non-primary properties from the list (properties with an owner).
         * Used iterator as arrayList does not support removal and iteration
         * in for-each structures. */
        Iterator<O> iter = propertyList.iterator();
        while (iter.hasNext())
        {
            O prop = iter.next();

            if (prop instanceof Company)
            {
                if (((Company) ((Property) prop)).getOwner() != null) // If the company has an owner.
                {
                    iter.remove();
                }
            }
            if (prop instanceof BusinessUnit)
            {
                if (((Property) prop).getOwner() != null) // If the business unit has an owner.
                {
                    iter.remove();
                }
            }
        }

        /* Move the primary company (first company in the file) to the first
         * position in the property list. The relative position of any businessUnits
         * before this point does not matter, as they have an unnamed owned. */
        if (!(propertyList.get(0) instanceof Company))
        {
            iter = propertyList.iterator(); // Reset the iterator used before.
            int index = 0;
            boolean found = false;
            while (iter.hasNext() && !found)
            {
                O prop = iter.next();
                if (prop instanceof Company)
                {
                    // Swap the businessunit at the 0 index with the index of the primary company.
                    propertyList.set(index, propertyList.set(0, propertyList.get(index)));
                    found = true;
                }
                else
                {
                    // Not yet found.
                    index++;
                }
            }
        }

        // Return the list of property objects for the property file.
        return propertyList;
    }

    /* Error check confirms that there is not a greater than expected number of
     * commas separating the data fields, and there are no blank fields where there
     * should otherwise be data, and there are no blank lines in the file.
     * Using arbitrary value of LINE_LIMIT characters as the line limit.
     * Most errors give some debugging/general information where applicable.
     */
    public void errorCheck(String lineToCheck) throws IOException
    {
        String[] tempData = lineToCheck.split("\\s*,\\s*", -1); // Removes any accidental whitespace and keeps blank strings.

        if (lineToCheck.equals(""))
        {
            throw new IOException("Error: A line cannot be blank.");
        }
        if (lineToCheck.length() > LINE_LIMIT)
        {
            throw new IOException("Error: Too many characters on a single line.");
        }
        if (tempData.length != NUM_ELEMENTS)
        {
            throw new IOException("Error: Unexpected number of elements in line: " + lineToCheck);
        }
        if (tempData[0].equals(""))
        {
            throw new IOException("Error: Property name cannot be blank.");
        }
        if (!tempData[1].equals("C") && !tempData[1].equals("B"))
        {
            throw new IOException("Error: Unknown type of property: " + tempData[1]);
        }
        if (tempData[3].equals(""))
        {
            throw new IOException("Error: Property cannot be worth a null value.");
        }
        if ((tempData[1].equals("C") && !tempData[4].equals(""))
                || (tempData[1].equals("C") && !tempData[5].equals(""))) // If the property is a company yet has a specified revenue or wages.
        {
            throw new IOException("Error: A Company cannot have a specified revenue or wages.");
        }
        if ((tempData[1].equals("B") && tempData[4].equals(""))
                || (tempData[1].equals("B") && tempData[5].equals(""))) // If a business unit, but no specified revenue and/or wages.
        {
            throw new IOException("Error: Business Unit: " + tempData[0] + " must have a specified revenue and/or wages.");
        }
        if (tempData[1].equals("B"))
        {
            try
            {
                Double.parseDouble(tempData[3]);
                Double.parseDouble(tempData[4]);
                Double.parseDouble(tempData[5]);
            }
            catch (NumberFormatException e)
            {
                throw new IOException("Error: Worth, Revenue or Wages for: " + tempData[1] + " does not represent a valid number.");
            }
        }
        if (tempData[1].equals("C"))
        {
            try
            {
                Double.parseDouble(tempData[3]);
            }
            catch (NumberFormatException e)
            {
                throw new IOException("Error: Worth for: " + tempData[1] + " does not represent a valid number.");
            }
        }
    }
}
