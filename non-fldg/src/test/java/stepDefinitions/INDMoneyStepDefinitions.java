package stepDefinitions;

import base.BaseClass;
import constants.APIEndPoints;
import constants.HmacSignedPaths;
import helpers.HelperMethods;
import helpers.ReadDataFromJson;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static helpers.HelperMethods.*;
import static io.restassured.RestAssured.given;

public class INDMoneyStepDefinitions extends BaseClass {
    public String indMoneyAppform = "";
    public String hmacSignedPathForDocTagging = "";
    public static String withdrawalId = "";
    public static String withdrawalpartnerLoanId = "";

    @Given("Partner sends initial offer")
    public void postInitialOffer() throws Exception {
        partnerLoanId = HelperMethods.doPostWithFile("scarletUri", APIEndPoints.INITIAL_OFFER_API);
        System.out.println(partnerLoanId +"***************");
        Thread.sleep(20000);
    }

    @Then("INDMoney sends final offer request and final offer workflow ends successfully")
    public void indMoneySendsFinalOffer() throws Exception {
        indMoneyAppform = ReadDataFromJson.updatePartnerLoanId("src/test/resources/indMoneyOkycAppform.json");
        indMoneyAppform = ReadDataFromJson.changePartnerLoanID(indMoneyAppform, partnerLoanId);
        JsonPath finalOfferResponse = HelperMethods.doPost("ponyUri", indMoneyAppform, APIEndPoints.FINAL_OFFER_API);
        String status = finalOfferResponse.getString("status");
        appFormId = finalOfferResponse.getString("appFormId");
        Assert.assertEquals(status, "success");

        Thread.sleep(60000);

        JsonPath finalOfferWorkflowResponse = HelperMethods.doGet("ponyUri", partnerLoanId, APIEndPoints.GET_FINAL_OFFER_API);
        int taskListSize = HelperMethods.countTaskList(finalOfferWorkflowResponse);
        Assert.assertEquals(taskListSize, "9");

        Thread.sleep(2000);
    }

    @And("Partner sends Loan Agreement")
    public void postLoanAgreement() throws Exception {
        String docTagRequestBody = "{\n" +
                "    \"partnerLoanId\": \""+partnerLoanId+"\",\n" +
                "    \"appForm\": {\n" +
                "        \"documents\": [\n" +
                "            {\n" +
                "                \"fileName\": \""+partnerLoanId+"_LA.pdf\",\n" +
                "                \"docType\": \"Loan Agreement\",\n" +
                "                \"docCategory\": [],\n" +
                "                \"url\": \"https://www.africau.edu/images/default/sample.pdf\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        if (environment.equals("qa2")) {
            hmacSignedPathForDocTagging = HmacSignedPaths.HMAC_PATH_DOC_TAGGING_QA2;
        }else if (environment.equals("uat")) {
            hmacSignedPathForDocTagging = HmacSignedPaths.HMAC_PATH_DOC_TAGGING_UAT;
        }else if (environment.equals("dev")) {
            hmacSignedPathForDocTagging = HmacSignedPaths.HMAC_PATH_DOC_TAGGING_DEV;
        }
        HelperMethods.doPostLamda(docTagRequestBody,APIEndPoints.DOC_TAG_API,hmacSignedPathForDocTagging);
        Thread.sleep(40000);

        JsonPath docStatusResponse = HelperMethods.doGetWithAppForm("drstrangeUri",appFormId,APIEndPoints.DOC_BYSECTION_API,"INC");
//        String fileName =  docStatusResponse.get("untagged.filename");
//        Assert.assertEquals(fileName,"partnerLoanId"+"_LA.pdf" );

    }


    @Then("Post final offer workflow starts and ends")
    public void postFinalOfferWorkflowStartsAndEnds() throws Exception {
        JsonPath finalOfferWorkflowResponse = HelperMethods.doGet("ponyUri", partnerLoanId, APIEndPoints.GET_FINAL_OFFER_API);
        int taskListSize = HelperMethods.countTaskList(finalOfferWorkflowResponse);
        Assert.assertEquals(taskListSize, 7);
        JsonPath appFormResponse = HelperMethods.doGet("shieldUri" ,partnerLoanId,APIEndPoints.SHIELD_GET_APPFORM_API);
        String appFormStatus = appFormResponse.get("appFormStatus");
        System.out.print("actualStatus : " + appFormStatus);
        Assert.assertEquals(appFormStatus,"14");
//        appFormResponse.get("linkedIndividuals")[2][]
//        "cif": null,
//                "limitId": null,
//        // add check for limitid and cif
//        Assert.assertNotEquals();
    }

    @And("Green Channel workflow is triggered")
    public void greenChannelWorkflowIsTriggered() throws Exception {

        JsonPath appFormResponse = HelperMethods.doGet("shieldUri" ,partnerLoanId,APIEndPoints.SHIELD_GET_APPFORM_API);
        String appFormStatus = appFormResponse.get("appFormStatus");
        System.out.print("actualStatus : " + appFormStatus);
        Assert.assertEquals(appFormStatus, "15");
    }

    @Then("Partner sends withdrawal request")
    public void partnerSendsWithdrawalRequest(String partner ) throws Exception {
        String WithdrawalInstructionBody = "{\n" +
                "    \"appFormId\": \"" + appFormId + "\",\n" +
                "    \"amount\": 627,\n" +
                "    \"tenure\": 12,\n" +
                "    \"partnerLoanId\": \"" + partnerLoanId + "\"\n" +
                "}";

        JsonPath withdrawalResponse = HelperMethods.doPost("mrburnsUri", WithdrawalInstructionBody, APIEndPoints.WITHDRAWAL_INSTRUCTIONS_API);
        withdrawalId = withdrawalResponse.getString("withdrawalId");
        withdrawalpartnerLoanId = withdrawalResponse.getString("partnerLoanId");
        String message = withdrawalResponse.getString("message");

        Assert.assertEquals(message, " Withdrawal instruction received for the application");
        Thread.sleep(25000);

        JsonPath withdrawalHistoryResponse = HelperMethods.doGet("herculesUri", partnerLoanId, APIEndPoints.WITHDRAWAL_HISTORY_API);
        int taskListSize = HelperMethods.countTaskList(withdrawalHistoryResponse);
        Assert.assertEquals(taskListSize, 7);

    }

    @When("Partner Searches and Downloads CKYC")
    public void partnerSearchesAndDownloadsCKYC() throws Exception {

        String ckycSearchRequestBody =  "{\n" +
                        "    \"loanProduct\": \"INC\",\n" +
                        "    \"kycType\": \"panCard\",\n" +
                        "    \"kycValue\": \"BCRPS1002G\",\n" +
                        "    \"dob\": \"10-05-1998\",\n" +
                        "    \"mobileNumber\": \"9023851213\",\n" +
                        "    \"pincode\": \"560047\",\n" +
                        "    \"entityType\": \"partnerLoan\",\n" +
                        "    \"partnerLoanID\": \"" + partnerLoanId + "\"\n" +
                        "}";
        JsonPath ckycSearchResponse = HelperMethods.doPost("eywaUri", ckycSearchRequestBody, APIEndPoints.CKYC_SEARCH_API);
        String status = ckycSearchResponse.getString("status");
        Assert.assertEquals(status, "success");
        Thread.sleep(2000);

        String ckycDownloadRaquest = "{\n" +
                        "    \"identifierValue\":\"20084683812794\",\n" +
                        "    \"dob\":\"10-05-1998\",\n" +
                        "    \"identifierType\":\"ckycNo\",\n" +
                        "    \"mobileNumber\":\"9023851213\",\n" +
                        "    \"pincode\":\"\",\n" +
                        "    \"birthYear\":\"\",\n" +
                        "    \"entityId\": \"" + partnerLoanId + "\",\n" +
                        "    \"entityType\": \"partnerLoanId\"\n" +
                        "}";

        JsonPath ckycDownloadResponse = HelperMethods.doPost("eywaUri",ckycDownloadRaquest, APIEndPoints.CKYC_DOWNLOAD_API);
        String Message = ckycDownloadResponse.getString("downloadStatus.Message");
        Assert.assertEquals(Message, "CKYC Successfully Downloaded");
        Thread.sleep(2000);

    }


    @And("KSF reviews QC steps")
    public void ksfReviewsQCSteps() {
    }

    @Then("KSF sends {string} with status {string} callback to partners")
    public void ksfSendsCallbackToPartner(String approvedMessage, String statusCode) throws Exception {
        String callBackRequest = "{\n" +
                "\"partnerLoanIds\": [\""+partnerLoanId+"\"]\n" +
                "}";
        JsonPath postMvlAppFormResponse = HelperMethods.doPost("thorUri",callBackRequest,APIEndPoints.THOR_CALLBACK_API);

    }
}