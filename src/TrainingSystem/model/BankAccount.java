/*
 * FILE:        BankAccount.java
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Provides a model class to represent and modify a bank account
 *              for the training simulation. As this Class is injected into the
 *              Company object, I decided not to implement it as a private class,
 *              even though it has no intrinsic use elsewhere.
 */

package TrainingSystem.model;

public class BankAccount
{
    private double balance;
    
    public BankAccount()
    {
        balance = 0.0;
    }
    
    public void setBalance(double inBalance)
    {
        this.balance = inBalance;
    }
    
    public double getBalance()
    {
        return this.balance;
    }
    
    public double updateBalance(double inBalance)
    {
        this.balance += inBalance;
        return this.balance;
    }
    
    /* 
     * Determine what 5% of the current balance is, representing profit if positive,
     * and a Company which is in debt, if negative.
     */
    public double profitCalc()
    {
        return this.balance * 0.05;
    }
}
