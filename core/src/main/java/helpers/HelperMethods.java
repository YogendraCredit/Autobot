package helpers;

import base.BaseClass;
import com.opencsv.CSVWriter;
import constants.Headers;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static constants.Headers.*;
import static io.restassured.RestAssured.given;

public class HelperMethods extends BaseClass {


    public static RequestSpecification request;
    public static RequestSpecification multipartrequest;

    public static RequestSpecification requestSpecifications() throws Exception {

        if (request == null) {
            PrintStream log = new PrintStream(new FileOutputStream("WorkFlowLogs.txt"));
            request = new RequestSpecBuilder().setContentType(ContentType.JSON)
                    .addFilter(RequestLoggingFilter.logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .addHeader(HEADER_NAME, HEADER_VALUE).build();

            return request;
        }
        return request;
    }

    public static RequestSpecification multipartRequestSpecifications() throws Exception {

        if (multipartrequest == null) {
            PrintStream log = new PrintStream(new FileOutputStream("FileUploadLogs.txt"));
            multipartrequest = new RequestSpecBuilder().setContentType(ContentType.MULTIPART)
                    .addFilter(RequestLoggingFilter.logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .addHeader(HEADER_NAME, HEADER_VALUE).build();

            return multipartrequest;
        }
        return multipartrequest;
    }


    public static String readCsv(String csv) throws IOException {
        String line = "";
        String splitBy = ",";
        String partnerLoanId = "";

        //parsing a CSV file into BufferedReader class constructor
        BufferedReader br = new BufferedReader(new FileReader(csv));
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
            String[] csvBody = line.split(splitBy);    // use comma as separator
            partnerLoanId = csvBody[0];

        }
        return partnerLoanId;

    }

    public static JsonPath doPost(String baseUri, String requestBody, String apiEndPoints) throws Exception {
        RequestSpecification apiRequest = given().spec(requestSpecifications())
                .baseUri(initializeEnvironment(baseUri)).body(requestBody);
        String apiResponse = apiRequest.when().post(apiEndPoints)
                .then().statusCode(200).extract().response().asString();

        JsonPath jsonPath = new JsonPath(apiResponse);
        return jsonPath;
    }

    public static String doPostWithFile(String baseUri, String apiEndPoints) throws Exception {
        File file = new File("src/test/resources/initialOffer"+System.currentTimeMillis()+".csv");

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile, CSVWriter.NO_QUOTE_CHARACTER);

            // create a List which contains String array
            List<String[]> data = new ArrayList<>();
            data.add(new String[] { "partner_loan_id", "offer_id", "credit_limit","min_tenure","max_tenure","roi","preferred_tenure","date_of_offer","expiry_date_of_offer" });
            partnerLoanId = "AutomationNonFldg"+System.currentTimeMillis();
            data.add(new String[] { partnerLoanId, "c9bf8180-fd9c-4115-b786-d39fe97e1900", "200000" ,"3","60","14","48","2022-07-22","2023-10-22"});
            writer.writeAll(data);
            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        RequestSpecification apiRequest = given().spec(multipartRequestSpecifications())
                .baseUri(initializeEnvironment(baseUri)).multiPart("file", file);
        String apiResponse = apiRequest.when().post(apiEndPoints)
                .then().statusCode(200).extract().response().asString();

        //file.deleteOnExit();
        return partnerLoanId;
    }


    public static JsonPath doPostLamda(String requestBody, String apiEndPoint, String hmacSignedPath) throws Exception {

        RequestSpecification postHmacRequest = given().spec(requestSpecifications()).baseUri(initializeEnvironment("hmacUri"))
                .header(Headers.CONTENT_TYPE, Headers.JSON_TYPE).header(Headers.API_KEY, Headers.HMAC_API_KEY)
                .queryParam(Headers.HMAC_SIGNED_PATH, hmacSignedPath).queryParam(Headers.API_METHOD_CALL, Headers.POST_CALL).body(requestBody);
        String hmacResponse = postHmacRequest.when().post().then().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(hmacResponse);
        String signedDate = jsonPath.get("signedDate");
        String signGenerated = jsonPath.get("signGenerated");

        RequestSpecification postLambdaRequest = given().spec(requestSpecifications()).baseUri(initializeEnvironment("lambdaUri"))
                .header(Headers.CONTENT_TYPE, Headers.JSON_TYPE).header(Headers.USER_NAME, Headers.GUPSHUP_USER_NAME)
                .header(API_KEY, GUPSHUP_API_KEY).header(Headers.SIGNATURE,signGenerated).queryParam(Headers.SIGNED_DATE,signedDate).body(requestBody);
        String lambdaResponse = postLambdaRequest.when().post(apiEndPoint).then().statusCode(200)
                .extract().response().asString();

        JsonPath postlambdaResponse = new JsonPath(lambdaResponse);
        return postlambdaResponse;

    }

    public static JsonPath doGet(String baseUri, String partnerLoanId, String apiEndPoints) throws Exception {
        RequestSpecification apiRequest = given().spec(requestSpecifications())
                .baseUri(initializeEnvironment(baseUri)).pathParam("partnerLoanId", partnerLoanId);
        String apiResponse = apiRequest.when().get(apiEndPoints)
                .then().statusCode(200).extract().response().asString();

        JsonPath jsonPath = new JsonPath(apiResponse);
        return jsonPath;
    }

    public static JsonPath doGetWithAppForm(String baseUri, String appFormId, String apiEndPoints,String loanProductCode) throws Exception {
        RequestSpecification apiRequest = given().spec(requestSpecifications())
                .baseUri(initializeEnvironment(baseUri)).pathParam("loanProductCode", appFormId).queryParam("loanProductCode",loanProductCode);
        String apiResponse = apiRequest.when().get(apiEndPoints)
                .then().statusCode(200).extract().response().asString();

        JsonPath jsonPath = new JsonPath(apiResponse);
        return jsonPath;
    }

    public static String createCsv()
    {
        // first create file object for file placed at location specified by filepath
        File file = new File("src/test/resources/initialOfferNonFldg.csv");

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile, CSVWriter.NO_QUOTE_CHARACTER);

            // create a List which contains String array
            List<String[]> data = new ArrayList<>();
            data.add(new String[] { "partner_loan_id", "offer_id", "credit_limit","min_tenure","max_tenure","roi","preferred_tenure","date_of_offer","expiry_date_of_offer" });
            String partnerLoanId = "AutomationNonFldg"+System.currentTimeMillis();
            data.add(new String[] { partnerLoanId, "c9bf8180-fd9c-4115-b786-d39fe97e1900", "200000" ,"3","60","14","48","2022-07-22","2023-10-22"});
            writer.writeAll(data);
            // closing writer connection
            writer.close();
            file.delete();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return partnerLoanId ;
    }

   public static int countTaskList(JsonPath response){
       int taskListSize = response.getInt("taskList.size()");
       for (int i = 0; i < taskListSize; i++) {
           String taskName = response.get("taskList[" + i + "].taskName");
           System.out.println("taskName : " + taskName);
       }
       return taskListSize;
   }

}
