Object Oriented Software Engineering Training System Assignment
================================

Assignment Files
----------------
	-  /TrainingSystem/src/TrainingSystem/view:
		TrainingSystem.java, DisplayReport.java

	-  /TrainingSystem/src/TrainingSystem/model:
		Property.java, Plan.java, Event.java, CompanyReport.java,
		Company.java, BusinessUnit.java, BankAccount.java

	-  /TrainingSystem/src/TrainingSystem/controller:
		WagesEvent.java, ValueEvent.java, TrainingFileReader.java, 
		Simulation.java, RevenueEvent.java, PropertyReader.java, 
		PlanReader.java, IEvent.java, EventReader.java

Compile
-------
	Run "ant" or "ant clean-build" (default) (or “ant clean” to remove build files)
	-  From the directory containing build.xml, dist, resources and src.
	-  See ‘build.xml’ for other options

Run
---
	$ java -jar TrainingSystem.jar propertyFile eventFile planFile startYear endYear
	-  From the /dist/ directory.
	-  Included are some test files, which produces the “TestOutput.png” when run between 2010 and 2020.
	-  File formats of all files are checked before the program executes.
	-  "propertyFileName eventFileName planFileName" files should be placed in either "/TrainingSystem/resources/" before compilation or in "/dist/" after compilation.
	-  There is a restriction of a 100 year timespan between the start and end year.


Example Output
---------------

	$ Bens-MBP:dist benpalmer$ java -jar TrainingSystem.jar prop.txt event.txt plan.txt 2005 2020

    +------------------------------------------------------------------------------------------------+
    |-------------------Object Oriented Software Engineering Assignment - COMP2003-------------------|
    |-------------------------Benjamin Palmer - Curtin University - 17743075-------------------------|
    +------------------------------------------------------------------------------------------------+
    |              Year             |          Company Name         |          Bank Balance          |
    +------------------------------------------------------------------------------------------------+
    |              2005                        AmazingCorp                         0.00              |
    |                                           Giblet Inc                         0.00              |
    |                                         Tumbleweed Co                        0.00              |
    |                                        SunnyDays Pools                       0.00              |
    +------------------------------------------------------------------------------------------------+
    |              2006                        AmazingCorp                      137500.00            |
    |                                           Giblet Inc                      250000.00            |
    |                                         Tumbleweed Co                      25000.00            |
    |                                        SunnyDays Pools                     1000.00             |
    +------------------------------------------------------------------------------------------------+
    |              2007                        AmazingCorp                      281875.00            |
    |                                           Giblet Inc                      506250.00            |
    |                                         Tumbleweed Co                      50625.00            |
    |                                        SunnyDays Pools                     2025.00             |
    +------------------------------------------------------------------------------------------------+
    |              2008                        AmazingCorp                      433382.81            |
    |                                           Giblet Inc                      768906.25            |
    |                                         Tumbleweed Co                      76890.63            |
    |                                        SunnyDays Pools                     3075.63             |
    +------------------------------------------------------------------------------------------------+
    |              2009                        AmazingCorp                      592289.84            |
    |                                           Giblet Inc                      1038128.91           |
    |                                         Tumbleweed Co                     103812.89            |
    |                                        SunnyDays Pools                     4152.52             |
    +------------------------------------------------------------------------------------------------+
    |              2010                        AmazingCorp                      758871.36            |
    |                                           Giblet Inc                      1314082.13           |
    |                                         Tumbleweed Co                     131408.21            |
    |                                        SunnyDays Pools                     5256.33             |
    +------------------------------------------------------------------------------------------------+
    |              2011                        AmazingCorp                      933411.78            |
    |                                           Giblet Inc                      1596934.18           |
    |                                         Tumbleweed Co                     159693.42            |
    |                                        SunnyDays Pools                     6387.74             |
    +------------------------------------------------------------------------------------------------+
    |              2012                        AmazingCorp                      604704.92            |
    |                                           Giblet Inc                      1886857.54           |
    |                                         Tumbleweed Co                     188685.75            |
    |                                        SunnyDays Pools                    519047.43            |
    +------------------------------------------------------------------------------------------------+
    |              2013                        AmazingCorp                      8323181.11           |
    |                                         Tumbleweed Co                     218402.90            |
    |                                        SunnyDays Pools                    532023.62            |
    |                                           Giblet Inc                      2184028.98           |
    +------------------------------------------------------------------------------------------------+
    |              2014                        AmazingCorp                      8135315.67           |
    |                                         Tumbleweed Co                     251362.97            |
    |                                        SunnyDays Pools                    545324.21            |
    |                                           Giblet Inc                      2496129.70           |
    +------------------------------------------------------------------------------------------------+
    |              2015                        AmazingCorp                      8823148.57           |
    |                                        SunnyDays Pools                    558957.31            |
    |                                           Giblet Inc                      2816032.94           |
    |                                         Tumbleweed Co                     285147.04            |
    +------------------------------------------------------------------------------------------------+
    |              2016                        AmazingCorp                     -3009818.97           |
    |                                        SunnyDays Pools                    572931.24            |
    |                                           Giblet Inc                     15186433.77           |
    |                                         Tumbleweed Co                     317400.72            |
    +------------------------------------------------------------------------------------------------+
    |              2017                        AmazingCorp                     -2327120.69           |
    |                                        SunnyDays Pools                    587254.53            |
    |                                           Giblet Inc                     15566094.61           |
    |                                         Tumbleweed Co                     350460.74            |
    +------------------------------------------------------------------------------------------------+
    |              2018                        AmazingCorp                      4812450.98           |
    |                                        SunnyDays Pools                    601935.89            |
    |                                           Giblet Inc                     15955246.97           |
    |                                         Tumbleweed Co                     386841.01            |
    +------------------------------------------------------------------------------------------------+
    |              2019                        AmazingCorp                      3980582.58           |
    |                                         Tumbleweed Co                     426499.84            |
    |                                        SunnyDays Pools                    616984.29            |
    |                                           Giblet Inc                     16354128.15           |
    +------------------------------------------------------------------------------------------------+
    |              2020                        AmazingCorp                     -3361522.78           |
    |                                           Giblet Inc                     16762981.35           |
    |                                         Tumbleweed Co                     462536.40            |
    |                                        SunnyDays Pools                    632408.89            |
    +------------------------------------------------------------------------------------------------+
    |-------------------------------End of Simulation. Total Years: 15-------------------------------|
    +------------------------------------------------------------------------------------------------+

Author
------
Benjamin Palmer, Student 17743075.
Repository made public in 2019.