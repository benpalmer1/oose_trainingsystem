/*
 * FILE:        Company.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Model class to hold information about a company.
 * COMMENTS:    Performs checks on added and removed properties, and throws exceptions on errors.
 */

package TrainingSystem.model;
import java.util.*;

public class Company extends Property
{
    private final BankAccount account;
    private final Map<String, Company> ownedCompanies;
    private final Map<String, BusinessUnit> ownedBusinessUnits;
    
    /* Company Constructor - defines a new company and imports its bank account object.
     * The inAccount object is injected so that it can be mocked for testing.
     */
    public Company(String inName, double inValue, Company inOwner, char inPropType, BankAccount inAccount)
    {
        super(inName, inValue, inOwner, inPropType);
        this.account = inAccount;
        this.ownedCompanies = new HashMap<>();
        this.ownedBusinessUnits = new HashMap<>();
    }
    
    public Map<String, Company> getOwnedCompanies()
    {
        return this.ownedCompanies;
    }
    
    public Map<String, BusinessUnit> getOwnedBusinessUnits()
    {
        return this.ownedBusinessUnits;
    }
    
    // Bank account related functions:
    public double getBankBalance()
    {
        return this.account.getBalance();
    }
    
    // Recursively updates the wages of every owned business unit.
    public double updateWages(double inModifier)
    {
        double wages = 0;
        
        for(BusinessUnit b : this.ownedBusinessUnits.values())
        {
            wages += b.updateWages(inModifier);
        }
        
        // And recurse for every owned company.
        for(Company c : this.ownedCompanies.values())
        {
            wages += c.updateWages(inModifier);
        }
        return wages;
    }
    
    public double updateBankBalance(double inValue)
    {
        // Adds or removes from the bank balance by the given inValue.
        this.account.updateBalance(inValue);
        // Also returns the new bank account balance.
        return this.account.getBalance();
    }
    
    public void setBankBalance(double inBalance)
    {
        this.account.setBalance(inBalance);
    }
    // End of bank account functions.
    
    // Including every sub-company, add all companies to a list.
    public List<Company> ownedCompanies()
    {
        List<Company> compList = new ArrayList<>();
        List<Company> tempList = new ArrayList<>();
        
        // Add everything this company owns to the list to return.
        compList.addAll(this.ownedCompanies.values());
        
        if(!this.ownedCompanies.isEmpty())
        {
            for(Company c : this.ownedCompanies.values())
            {
                tempList = c.ownedCompanies();
                if(!Collections.disjoint(compList, tempList)) // Test if a company is owned somewhere else.
                {
                    throw new IllegalArgumentException("Error: Detected company in more than one location.");
                }
                compList.addAll(tempList);
            }
        }  
        return compList;
    }
    
    /* ownsCompany - Recursively checks this company to see whether it owns a specific 
     * company and returns it if found. If not found, returns null. */
    public Company ownsCompany(String inName)
    {
        Company foundCompany = null;
        
        if(this.ownedCompanies.containsKey(inName))
        {
            foundCompany = this.ownedCompanies.get(inName);
        }
        
        if(foundCompany == null) // Company not yet found, recurse.
        {
            // Recurse
            for(Company c : this.ownedCompanies.values())
            {
                foundCompany = c.ownsCompany(inName);
                if(foundCompany != null) // Found the company, return.
                {
                    return foundCompany;
                }
            }
        }
        
        return foundCompany; //Will return null if the company couldn't be found
    }
    
    public BusinessUnit ownsBusinessUnit(String inName)
    {
        BusinessUnit foundBusiness = null;
        
        if(this.ownedBusinessUnits.containsKey(inName)) // If the business unit is found.
        {
            foundBusiness = this.ownedBusinessUnits.get(inName);
        }
        
        if(foundBusiness == null)
        {
            // Recurse.
            for(Company c : this.ownedCompanies.values())
            {
                foundBusiness = c.ownsBusinessUnit(inName);
                if(foundBusiness != null) // Found the business unit, return.
                {
                    return foundBusiness;
                }
            }
        }

        return foundBusiness; // Will return null if the company couldn't be found.
    }
    
    // Compositely iterate over every owned Company and BusinessUnit to calculate profit.
    public double profitCalc()
    {
        double profit = 0.0;
        
        // Calculate the interest earned over the previous year.
        profit += this.account.profitCalc();
        
        // Determine the profit of every owned business unit.
        for(BusinessUnit b : this.ownedBusinessUnits.values())
        {
            profit += b.profitCalc();
        }
        
        // Recursively calculate, for every owned company.
        for(Company c : this.ownedCompanies.values())
        {
            profit += c.profitCalc();
        }
        
        if(profit<=0) //If the company made a loss
        {
            this.account.updateBalance(profit);
        }
        else // If the company made a profit.
        {
            this.account.updateBalance(0.5*profit);
            profit *= 0.5; // As specified, half the profit for the "owner".
        }
        return profit;
    }
    
    // Recursively updates the revenue of every owned business unit.
    public double updateAllRevenue(double inModifier)
    {
        double revenue = 0;
        
        for(BusinessUnit b : this.ownedBusinessUnits.values())
        {
            revenue += b.updateRevenue(inModifier);
        }
        for(Company c : this.ownedCompanies.values())
        {
            revenue += c.updateAllRevenue(inModifier);
        }
        return revenue;
    }
    
    public void addCompany(Company inCompany)
    {
        Company tempOwnerCompany;
        
        /* Two checks to confirm the company is not already owned by the purchaser
         * One to confirm the entire company, and one to confirm the name also. */
        if(this.ownedCompanies.containsKey(inCompany.getName()))
        {
            throw new IllegalArgumentException("Error: " + inCompany.getName() + " is already owned by this company.");
        }
        if(this.ownedCompanies.containsValue(inCompany))
        {
            throw new IllegalArgumentException("Error: " + inCompany.getName() + " is already owned by this company.");
        }
        
        /* Checks to confirm that there is not a cycle in company ownership.
         * i.e The company cannot try to own itself, or by forming an ownership cycle
         * where recursively checking owners will reveal this Company is trying to be
         * bought by a company already owned by it, and not a direct relationship. */
        tempOwnerCompany = this.getOwner();
        while(tempOwnerCompany != null)
        {
            if(tempOwnerCompany.getName().equals(this.getName()))
            {
                throw new IllegalStateException("Error: Cycle in company ownership. Company attempting to be owned by itself.");
            }
            // Continue checking up the map of owners.
            tempOwnerCompany = tempOwnerCompany.getOwner();
        }
        
        // This company is therefore valid, add it to the map of owned companies.
        this.ownedCompanies.put(inCompany.getName(), inCompany);
    }
    
    public void addBusinessUnit(BusinessUnit inBusiness)
    {
        /* Two checks to confirm the business unit is not already owned by the purchaser
         * One to confirm the entire business unit, and one to confirm the name also. */
        if(this.ownedBusinessUnits.containsKey(inBusiness.getName()))
        {
            throw new IllegalArgumentException("Error: " + inBusiness.getName() + " is already owned by this company.");
        }
        if(this.ownedBusinessUnits.containsValue(inBusiness))
        {
            throw new IllegalArgumentException("Error: " + inBusiness.getName() + " is already owned by this company.");
        }
    
        // Add to the list of owned business units.
        this.ownedBusinessUnits.put(inBusiness.getName(), inBusiness);
    }
    
    public Company removeCompany(String inName)
    {
        Company returnCompany = this.ownedCompanies.remove(inName);

        if(returnCompany == null)
        {
            throw new IllegalArgumentException("Error: " + inName + " is not owned by this company.");
        }
        else // Company was found, removed from the map and returned.
        {
            return returnCompany;
        }
    }
    
    public Company removeCompany(Company inCompany)
    {
        Company returnCompany = this.ownedCompanies.remove(inCompany.getName());
        /* To make sure a company is removed, even if a value is changed,
         * the name is used to search for it and not the entire object. */

        if(returnCompany == null)
        {
            throw new IllegalArgumentException("Error: " + inCompany.getName() + " is not owned by this company.");
        }
        else // Company was found, removed from the map and returned.
        {
            return returnCompany;
        }
    }
    
    public BusinessUnit removeBusinessUnit(String inName)
    {
        BusinessUnit returnBusinessUnit = this.ownedBusinessUnits.remove(inName);
        
        if(returnBusinessUnit == null)
        {
            throw new IllegalArgumentException("Error: " + inName + " is not owned by this company.");
        }
        else // Business unit was found, removed from the map and returned.
        {
            return returnBusinessUnit;
        }
    }
    
    public BusinessUnit removeBusinessUnit(BusinessUnit inBusinessUnit)
    {
        BusinessUnit returnBusinessUnit = this.ownedBusinessUnits.remove(inBusinessUnit.getName());
        
        /* To make sure a business unit is removed, even if a value is changed,
         * the name is used to search for it and not the entire object.*/
        
        if(returnBusinessUnit == null)
        {
            throw new IllegalArgumentException("Error: " + inBusinessUnit.getName() + " is not owned by this company.");
            
        }
        else // Business unit was found, removed from the map and returned.
        {
            return returnBusinessUnit;
        }
    }
}
