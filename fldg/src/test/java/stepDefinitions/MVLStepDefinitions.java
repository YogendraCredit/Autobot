package stepDefinitions;

import base.BaseClass;
import constants.APIEndPoints;
import helpers.HelperMethods;
import helpers.ReadDataFromJson;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static helpers.HelperMethods.requestSpecifications;
import static io.restassured.RestAssured.given;

public class MVLStepDefinitions extends BaseClass {

    @Given("partner sends MVL Appform")
    public void partnerSendsMVLAppform() throws Exception {
        String mvlAppform = ReadDataFromJson.updatePartnerLoanId("src/test/resources/mvlAppForm.json");
        partnerLoanId = ReadDataFromJson.readPartnerLoanId(mvlAppform);

        JsonPath postMvlAppFormResponse = HelperMethods.doPost("plexUri", mvlAppform, APIEndPoints.MVL_START_PROCESS_API);
        String status = postMvlAppFormResponse.get("status");
        Assert.assertEquals("i",status);
        Thread.sleep(25000);
    }


    @When("KSF starts and ends Green channel workflow")
    public void ksfEndsGreenChannelWorkflow() throws Exception {

        JsonPath historyResponse = HelperMethods.doGet("plexUri", partnerLoanId, APIEndPoints.PLEX_WORKFLOW_HISTORY_API);
        int taskListSize = HelperMethods.countTaskList(historyResponse);

        Assert.assertEquals(13, taskListSize);
        Thread.sleep(30000);
    }


    @Then("appform status should be {string}")
    public void checkAppformStatus(String expectedStatus) throws Exception {

        JsonPath appFormResponse = HelperMethods.doGet("shieldUri" ,partnerLoanId,APIEndPoints.SHIELD_GET_APPFORM_API);
        String appFormStatus = appFormResponse.get("appFormStatus");
        System.out.print("actualStatus : " + appFormStatus);
        Assert.assertEquals(expectedStatus,appFormStatus);

    }
    @Then("We process disbursal from Razor-pay")
    public void we_process_disbursal_from_razor_pay() throws InterruptedException {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(40, TimeUnit.MICROSECONDS);
        driver.get("https://x.razorpay.com/auth");
        driver.findElement(By.xpath("//input[@id=\"accountId\"]")).sendKeys("acp.avnish@gmail.com");
        Thread.sleep(1000);

    }
    

}