/*
 * FILE:        TrainingSystem.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Contains the main() method to run the TrainingSystem based on CL parameters
 *              in the format: "propertyFileName eventFileName planFileName startYear endYear".
 *              TrainingSimulation initiliases most dependencies, performs error checking
 *              on the C/L parameters, and passes the final CompanyReport list to the DisplayReport object.
 *              For testing and optional display, TrainingSystem has a value for PRINT_ERROR,
 *              which if "true" will print the company report list to the command line, even if an
 *              exception condition is experienced. Usually set to false.
 */

package TrainingSystem.view;

import java.util.*;
import TrainingSystem.model.*;
import TrainingSystem.controller.*;
import java.io.*;

public class TrainingSystem
{
    // Defines the maximum length of the simulation.
    public static final int MAX_SIM_LENGTH = 100;
    // Defines the maximum length of a command line statement.
    public static final int MAX_CL_LENGTH = 100;
    // Prints list even on exception. Usage explained at top of the file.
    public static final boolean PRINT_ERROR = false;

    public static void main(String[] args)
    {
        // Variable Declarations (To store command line arguments).
        int startYear, endYear;
        String propFile, eventFile, planFile;

        // Most objects below are injected to allow for better testability.
        // Reader object declarations.
        PropertyReader<Property> propertyReader = new PropertyReader<>();
        PlanReader<Plan> planReader = new PlanReader<>();
        EventReader<Event> eventReader = new EventReader<>();

        // Various list declarations.
        List<Property> properties = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        List<Plan> plans = new ArrayList<>();
        List<CompanyReport> companyReports = new ArrayList<>();

        // Main functionality declarations.
        Simulation simulation;
        DisplayReport display = new DisplayReport();

        try
        {
            // Check command line arguments are valid.
            checkError(args);

            // Parse command line arguments.
            propFile = args[0];
            eventFile = args[1];
            planFile = args[2];
            startYear = Integer.parseInt(args[3]);
            endYear = Integer.parseInt(args[4]);

            // Read files.
            try
            {
                properties = propertyReader.read(propFile, properties);
                events = eventReader.read(eventFile, events);
                plans = planReader.read(planFile, plans);

                // Initialise the simulation.
                simulation = new Simulation(properties, events, plans,
                                            companyReports, startYear, endYear);
                
                // Run the simulation and retrieve the company reports.
                companyReports = simulation.runSimulation();

                // Display the results of the simulation.
                display.displaySimulation(companyReports);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        catch (IllegalArgumentException | IllegalStateException e)
        {
            System.err.println(e.getMessage());
            if(!companyReports.isEmpty() && PRINT_ERROR)
            {
                System.err.println("ERROR: Simulation reached an exception condition. Results below may not be valid. ERROR");
                System.err.flush(); // Ensures the messages are printed correctly.
                display.displaySimulation(companyReports);
                System.out.flush();
                System.err.println("ERROR: Simulation reached an exception condition. Results above may not be valid. ERROR");
                System.err.flush();
            }
        }
    }

    /* checkError - Error checking method for the TrainingSystem simulation.
     * Checks if command line arguments are valid.
     */
    private static void checkError(String[] inArgs)
    {
        if (inArgs.length != 5)
        {
            throw new IllegalArgumentException("Error: Incorrect number of command line arguments."
                    + "\nRequired format:\n"
                    + "propertyFileName eventFileName planFileName startYear endYear");
        }
        if ((inArgs[0].length() + inArgs[1].length()
                + inArgs[2].length() + inArgs[3].length()
                + inArgs[4].length()) > MAX_CL_LENGTH) //If too many characters are entered.
        {
            throw new IllegalArgumentException("Error: Entered command line arguments exceeds maximum allowable amount.");
        }
        try
        {
            Integer.parseInt(inArgs[3]);
            Integer.parseInt(inArgs[4]);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Error: startYear and endYear must be valid dates (integers).");
        }
        if (Integer.parseInt(inArgs[3]) > Integer.parseInt(inArgs[4]))
        {
            throw new IllegalArgumentException("Error: startYear and endYear must be entered in the correct order.");

        }
        if (Integer.parseInt(inArgs[3]) == Integer.parseInt(inArgs[4]))
        {
            throw new IllegalArgumentException("Error: The simulation must run for at least one year (The start and end years are equal).");

        }
        if ((Integer.parseInt(inArgs[4]) - Integer.parseInt(inArgs[3])) > MAX_SIM_LENGTH)
        {
            throw new IllegalArgumentException("Error: Simulation length is greater than the maximum allowable amount.");

        }
    }
}
