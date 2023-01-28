package stepDefinitions;

import base.BaseClass;
import constants.APIEndPoints;
import constants.HmacSignedPaths;
import helpers.HelperMethods;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import static helpers.HelperMethods.requestSpecifications;
import static io.restassured.RestAssured.given;

public class CommonStepDefinitions extends BaseClass {

    public String disbursalRequestBody = "";
    public String hmacSignedPathForDisbursal = "";


    @When("Partner sends Disbursal request for {string}")
    public void postDisbursalInstructionsRequest(String partner) throws Exception {
        if (partner.equals("MVL")) {
            disbursalRequestBody = System.getProperty("user.dir") + "/FLDG/src/resources/mvlDisbursalRequest.json";
        }

        if (environment.equals("qa2")) {
            hmacSignedPathForDisbursal = HmacSignedPaths.HMAC_PATH_DISBURSAL_INSTRUCTIONS_QA2;
        } else if (environment.equals("uat")) {
            hmacSignedPathForDisbursal = HmacSignedPaths.HMAC_PATH_DISBURSAL_INSTRUCTIONS_UAT;
        }else if (environment.equals("dev")) {
            hmacSignedPathForDisbursal = HmacSignedPaths.HMAC_PATH_DISBURSAL_INSTRUCTIONS_UAT;
        }
        JsonPath disbursalInstructionsResponse = HelperMethods.doPostLamda(disbursalRequestBody,APIEndPoints.DISBURSAL_INSTRUCTIONS_API,hmacSignedPathForDisbursal);

    }

    @Then("KSF sends {string} with status {string} callback to partners")
    public void ksfSendsCallbackToPartner(String approvedMessage, String statusCode) throws Exception {
        String callBackRequest = "{\n" +
                "\"partnerLoanIds\": [\""+partnerLoanId+"\"]\n" +
                "}";
        JsonPath postMvlAppFormResponse = HelperMethods.doPost("thorUri",callBackRequest,APIEndPoints.THOR_CALLBACK_API);

    }




}
