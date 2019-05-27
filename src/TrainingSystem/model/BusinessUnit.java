/*
 * FILE:        BusinessUnit.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Represents a business unit within the training simulation.
 *              Contains all methods applicable to all (possible future) versions
 *              of the software, however some remain unused.
 *              BusinessUnit extends (is a child of) Property.
 */

package TrainingSystem.model;

public class BusinessUnit extends Property
{

    private double revenue;
    private double wages;

    public BusinessUnit(String inName,
                        double inValue,
                        Company inOwner,
                        char inPropType,
                        double inRevenue,
                        double inWages)
    {
        // Initialise the superclass.
        super(inName, inValue, inOwner, inPropType);
        this.revenue = inRevenue;
        this.wages = inWages;
    }

    public double getRevenue()
    {
        return this.revenue;
    }

    public double getWages()
    {
        return this.wages;
    }

    public void setRevenue(double inRevenue)
    {
        this.revenue = inRevenue;
    }

    public void setWages(double inWage)
    {
        this.wages = inWage;
    }

    /* updateRevenue - Updates the revenue by a given modifier. */
    public double updateRevenue(double inValue)
    {
        this.revenue *= inValue;
        return this.revenue;
    }

    /* updateWages - Updates the wages by a given modifier. */
    public double updateWages(double inValue)
    {
        this.wages *= inValue;
        return this.wages;
    }

    /* profitCalc - calculates the profit from the BusinessUnit revenue and wage costs. */
    public double profitCalc()
    {
        // If negative, represents a business unit that is making a loss.
        return (this.revenue - this.wages);
    }
}
