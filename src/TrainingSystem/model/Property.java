/*
 * FILE:        Property.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Used for the two different types of property represented
 *              in the property file. Extended in BusinessUnit and Company.
 */

package TrainingSystem.model;

/* To make a polymorphic 'property', Property represents a generic parent class 
 * type, with an owner, name, value and type. The type is for use in case statements.
 * Owner information is known by any child class in order to be able to detect
 * operations which aren't allowed.
 */
public class Property
{
    private Company owner;
    private String name;
    private Double value;
    private char propertyType;

    public Property(String inName, double inValue, Company inOwner, char inPropType)
    {
        this.name = inName;
        this.value = inValue;
        this.owner = inOwner;
        this.propertyType = inPropType;
    }

    public void setName(String inName)
    {
        this.name = inName;
    }

    public void setValue(Double inValue)
    {
        this.value = inValue;
    }

    public void setType(char inPropertyType)
    {
        this.propertyType = inPropertyType;
    }

    public void setOwner(Company inCompany)
    {
        this.owner = inCompany;
    }

    public double getValue()
    {
        return this.value;
    }

    public String getName()
    {
        return this.name;
    }

    public Company getOwner()
    {
        return this.owner;
    }

    public char getType()
    {
        return this.propertyType;
    }

    /* Update the value of this property by the specified modifier. */
    public double updateValue(double inModifier)
    {
        this.value *= inModifier;
        return this.value;
    }
}
