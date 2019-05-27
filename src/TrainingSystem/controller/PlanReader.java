/*
 * FILE:        PlanReader.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Reads files containing the different types of plans.
 */

package TrainingSystem.controller;

import TrainingSystem.model.*;
import java.util.*;
import java.io.*;

public class PlanReader<O> extends TrainingFileReader<O>
{
    public static final int NUM_ELEMENTS = 3;
    public static final String HEADER = "Year,Buy/Sell,Property";

    /*
     * parse - Parses the information inside the Plan file.
     * Constructs a Plan list based on the plans specified.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected List<O> parse(BufferedReader lineReader, List<O> planList) throws IOException
    {
        String newLine = lineReader.readLine();
        int currentYear = 0, tempYear;
        char tempType;
        String tempName, tempData[];
		
		if(newLine != null)
		{
	        newLine.replaceAll("\\s", ""); // Remove any whitespace from the header line.
	        if (!newLine.equals(HEADER))
	        {
	            throw new IOException("Error: Header file does not match specification: " + HEADER);
	        }
		}
		else
		{
			throw new IOException("Error: Plan file does not match specification.");			
		}
		
        newLine = lineReader.readLine();
        while (newLine != null)
        {
            currentYear = errorCheck(newLine, currentYear); // If no exception thrown, everything semantically checks outs, business rules are confirmed later.
            tempData = newLine.split("\\s*,\\s*", -1);
            for (int ii = 0; ii < NUM_ELEMENTS; ii++) // Removes accidental whitespace around vals or to  allow for file indentation.
            {
                tempData[ii].trim();
            }

            tempYear = Integer.parseInt(tempData[0]);
            tempType = tempData[1].charAt(0);
            tempName = tempData[2];

            Plan newPlan = new Plan(tempYear, tempType, tempName);
            planList.add((O) newPlan);

            newLine = lineReader.readLine();
        }
        return planList;
    }

    public int errorCheck(String lineToCheck, int currentYear) throws IOException
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
            throw new IOException("Error: Unexpected number of elements in line" + lineToCheck);
        }
        if (tempData[0].equals("") || tempData[1].equals("") || tempData[2].equals(""))
        {
            throw new IOException("Error: A component of the Plan file has been left blank.");
        }
        try
        {
            Integer.parseInt(tempData[0]);
        }
        catch (NumberFormatException e)
        {
            throw new IOException("Error: The entered year is not a valid number.");
        }
        if (Integer.parseInt(tempData[0]) < 0)
        {
            throw new IOException("Error: The entered year cannot be negative: " + Integer.parseInt(tempData[0]));
        }
        if (!tempData[1].equals("S") && !tempData[1].equals("B"))
        {
            throw new IOException("Error: The entered transaction type is not valid.");
        }

        // Checks to confirm and update chronological ordering.
        if (Integer.parseInt(tempData[0]) > currentYear)
        {
            currentYear = Integer.parseInt(tempData[0]);
        }
        if (Integer.parseInt(tempData[0]) < currentYear)
        {
            throw new IOException("Error: The file is not in chronological order.");
        }
        return currentYear;
    }
}
