/*
 * FILE:        DisplayReport.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Displays every yearly report for each company in a formatted
 *              output. This is the only class which prints to System.out
 */

package TrainingSystem.view;

import java.util.*;
import TrainingSystem.model.*;

public class DisplayReport
{
    // Supports values of 100 (regular table) or 125(larger table).
    public static final int WIDTH = 100;
    public static final String ASSIGNMENT = "Object Oriented Software Engineering Assignment - COMP2003";
    public static final String CREATOR = "Benjamin Palmer - Curtin University - 17743075";

    /* displaySimulation - Displays a title block, then loops over every
     * report which has been passed to it then displays in a formatted output.
     */
    public void displaySimulation(List<CompanyReport> reports)
    {
        int currentYear = 0;
        
        if (reports.isEmpty())
        {
            throw new IllegalArgumentException("Error: No company reports passed to the display function.");
        }
        
        // Title block for the assignment and my details.
        System.out.println(pad("", 1));
        System.out.println(pad(ASSIGNMENT, 2));
        System.out.println(pad(CREATOR, 2));
        System.out.println(pad("", 1));
        System.out.println(pad("Year, Company Name, Bank Balance", 4));
        System.out.println(pad("", 1));
        currentYear = reports.get(0).getYear();

        for (CompanyReport c : reports) // Display the report for every companyReport.
        {
            String accountOutput = String.format("%.2f", c.getBankBalance());
            if (c.getYear() > currentYear)
            {
                System.out.println(pad("", 1));
                System.out.println(pad(c.getYear() + "," + c.getCompany() + "," + accountOutput, 5));
            }
            if (c.getYear() == currentYear)
            {
                if (c.equals(reports.get(0))) // If it's the first year in a block, print the year.
                {
                    System.out.println(pad(c.getYear() + "," + c.getCompany() + "," + accountOutput, 5));
                }
                else // Don't print the year if it isn't the first in that block.
                {
                    System.out.println(pad("," + c.getCompany() + "," + accountOutput, 5));
                }
            }
            currentYear = c.getYear();
        }
        
        //  End block.
        System.out.println(pad("", 1));
        System.out.println(pad("End of Simulation. Total Years: " + (reports.get(reports.size() - 1).getYear() - reports.get(0).getYear()), 2));
        System.out.println(pad("", 1));
    }

    /*
     * pad(instring,type) pads the desired string to create a SQL-like table
     * format.
     * inString represents the string to pad with dashes or spaces on either
     * side.
     * type represents if the string is part of a cell separator, tile, data
     * or contents.
     * type 4 & 5 strings are split based on a .csv format, into 3 columns.
     */
    public static String pad(String inString, int type)
    {
        String outString = "";
        String temp[] = inString.split(",");
        int sLength = inString.length();

        if (((inString.length()) % 2 != 0) && (type == 1 || type == 2)) // If an odd-length string, pad with an extra tab.
        {
            inString = "-" + inString;
            sLength = inString.length();
        }

        if (((inString.length()) % 2 != 0) && type == 3)
        {
            inString = " " + inString;
            sLength = inString.length();
        }

        if (type == 4 || type == 5)
        {
            temp = inString.split("\\s*,\\s*", -1);
            
            if (sLength > (WIDTH - 4)) // String is too long to format (-4 represents the side pipes for table formatting).
            {
                    throw new IllegalArgumentException("Error: Requested format string is longer than: " + (WIDTH-4) + " characters.");
            }

            if (temp.length != 3)
            {
                throw new IllegalArgumentException("Error: Too many arguments passed to pad(inString, type)");
            }
            if (temp[0].length() % 2 == 0)
            {
                temp[0] = " " + temp[0];
            }
            if (temp[1].length() % 2 == 0)
            {
                temp[1] = " " + temp[1];
            }
            if (temp[2].length() % 2 == 0)
            {
                temp[2] = " " + temp[2];
            }
        }

        if (sLength > (WIDTH - 2)) // String is too long to format (-2 represents the side pipes for table formatting, on non-column data).
        {
            throw new IllegalArgumentException("Error: Requested format string is longer than: " + (WIDTH-2) + " characters.");
        }

        switch (type)
        {
            case (1): //Cell separator                  -> "+----type 1----+"
                outString = (String.format("+%" + ((WIDTH - sLength) / 2 - 2) + "s", "").replace(' ', '-'));
                outString += inString;
                outString += (String.format("%-" + ((WIDTH - sLength) / 2 - 2) + "s+", "").replace(' ', '-'));
                break;
            case (2): //Cell title contents             -> "|----type 2----|"
                outString = (String.format(("|%" + ((WIDTH - sLength) / 2 - 2) + "s"), "").replace(' ', '-'));
                outString += inString;
                outString += (String.format(("%-" + ((WIDTH - sLength) / 2 - 2) + "s|"), "").replace(' ', '-'));
                break;
            case (3): //Cell titles (only one)          -> "|    type 3    |"
                outString = (String.format(("|%" + ((WIDTH - sLength) / 2 - 2) + "s"), ""));
                outString += inString;
                outString += (String.format(("%-" + ((WIDTH - sLength) / 2 - 2) + "s|"), ""));
                break;
            case (4): //Cell titles (spread over three) -> "|  4 | 4  | 4  |"
                for (String temp1 : temp)
                {
                    outString += (String.format("|%" + (((Math.round((WIDTH - 4) / 3) / 2) * 2 - temp1.length()) / 2) + "s", ""));
                    outString += temp1;
                    outString += (String.format("%" + (((Math.round((WIDTH - 4) / 3) / 2) * 2 - temp1.length()) / 2) + "s", ""));
                }
                outString += " |";
                break;
            case (5): //Cell data (spread over three)   -> "|  5   5   5  |"
                outString += "|";
                for (String temp1 : temp)
                {
                    outString += (String.format("%" + (((Math.round((WIDTH - 4) / 3) / 2) * 2 - temp1.length()) / 2) + "s", ""));
                    outString += temp1;
                    outString += (String.format("%" + (((Math.round((WIDTH - 4) / 3) / 2) * 2 - temp1.length()) / 2) + "s ", ""));
                }
                outString += "|";
                break;
        }
        return outString;
    }
}
