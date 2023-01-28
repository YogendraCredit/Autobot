package stepDefinitions;

import base.BaseClass;
import constants.APIEndPoints;
import helpers.HelperMethods;
import helpers.ReadDataFromJson;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class JARStepDefinitions extends BaseClass {
    public String jarAppform = "";

    @Then("JAR sends final offer request and final offer workflow ends successfully")
    public void jarFinalOfferRequest() throws Exception {
        jarAppform = ReadDataFromJson.updatePartnerLoanId("src/test/resources/jarAppform.json");
        jarAppform = ReadDataFromJson.changePartnerLoanID(jarAppform, partnerLoanId);
        JsonPath finalOfferResponse = HelperMethods.doPost("ponyUri", jarAppform, APIEndPoints.FINAL_OFFER_API);
        String status = finalOfferResponse.getString("status");
        appFormId = finalOfferResponse.getString("appFormId");
        Assert.assertEquals(status, "success");

        Thread.sleep(28000);

        JsonPath finalOfferWorkflowResponse = HelperMethods.doGet("ponyUri", partnerLoanId, APIEndPoints.GET_FINAL_OFFER_API);
        String totalAPICalls = finalOfferWorkflowResponse.getString("totalAPICalls");
        //Assert.assertEquals(totalAPICalls, "29");

        Thread.sleep(2000);
    }
}
