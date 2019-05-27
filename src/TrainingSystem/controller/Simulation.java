/*
 * FILE:        Simulation.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     This file controls the entire simulation. When finished, will return a
 *              list of all companies and their bank balances for every year (list of CompanyReports).
 *              Handles events, and updates companies and business units as appropriate.
 *              No file IO or console output is performed by the simulation class.
 */

package TrainingSystem.controller;

import java.util.*;
import TrainingSystem.model.*;

public class Simulation
{

    private final List<Property> propertyList;
    private final List<Event> events;
    private final List<Plan> plans;
    private final List<CompanyReport> companyReports;
    private final int startYear;
    private final int endYear;
    private int currentYear;

    public Simulation(List<Property> inProperties, List<Event> inEvents,
                      List<Plan> inPlans, List<CompanyReport> inCompanyReports,
                      int inStartYear, int inEndYear)
    {
        this.propertyList = inProperties;
        this.events = inEvents;
        this.plans = inPlans;
        this.companyReports = inCompanyReports;
        this.startYear = inStartYear;
        this.endYear = inEndYear;
        this.currentYear = inStartYear;
    }

	//Uncomment for testing. This code otherwise violates data-hiding principles.
	/*
	public List<Property> getProperties()
	{
		return this.propertyList;
	}
	
	public List<Event> getEvents()
	{
		return this.events;
	}
	
	public List<Plan> getPlans()
	{
		return this.plans;
	}
	*/

    // Runs the simulation in the correct sequence.
    public List<CompanyReport> runSimulation()
    {
        /* Followed a slightly different path, have created a 'companyReport'
         * class and list, which enables display of all company data at the
         * same time, at the end of the simulation. If interrupted halfway
         * through, by an exception, will output company data until that
         * point.
         */
        yearlyUpdate();

        return Collections.unmodifiableList(this.companyReports);
    }

    public void yearlyUpdate()
    {
        List<Company> allCompanies = new ArrayList<>();
        double modifier = 0;
        
        // Loop for every year of the simulation.
        for (; this.currentYear <= this.endYear; this.currentYear++)
        {
            allCompanies.clear(); // Resets the allCompanies list, required if program is to allow new properties in the future.
            for (Property prop : this.propertyList)
            {
                if (prop instanceof Company)
                {
                    if (this.currentYear != this.startYear)
                    {
                        // Update the bank balances of all companies.
                        ((Company) prop).profitCalc();
                    }

                    // Add each primary company to the list.
                    allCompanies.add((Company) prop);
                    // Add all companies in the primary company to the list.
                    allCompanies.addAll(((Company) prop).ownedCompanies());
                }
            }

            // Create a company report for every company.
            for (Company c : allCompanies)
            {
                this.companyReports.add(new CompanyReport(this.currentYear,
                        c.getName(),
                        c.getBankBalance()));
            }

            // Action every event in the simulation, for that year.
            for (Event ev : this.events)
            {
                if (ev.getYear() == currentYear)
                {
                    if (ev.getEvent().endsWith("+"))
                    {
                        modifier = 1.05;
                    }
                    else if (ev.getEvent().endsWith("-"))
                    {
                        modifier = 0.95;
                    }
                    IEvent newEvent = eventFactory(ev);
                    newEvent.change(this.propertyList, ev.getProperty(), modifier);
                }
            }

            // Action every buy/sell decision, for that year.
            for (Plan pl : this.plans)
            {
                if (pl.getYear() == currentYear)
                {
                    switch (pl.getType())
                    {
                        case ('S'):
                            sellProperty(pl.getProperty());
                            break;
                        case ('B'):
                            buyProperty(pl.getProperty());
                            break;
                    }
                }
            }
        }
    }
	
    // eventFactory - Factory method to create a specific type of event, for the yearlyUpdate method to use.
    public IEvent eventFactory(Event inEvent)
    {
        IEvent tempEvent = null;
        if (inEvent.getEvent().startsWith("R")) // A revenue event
        {
            tempEvent = new RevenueEvent();
        }
        if (inEvent.getEvent().startsWith("V")) // A value event 
        {
            tempEvent = new ValueEvent();
        }
        if (inEvent.getEvent().startsWith("W")) // A wages event
        {
            tempEvent = new WagesEvent();
        }

        return tempEvent;
    }

    /* buyProperty - Transfers a property between the company owning inPropName
     * and the primary company.
     * Assumes that the simulation allows a purchase of a sub-property of the
     * primary company. */
    public void buyProperty(String inPropName)
    {
        Property tempProp = null;

        if (((Company) this.propertyList.get(0)).getOwnedBusinessUnits().containsKey(inPropName)) // Already owns, throw exception.
        {
            throw new IllegalArgumentException("Error: This property is already owned by the primary company: \""
                    + this.propertyList.get(0).getName() + "\"");
        }
        if (((Company) this.propertyList.get(0)).getOwnedCompanies().containsKey(inPropName)) // Already owns, throw exception.
        {
            throw new IllegalArgumentException("Error: This property is already owned by the primary company: \""
                    + this.propertyList.get(0).getName() + "\"");
        }
        if ((this.propertyList.get(0)).getName().equals(inPropName)) // Cannot buy itself.
        {
            throw new IllegalArgumentException("Error: The primary company cannot buy itself.");
        }

        Company tempOwner = this.propertyList.get(0).getOwner();
        /* Check for potential ownership cycle.
         * Applicable if program format were to change, so that the primary
         * company could be transferred. */
        while (tempOwner != null)
        {
            if (tempOwner.getName().equals(inPropName))
            {
                throw new IllegalArgumentException("Error: The company cannot purchase a company which indirectly owns this company.\n"
                        + "Company cycle not allowed.");
            }
            tempOwner = tempOwner.getOwner();
        }

        /* Find and purchase the property.
         * Checking if the property exists more than once is not required,
         * as this check is performed when the files are loaded and when added
         * to any list or map. */
        for (Property prop : this.propertyList)
        {
            if (tempProp == null) // If it hasn't yet been found.
            {
                if (prop.getName().equals(inPropName))
                {
                    tempProp = prop;
                }
                else if (prop instanceof Company)
                {
                    tempProp = ((Company) prop).ownsCompany(inPropName);
                    if (tempProp == null) // Look for a business unit.
                    {
                        tempProp = ((Company) prop).ownsBusinessUnit(inPropName);
                    }
                }
            }
        }

        if (tempProp == null)
        {
            throw new IllegalArgumentException("Error: The property: \"" + inPropName + "\" doesn't exist.");
        }
        else
        {
            switch (tempProp.getType())
            {
                case ('C'):
                    if (tempProp.getOwner() != null) // Not a primary property.
                    {
                        tempProp.getOwner().updateBankBalance(tempProp.getValue());
                        tempProp.getOwner().removeCompany((Company) tempProp);
                    }
                    else // Is a primary property.
                    {
                        this.propertyList.remove(tempProp);
                    }

                    // Update the primary company.
                    ((Company) this.propertyList.get(0)).addCompany((Company) tempProp);
                    ((Company) this.propertyList.get(0)).updateBankBalance(-(tempProp.getValue()));
                    break;
                case ('B'):
                    if (tempProp.getOwner() != null) // Not a primary property.
                    {
                        tempProp.getOwner().updateBankBalance(tempProp.getValue());
                        tempProp.getOwner().removeBusinessUnit((BusinessUnit) tempProp);
                    }
                    else // Is a primary property.
                    {
                        this.propertyList.remove(tempProp);
                    }

                    // Update the primary company.
                    ((Company) this.propertyList.get(0)).addBusinessUnit((BusinessUnit) tempProp);
                    ((Company) this.propertyList.get(0)).updateBankBalance(-(tempProp.getValue()));
                    break;
            }
        }
    }

    /* sellProperty - Sells a property of the primary company.
     * Removes from the primary company and adds to the property list.
     * Adds the value of the sold property to the primary company. */
    public void sellProperty(String inPropName)
    {
        Property tempProp = null;

        if ((this.propertyList.get(0)).getName().equals(inPropName))
        {
            throw new IllegalArgumentException("Error: The primary company cannot sell itself.");
        }

        if (!((Company) this.propertyList.get(0)).getOwnedBusinessUnits().containsKey(inPropName))
        {
            if (!((Company) this.propertyList.get(0)).getOwnedCompanies().containsKey(inPropName))
            {
                throw new IllegalArgumentException("Error: The property: \"" + inPropName + "\" is not already owned by the primary company: \""
                        + this.propertyList.get(0).getName() + "\""); // Doesn't own the property, throw exception.
            }
            else
            {
                tempProp = ((Company) this.propertyList.get(0)).getOwnedCompanies().remove(inPropName);
            }
        }
        else
        {
            tempProp = ((Company) this.propertyList.get(0)).getOwnedBusinessUnits().remove(inPropName);
        }

        // Increase the value of the primary property's bank balance.
        ((Company) this.propertyList.get(0)).updateBankBalance(tempProp.getValue());

        tempProp.setOwner(null);

        // Add to the list of unnamed owner properties.
        this.propertyList.add(tempProp);
    }
}
