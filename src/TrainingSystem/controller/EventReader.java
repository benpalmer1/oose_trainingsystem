/*
 * FILE:        EventReader.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Reads file containing training system events.
 */

package TrainingSystem.controller;

import java.util.*;
import java.io.*;
import TrainingSystem.model.*;

public class EventReader<O> extends TrainingFileReader<O>
{
    public static final int NUM_ELEMENTS = 3;
    public static final String HEADER = "Year,Event,Property";

    /* 
     * parse - reads an Event file and passes it for information on the different types of events,
     * which are actioned later in the Simulation Object.
     */
    @Override
    @SuppressWarnings("unchecked") // Suppressed warning for "unchecked" cast between O (generic type) and Event.
    protected List<O> parse(BufferedReader lineReader, List<O> eventList) throws IOException
    {
        Event newEvent;
        String newLine = lineReader.readLine();
        int currentYear = 0, tempYear;
        String tempType, tempName;
        String[] tempData;
		
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
			throw new IOException("Error: Event file does not match specification.");
		}

        newLine = lineReader.readLine();
        while (newLine != null)
        {
            currentYear = errorCheck(newLine, currentYear); // If no exception thrown, everything semantically checks outs, business rules are confirmed later.
            tempData = newLine.split("\\s*,\\s*", -1);
            for (int ii = 0; ii < NUM_ELEMENTS; ii++) // Removes accidental whitespace around vals or to allow for file indentation.
            {
                tempData[ii].trim();
            }

            tempYear = Integer.parseInt(tempData[0]);
            tempType = tempData[1];
            tempName = tempData[2];

            newEvent = new Event(tempYear, tempType, tempName);
            eventList.add((O) newEvent);

            newLine = lineReader.readLine();
        }
        return eventList;
    }
    
    /*
     * errorCheck - Confirms that there are no syntactic errors with the EventReader file.
     */
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
            throw new IOException("Error: Unexpected number of elements in line: " + lineToCheck);
        }
        if (tempData[0].equals("") || tempData[1].equals(""))
        {
            throw new IOException("Error: A component of the Event file has been left blank.");
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
        if (!tempData[1].equals("R+") && !tempData[1].equals("W+") && !tempData[1].equals("V+")
                && !tempData[1].equals("R-") && !tempData[1].equals("W-") && !tempData[1].equals("V-"))
        {
            throw new IOException("Error: The entered event type is not valid.");
        }
        if (tempData[1].startsWith("W"))
        {
            if (!tempData[2].equals(""))
            {
                throw new IOException("Error: The entered event type cannot have a specified property.");
            }
        }
        if (tempData[1].startsWith("R") || tempData[1].startsWith("V"))
        {
            if (tempData[2].equals(""))
            {
                throw new IOException("Error: The entered event type must have a specified property.");
            }
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
