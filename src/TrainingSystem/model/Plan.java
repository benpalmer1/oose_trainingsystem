/*
 * FILE:        Plan.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Represents an plan within the training simulation, showing
 *              buying and sell decisions of the primary company.
 */
package TrainingSystem.model;

public class Plan
{
    private int year;
    private char type;
    private String property; // Represents the property which will be bought or sold.

    public Plan(int inYear, char inType, String inProperty)
    {
        year = inYear;
        type = inType;
        property = inProperty;
    }

    public int getYear()
    {
        return this.year;
    }

    public char getType()
    {
        return this.type;
    }

    public String getProperty()
    {
        return this.property;
    }
}
