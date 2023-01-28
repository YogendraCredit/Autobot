package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions
        (
                features = "/src/test/java/features/PCL.feature",
                glue = "stepDefinitions",                                                                                                                          //Name of Step Definition Package
                dryRun = false,                                                                                                                                   // Check the mapping between feature file and Step Definition
                monochrome = true,                                                                                                                           //  For readable format output	plugin = {"pretty","html:Reports/Report.html","json:Reports/Report.json","junit:Reports/Report.xml"},                                                              //type:folder/name.type
                plugin = {"pretty","html:Reports/Report.html"}
        )


public class SmeRun extends AbstractTestNGCucumberTests {

}
