/*
 * FILE:        CompanyReport
 * AUTHOR:      Benjamin Palmer
 * USERNAME:    17743075
 * UNIT:        COMP2003
 * PURPOSE:     Model/container class to hold the yearly result of each company.
 */

package TrainingSystem.model;

public class CompanyReport
{
    private int year;
    private String company;
    private double bankBalance;

    public CompanyReport(int inYear, String inCompany, double inBalance)
    {
        this.year = inYear;
        this.company = inCompany;
        this.bankBalance = inBalance;
    }

    public int getYear()
    {
        return this.year;
    }

    public String getCompany()
    {
        return this.company;
    }

    public double getBankBalance()
    {
        return this.bankBalance;
    }
}
