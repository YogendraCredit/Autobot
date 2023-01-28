package helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ReadDataFromJson {

    public static String readPartnerLoanId(String appform) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(appform);
        JSONObject jsonObject = (JSONObject)obj;
        String readPartnerLoanId = (String)jsonObject.get("partnerLoanId");
        return readPartnerLoanId;
    }

    public static String updatePartnerLoanId(String appform) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(appform);
        Object obj = jsonParser.parse(fileReader);
        JSONObject jsonObject = (JSONObject)obj;
        jsonObject.put("partnerLoanId","Automation"+System.currentTimeMillis());
        return jsonObject.toString();

    }

    public static String changePartnerLoanID(String appform,String partnerLoanId) throws InterruptedException, IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        //FileReader fileReader = new FileReader(appform);
        Object obj = jsonParser.parse(appform);
        JSONObject jsonObject = (JSONObject)obj;
        jsonObject.put("partnerLoanId",partnerLoanId);
        return jsonObject.toString();
    }



}
