package stepDefinitions;

import base.BaseClass;
import constants.APIEndPoints;
import helpers.HelperMethods;
import helpers.ReadDataFromJson;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;


public class PCLStepDefinitions extends BaseClass {


    public static String id = "";
    public static String withdrawalId;
    public static String withdrawalpartnerLoanId;
    @When("Partner sends PCL Appform")
    public void partner_sends_pcl_appform() throws Exception {

        String pclAppform = ReadDataFromJson.updatePartnerLoanId("src/test/resources/pclAppForm.json");
        partnerLoanId = ReadDataFromJson.readPartnerLoanId(pclAppform);
        JsonPath pclResponse = HelperMethods.doPost("tarsUri",pclAppform, APIEndPoints.PCL_START_PROCCESS_API);
        JsonPath appFormResponse = HelperMethods.doGet("shieldUri" ,partnerLoanId,APIEndPoints.SHIELD_GET_APPFORM_API);
        id = appFormResponse.get("id");
        System.out.println("Appform id : "+id);

        Thread.sleep(200000); // have 3.3 min to do QA approved manually on jarvis.

        /*WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://jarvis.qa2.creditsaison.xyz/");*/

    }
    @Then("appform status should be {string}")
    public void appform_status_should_be(String appFormStatus) throws Exception {

        JsonPath appFormResponse = HelperMethods.doGet("shieldUri" ,partnerLoanId,APIEndPoints.SHIELD_GET_APPFORM_API);
        String pclAppFormStatus = appFormResponse.get("appFormStatus");
        id = appFormResponse.get("id");
        Assert.assertEquals(appFormStatus,pclAppFormStatus);
    }

    @Then("PCL Partner sends withdrawal request")
    public void partnerSendsWithdrawalRequest(String partner ) throws Exception {
        String WithdrawalInstructionBody = "{\n" +
                "    \"appFormId\": \"" + id + "\",\n" +
                "    \"amount\": 1001,\n" +
                "    \"availableLimit\": 2000000,\n" +
                "    \"tenure\": 140,\n" +
                "    \"loanIntRate\": 15,\n" +
                "    \"partnerLoanId\": \"" + partnerLoanId + "\"\n" +
                "}\n" +
                "\n";


        JsonPath withdrawalResponse = HelperMethods.doPost("mrburnsUri", WithdrawalInstructionBody, APIEndPoints.WITHDRAWAL_INSTRUCTIONS_API);
        withdrawalId = withdrawalResponse.getString("withdrawalId");
        withdrawalpartnerLoanId = withdrawalResponse.getString("partnerLoanId");
        String message = withdrawalResponse.getString("message");

        Assert.assertEquals(message, " Withdrawal instruction received for the application");
        Thread.sleep(25000);

        JsonPath withdrawalHistoryResponse = HelperMethods.doGet("herculesUri", partnerLoanId, APIEndPoints.WITHDRAWAL_HISTORY_API);
        String totalAPICalls = withdrawalHistoryResponse.getString("totalAPICalls");
        Assert.assertEquals(totalAPICalls, 7);
    }
    @Then("Partner checks withdrawal confirmation")
    public void partner_checks_withdrawal_confirmation() throws Exception {
        String withdrawalConfirmationBody = "{\n" +
                "    \"appFormId\": \"" + id + "\",\n" +
                "    \"firstEMIDate\": \"2023-02-06T12:48:31\",\n" +
                "    \"loanStartDate\": \"2022-12-06T12:48:31\",\n" +
                "    \"utr\": \"testdevPCL1b17\",\n" +
                "    \"amount\": 1001,\n" +
                "    \"partnerLoanId\": \"" + withdrawalpartnerLoanId + "\",\n" +
                "    \"withdrawalId\": \"" + withdrawalId + "\"\n" +
                "}";

        JsonPath withdrawalConfirmationResponse = HelperMethods.doPost("mrburnsUri", withdrawalConfirmationBody, APIEndPoints.WITHDRAWAL_CONFIRMATION_API);
        String message = withdrawalConfirmationResponse.get("message");
        Assert.assertEquals(message," Withdrawal confirmation received for the application");
    }

    @Then("KSF sends {string} with status {string} callback to partners")
    public void ksfSendsCallbackToPartner(String approvedMessage, String statusCode) throws Exception {
        String callBackRequest = "{\n" +
                "\"partnerLoanIds\": [\""+partnerLoanId+"\"]\n" +
                "}";
        JsonPath postMvlAppFormResponse = HelperMethods.doPost("thorUri",callBackRequest,APIEndPoints.THOR_CALLBACK_API);

    }

}
