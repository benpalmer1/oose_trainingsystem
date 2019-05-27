/*
 * FILE:        Event.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Represents an event within the training simulation.
 */

package TrainingSystem.model;

public class Event
{
    private int year;
    private String event;
    private String property;
    
    public Event(int inYear, String inEvent, String inProperty)
    {
        this.year = inYear;
        this.event = inEvent;
        this.property = inProperty;
    }
    
    public int getYear()
    {
        return this.year;
    }
    
    public String getEvent()
    {
        return this.event;
    }
    
    public String getProperty()
    {
        return this.property;
    }
}