package constants;

public class APIEndPoints {
    public static final String MVL_START_PROCESS_API = "/api/v1/re/async/start-process";
    public static final String PLEX_WORKFLOW_HISTORY_API = "api/v1/process/resource/{partnerLoanId}/history?onlyTasks=true";
    public static final String SHIELD_GET_APPFORM_API = "/api/v1/appForm/findByPartnerLoanId/{partnerLoanId}";
    public static final String THOR_CALLBACK_API = "/api/v1/callback/status";
    public static final String DISBURSAL_INSTRUCTIONS_API = "/api/v2/disbursal/instructions";
    public static final String INITIAL_OFFER_API = "/api/v1/preApprovedOffer/file";
    public static final String CKYC_SEARCH_API = "/api/v1/ckyc/search?hardSearch=true";
    public static final String CKYC_DOWNLOAD_API = "/api/v1/ckyc/download?hardDownload=true";
    public static final String FINAL_OFFER_API = "/api/v1/finalOffer/start-process";
    public static final String GET_FINAL_OFFER_API = "/api/v1/process/resource/{partnerLoanId}/history?onlyTasks=true";
    public static final String WITHDRAWAL_INSTRUCTIONS_API = "/api/v1/withdrawal/instructions";
    public static final String WITHDRAWAL_HISTORY_API = "/api/v1/process/resource/{partnerLoanId}/history";
    public static final String DOC_TAG_API = "/api/v2/document/tag";

    public static final String DOC_BYSECTION_API = "/api/v1/appForm/{appFormId}/docsBySection";

    public static final String PCL_START_PROCCESS_API = "/api/v1/generic/start-process";
    public static final String PROCCESS_PENDING_API = "/api/v1/processPending/{workflowid}";
    public static final String WITHDRAWAL_CONFIRMATION_API = "/api/v1/withdrawal/confirmation";
//
//    public static final String HMAC_PATH_DISBURSAL_INSTRUCTIONS_UAT = "/uat/api/v2/disbursal/instructions";
//    public static final String HMAC_PATH_DISBURSAL_INSTRUCTIONS_QA2 = "/qa2/api/v2/disbursal/instructions";
//
//    public static final String HMAC_PATH_DOC_TAGGING_QA2 = "/qa2/shi/api/v1/appForm";
//    public static final String HMAC_PATH_DOC_TAGGING_UAT = "/uat/shi/api/v1/appForm";

}
