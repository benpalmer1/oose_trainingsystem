/*
 * FILE:        TrainingFileReader.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Abstract to the reader classes, implementing the template method pattern.
 *              TrainingFileReader's parse method is extended in property, event and plan readers.
 *              Uses generics to abstract the reader classes from the resulting type.
 */

package TrainingSystem.controller;
import java.util.*;
import java.io.*;

public abstract class TrainingFileReader<O>
{
    public static final int LINE_LIMIT = 100;
    
    // Template method pattern implementation, parse, implemented in propertyreader, eventreader and planreader.
    protected abstract List<O> parse(BufferedReader lineReader, List<O> propertyList) throws IOException;
    
    /* Reads a specific type of file, given by the fileName
     * Returns a list representation of all objects in that file.
     */
    public List<O> read(String fileName, List<O> objectList) throws IOException
    {
        FileReader reader = null;
        BufferedReader lineReader = null;

        try
        {  
            reader = new FileReader(fileName);
            lineReader = new BufferedReader(reader);
            objectList = parse(lineReader, objectList);
        }
		catch(NullPointerException e) // Rethrow as an IO exception with the appropriate message.
		{
			throw new IOException(e.getMessage());
		}
		catch(FileNotFoundException e)
		{
			throw new IOException(e.getMessage());
		}
        finally // So that the reader is definitely closed, even on exception.
        {
			if(reader != null)
			{            
				reader.close();
			}
        }
        return objectList;
    }
}
