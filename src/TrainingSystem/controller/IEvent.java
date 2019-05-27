/*
 * FILE:        IEvent.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     An interface to provide a polymorphic simulation event.
 */
package TrainingSystem.controller;

import java.util.*;
import TrainingSystem.model.*;

public interface IEvent
{
    public void change(List<Property> propList, String propName, double modifier);
}