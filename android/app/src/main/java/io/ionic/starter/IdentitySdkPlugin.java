package io.ionic.starter;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.idmission.sdk2.client.model.CommonApiResponse;
import com.idmission.sdk2.client.model.ExtractedIdData;
import com.idmission.sdk2.client.model.ExtractedPersonalData;
import com.idmission.sdk2.client.model.HostDataResponse;
import com.idmission.sdk2.client.model.InitializeResponse;
import com.idmission.sdk2.client.model.Response;
import com.idmission.sdk2.client.model.ResponseCustomerData;
import com.idmission.sdk2.identityproofing.IdentityProofingSDK;
import com.idmission.sdk2.client.model.AdditionalCustomerLiveCheckResponseData;
import com.idmission.sdk2.client.model.AliasesResponse;
import com.idmission.sdk2.client.model.CriminalRecordResponse;
import com.idmission.sdk2.client.model.NmResultResponse;
import com.idmission.sdk2.client.model.OffensesResponse;
import com.idmission.sdk2.client.model.PepResultResponse;
import com.idmission.sdk2.client.model.ProfilesResponse;
import com.idmission.sdk2.client.model.ResultData;
import com.idmission.sdk2.client.model.SexOffendersResponse;
import com.idmission.sdk2.client.model.Status;
import com.idmission.sdk2.client.model.TextMatchResultResponse;
import com.idmission.sdk2.client.model.WlsResultResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

@CapacitorPlugin
public class IdentitySdkPlugin extends Plugin {
    String initializeApiBaseUrl = "https://demo.idmission.com/";
    String apiBaseUrl = "https://apidemo.idmission.com/";
    String loginID = "";
    String password = "";
    long merchantID = 0;

    @PluginMethod
    public void  idm_sdk_init(PluginCall call) {
        if(!StringUtils.isEmpty(call.getString("username"))){
            loginID = call.getString("username").toString();
        }
        if(!StringUtils.isEmpty(call.getString("password"))){
            password = call.getString("password").toString();
        }
        if(!StringUtils.isEmpty(call.getString("merchantID"))){
            merchantID = Long.parseLong(call.getString("merchantID"));
        }

      new BackgroundTask(call).execute();
    }

    @PluginMethod
    public void  idm_sdk_serviceID20(PluginCall call) {
      Intent i = new Intent(getActivity(), CallPluginActivity.class);
      i.putExtra("serviceID", 20);
      startActivityForResult(call, i, "callback");
    }

    @PluginMethod
    public void  idm_sdk_serviceID10(PluginCall call) {
      Intent i = new Intent(getActivity(), CallPluginActivity.class);
      i.putExtra("serviceID", 10);
      startActivityForResult(call, i, "callback");
    }

    @PluginMethod
    public void  idm_sdk_serviceID185(PluginCall call) {
      Intent i = new Intent(getActivity(), CallPluginActivity.class);
      i.putExtra("serviceID", 185);
      startActivityForResult(call, i, "callback");
    }

    @PluginMethod
    public void  idm_sdk_serviceID660(PluginCall call) {
      Intent i = new Intent(getActivity(), CallPluginActivity.class);
      i.putExtra("serviceID", 660);
      startActivityForResult(call, i, "callback");
    }

    @PluginMethod
    public void  idm_sdk_serviceID50(PluginCall call) {
        String uniqueCustomerNumber = call.getInt("uniqueCustomerNumber").toString();
        if(!StringUtils.isEmpty(uniqueCustomerNumber)){
          Intent i = new Intent(getActivity(), CallPluginActivity.class);
          i.putExtra("serviceID", 50);
          i.putExtra("uniqueCustomerNumber", uniqueCustomerNumber);
          startActivityForResult(call, i, "callback");
        }
    }

    @PluginMethod
    public void  idm_sdk_serviceID175(PluginCall call) {
        String uniqueCustomerNumber = call.getInt("uniqueCustomerNumber").toString();
        if(!StringUtils.isEmpty(uniqueCustomerNumber)){
          Intent i = new Intent(getActivity(), CallPluginActivity.class);
          i.putExtra("serviceID", 175);
          i.putExtra("uniqueCustomerNumber", uniqueCustomerNumber);
          startActivityForResult(call, i, "callback");
        }
    }

    @PluginMethod
    public void  idm_sdk_serviceID105(PluginCall call) {
        String uniqueCustomerNumber = call.getInt("uniqueCustomerNumber").toString();
        if(!StringUtils.isEmpty(uniqueCustomerNumber)){
          Intent i = new Intent(getActivity(), CallPluginActivity.class);
          i.putExtra("serviceID", 105);
          i.putExtra("uniqueCustomerNumber", uniqueCustomerNumber);
          startActivityForResult(call, i, "callback");
        }
    }

    @PluginMethod
    public void submit_result(PluginCall call) {
        new FinalSubmitTask(call).execute();
    }

    @ActivityCallback
    private void callback(PluginCall call, ActivityResult result) {
      JSObject results = new JSObject();
      results.put("result",CallPluginActivity.result);
      call.resolve(results);
    }

    class BackgroundTask extends AsyncTask<Void, Void, Response<InitializeResponse>> {
          PluginCall call;

          BackgroundTask(PluginCall calls){
              call = calls;
          }

        @Override
        protected void onPostExecute(Response<InitializeResponse> initializeResponseResponse) {
            super.onPostExecute(initializeResponseResponse);
            JSObject result = new JSObject();
            result.put("result",initializeResponseResponse.getResult().toString());
            call.resolve(result);
        }

        @Override
        protected Response<InitializeResponse> doInBackground(Void... voids) {
            Response<InitializeResponse> response =
                    IdentityProofingSDK.INSTANCE.initialize(getActivity(),
                            initializeApiBaseUrl,
                            apiBaseUrl,
                            loginID,
                            password,
                            merchantID,
                            true,
                            true);
            return response;
        }
    }

    class FinalSubmitTask extends AsyncTask<Void, Void, Response<CommonApiResponse>> {
        PluginCall call;

        FinalSubmitTask(PluginCall calls){
          call = calls;
        }

        @Override
        protected void onPostExecute(Response<CommonApiResponse> apiResponse) {
            super.onPostExecute(apiResponse);
            if(apiResponse.getErrorStatus()!=null) {
              JSObject result = new JSObject();
              result.put("result",apiResponse.getErrorStatus().getStatusMessage());
              call.resolve(result);
            } else  {
              JSObject result = new JSObject();
              result.put("result",parseResponse(apiResponse));
              call.resolve(result);
            }
        }

        @Override
        protected Response<CommonApiResponse> doInBackground(Void... voids) {
            Response<CommonApiResponse> response =
                    IdentityProofingSDK.INSTANCE.finalSubmit(getActivity());

            return response;
        }
    }

    private String parseResponse(Response<CommonApiResponse> response){
        JSONObject jo = new JSONObject();
        try {
            JSONObject statusData = new JSONObject();

            Status status = response.getResult().getStatus();
            statusData.put("statusCode",status.getStatusCode());
            statusData.put("requestId",status.getRequestId());
            statusData.put("errorData",status.getErrorData());
            statusData.put("statusMessage",status.getStatusMessage());

            jo.put("status", statusData);
        }catch(Exception e){}

        try {
            JSONObject resultData = new JSONObject();

            ResultData rd = response.getResult().getResultData();
            resultData.put("uniqueRequestId",rd.getUniqueRequestId());
            resultData.put("verificationResultCode",rd.getVerificationResultCode());
            resultData.put("verificationResultId",rd.getVerificationResultId());
            resultData.put("verificationResult",rd.getVerificationResult());

            jo.put("resultData", resultData);
        }catch(Exception e){}

        try {
            JSONObject responseCustomerData = new JSONObject();
            JSONObject extractedIdData = new JSONObject();
            JSONObject extractedPersonalData = new JSONObject();
            JSONObject hostDataResponseData = new JSONObject();
            JSONObject criminalRecordResponseData = new JSONObject();
            JSONObject aliasesResponseData = new JSONObject();
            JSONObject offensesResponseData = new JSONObject();
            JSONObject profilesResponseData = new JSONObject();
            JSONObject nmResultResponseResponseData = new JSONObject();
            JSONObject pepResultResponseData = new JSONObject();
            JSONObject textMatchResultResponseData = new JSONObject();
            JSONObject sexOffendersResponseData = new JSONObject();
            JSONObject profilesResponse2Data = new JSONObject();
            JSONObject wlsResultResponseData = new JSONObject();

            ResponseCustomerData rcd = response.getResult().getResponseCustomerData();

            try {
                ExtractedIdData eid = rcd.getExtractedIdData();
                extractedIdData.put("barcodeDataParsed",eid.getBarcodeDataParsed());
                extractedIdData.put("idCountry",eid.getIdCountry());
                extractedIdData.put("idDateOfBirth",eid.getIdDateOfBirth());
                extractedIdData.put("idDateOfBirthFormatted",eid.getIdDateOfBirthFormatted());
                extractedIdData.put("idDateOfBirthNonEng",eid.getIdDateOfBirthNonEng());
                extractedIdData.put("idExpirationDate",eid.getIdExpirationDate());
                extractedIdData.put("idExpirationDateFormatted",eid.getIdExpirationDateFormatted());
                extractedIdData.put("idExpirationDateNonEng",eid.getIdExpirationDateNonEng());
                extractedIdData.put("idIssueCountry",eid.getIdIssueCountry());
                extractedIdData.put("idIssueDate",eid.getIdIssueDate());
                extractedIdData.put("idIssueDateNonEng",eid.getIdIssueDateNonEng());
                extractedIdData.put("idNumber",eid.getIdNumber());
                extractedIdData.put("idNumberNonEng",eid.getIdNumberNonEng());
                extractedIdData.put("idNumber1",eid.getIdNumber1());
                extractedIdData.put("idNumber2",eid.getIdNumber2());
                extractedIdData.put("idNumber2NonEng",eid.getIdNumber2NonEng());
                extractedIdData.put("idNumber3",eid.getIdNumber3());
                extractedIdData.put("idState",eid.getIdState());
                extractedIdData.put("idType",eid.getIdType());
                extractedIdData.put("mrzData",eid.getMrzData());
                responseCustomerData.put("extractedIdData",extractedIdData);
            }catch(Exception e){}

            try {
                ExtractedPersonalData epd = rcd.getExtractedPersonalData();
                extractedPersonalData.put("addressLine1",epd.getAddressLine1());
                extractedPersonalData.put("addressLine1NonEng",epd.getAddressLine1NonEng());
                extractedPersonalData.put("addressLine2",epd.getAddressLine2());
                extractedPersonalData.put("addressLine2NonEng",epd.getAddressLine2NonEng());
                extractedPersonalData.put("city",epd.getCity());
                extractedPersonalData.put("addressNonEng",epd.getAddressNonEng());
                extractedPersonalData.put("country",epd.getCountry());
                extractedPersonalData.put("district",epd.getDistrict());
                extractedPersonalData.put("dob",epd.getDob());
                extractedPersonalData.put("email",epd.getEmail());
                extractedPersonalData.put("enrolledDate",epd.getEnrolledDate());
                extractedPersonalData.put("firstName",epd.getFirstName());
                extractedPersonalData.put("firstNameNonEng",epd.getFirstNameNonEng());
                extractedPersonalData.put("gender",epd.getGender());
                extractedPersonalData.put("lastName",epd.getLastName());
                extractedPersonalData.put("lastName2",epd.getLastName2());
                extractedPersonalData.put("lastNameNonEng",epd.getLastNameNonEng());
                extractedPersonalData.put("name",epd.getName());
                extractedPersonalData.put("phone",epd.getPhone());
                extractedPersonalData.put("uniqueNumber",epd.getUniqueNumber());
                extractedPersonalData.put("middleName",epd.getMiddleName());
                extractedPersonalData.put("middleNameNonEng",epd.getMiddleNameNonEng());
                responseCustomerData.put("extractedPersonalData",extractedPersonalData);
            }catch(Exception e){}

            try {
                HostDataResponse hdr = rcd.getHostData();

                try {
                    CriminalRecordResponse crr = hdr.getCriminalRecord();

                    try {
                        AliasesResponse ar = crr.getAliasesResponse();
                        aliasesResponseData.put("firstName",ar.getFirstName());
                        aliasesResponseData.put("middleName",ar.getMiddleName());
                        aliasesResponseData.put("lastName",ar.getLastName());
                        aliasesResponseData.put("fullName",ar.getFullName());
                        criminalRecordResponseData.put("aliases",aliasesResponseData);
                    }catch(Exception e){}

                    try {
                        OffensesResponse or = crr.getOffensesResponse();
                        offensesResponseData.put("addmissionDate",or.getAddmissionDate());
                        offensesResponseData.put("ageOfVictim",or.getAgeOfVictim());
                        offensesResponseData.put("arrestingAgency",or.getArrestingAgency());
                        offensesResponseData.put("caseNumber",or.getCaseNumber());
                        offensesResponseData.put("category",or.getCategory());
                        offensesResponseData.put("chargeFillingDate",or.getChargeFillingDate());
                        offensesResponseData.put("closedDate",or.getClosedDate());
                        offensesResponseData.put("code",or.getCode());
                        offensesResponseData.put("counts",or.getCounts());
                        offensesResponseData.put("courts",or.getCourts());
                        offensesResponseData.put("dateConvicted",or.getDateConvicted());
                        offensesResponseData.put("dateOfCrime",or.getDateOfCrime());
                        offensesResponseData.put("dateOfWarrant",or.getDateOfWarrant());
                        offensesResponseData.put("description",or.getDescription());
                        offensesResponseData.put("dispositionDate",or.getDispositionDate());
                        offensesResponseData.put("dispostion",or.getDispostion());
                        offensesResponseData.put("facility",or.getFacility());
                        offensesResponseData.put("jurisdication",or.getJurisdication());
                        offensesResponseData.put("prisonerNumber",or.getPrisonerNumber());
                        offensesResponseData.put("relationshipToVictim",or.getRelationshipToVictim());
                        offensesResponseData.put("releaseDate",or.getReleaseDate());
                        offensesResponseData.put("section",or.getSection());
                        offensesResponseData.put("sentence",or.getSentence());
                        offensesResponseData.put("sentenceDate",or.getSentenceDate());
                        offensesResponseData.put("subsection",or.getSubsection());
                        offensesResponseData.put("title",or.getTitle());
                        offensesResponseData.put("warrantDate",or.getWarrantDate());
                        offensesResponseData.put("warrantNumber",or.getWarrantNumber());
                        offensesResponseData.put("weaponsUsed",or.getWeaponsUsed());
                        criminalRecordResponseData.put("offenses",offensesResponseData);
                    }catch(Exception e){}

                    try {
                        ProfilesResponse pr = crr.getProfiles();
                        profilesResponseData.put("city",pr.getCity());
                        profilesResponseData.put("country",pr.getCountry());
                        profilesResponseData.put("fullName",pr.getFullName());
                        profilesResponseData.put("firstName",pr.getFirstName());
                        profilesResponseData.put("middleName",pr.getMiddleName());
                        profilesResponseData.put("address",pr.getAddress());
                        profilesResponseData.put("convictionType",pr.getConvictionType());
                        profilesResponseData.put("countryCode",pr.getCountryCode());
                        profilesResponseData.put("countryName",pr.getCountryName());
                        profilesResponseData.put("dobOfBirth",pr.getDobOfBirth());
                        profilesResponseData.put("drivingLicenseVerificationResult",pr.getDrivingLicenseVerificationResult());
                        profilesResponseData.put("internalId",pr.getInternalId());
                        profilesResponseData.put("internalIdCriminalRecords",pr.getInternalIdCriminalRecords());
                        profilesResponseData.put("lastName",pr.getLastName());
                        profilesResponseData.put("photoUrl",pr.getPhotoUrl());
                        profilesResponseData.put("postalCode",pr.getPostalCode());
                        profilesResponseData.put("sex",pr.getSex());
                        profilesResponseData.put("source",pr.getSource());
                        profilesResponseData.put("state",pr.getState());
                        profilesResponseData.put("street1",pr.getStreet1());
                        profilesResponseData.put("street2",pr.getStreet2());
                        profilesResponseData.put("verificationResult",pr.getVerificationResult());
                        criminalRecordResponseData.put("profiles",profilesResponseData);
                    }catch(Exception e){}

                    hostDataResponseData.put("criminalRecord",criminalRecordResponseData);

                }catch(Exception e){}

                try {
                    NmResultResponse nrr = hdr.getNmresult();
                    nmResultResponseResponseData.put("createdOnNM",nrr.getCreatedOnNM());
                    nmResultResponseResponseData.put("orderIdNM",nrr.getOrderIdNM());
                    nmResultResponseResponseData.put("resultCountNM",nrr.getResultCountNM());
                    nmResultResponseResponseData.put("orderStatusNM",nrr.getOrderStatusNM());
                    nmResultResponseResponseData.put("orderUrlNM",nrr.getOrderUrlNM());
                    nmResultResponseResponseData.put("productIdNM",nrr.getProductIdNM());
                    nmResultResponseResponseData.put("vital4APIHostTried",nrr.getVital4APIHostTried());
                    hostDataResponseData.put("nmResult",nmResultResponseResponseData);
                }catch(Exception e){}

                try {
                    PepResultResponse prr = hdr.getPepresult();
                    pepResultResponseData.put("createdOnPEP",prr.getCreatedOnPEP());
                    pepResultResponseData.put("orderIdPEP",prr.getOrderIdPEP());
                    pepResultResponseData.put("resultCountPEP",prr.getResultCountPEP());
                    pepResultResponseData.put("productId_PEP",prr.getProductId_PEP());
                    pepResultResponseData.put("orderUrlPEP",prr.getOrderUrlPEP());
                    pepResultResponseData.put("orderStatus_PEP",prr.getOrderStatus_PEP());
                    hostDataResponseData.put("pepresult",pepResultResponseData);
                }catch(Exception e){}

                try {
                    TextMatchResultResponse tmrr = hdr.getTextMatchResult();
                    textMatchResultResponseData.put("address",tmrr.getAddress());
                    textMatchResultResponseData.put("addressCityMatch",tmrr.getAddressCityMatch());
                    textMatchResultResponseData.put("addressLine1Match",tmrr.getAddressLine1Match());
                    textMatchResultResponseData.put("addressLine2Match",tmrr.getAddressLine2Match());
                    textMatchResultResponseData.put("addressZIP4Match",tmrr.getAddressZIP4Match());
                    textMatchResultResponseData.put("addressStateCodeMatch",tmrr.getAddressStateCodeMatch());
                    textMatchResultResponseData.put("addressZIP5Match",tmrr.getAddressZIP5Match());
                    textMatchResultResponseData.put("documentCategoryMatch",tmrr.getDocumentCategoryMatch());
                    textMatchResultResponseData.put("driverLicenseExpirationDateMatch",tmrr.getDriverLicenseExpirationDateMatch());
                    textMatchResultResponseData.put("driverLicenseIssueDateMatch",tmrr.getDriverLicenseIssueDateMatch());
                    textMatchResultResponseData.put("driverLicenseNumberMatch",tmrr.getDriverLicenseNumberMatch());
                    textMatchResultResponseData.put("hostTried",tmrr.getHostTried());
                    textMatchResultResponseData.put("identiFraudHostTried",tmrr.getIdentiFraudHostTried());
                    textMatchResultResponseData.put("personBirthDateMatch",tmrr.getPersonBirthDateMatch());
                    textMatchResultResponseData.put("personFirstNameExactMatch",tmrr.getPersonFirstNameExactMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyAlternateMatch",tmrr.getPersonFirstNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyPrimaryMatch",tmrr.getPersonFirstNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personLastNameExactMatch",tmrr.getPersonLastNameExactMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyAlternateMatch",tmrr.getPersonLastNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyPrimaryMatch",tmrr.getPersonLastNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personMiddleInitialMatch",tmrr.getPersonMiddleInitialMatch());
                    textMatchResultResponseData.put("personMiddleNameExactMatch",tmrr.getPersonMiddleNameExactMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyAlternateMatch",tmrr.getPersonMiddleNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyPrimaryMatch",tmrr.getPersonMiddleNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personSexCodeMatch",tmrr.getPersonSexCodeMatch());
                    textMatchResultResponseData.put("servicePresent",tmrr.getServicePresent());
                    textMatchResultResponseData.put("thirdPartyVerificationResultDescription",tmrr.getThirdPartyVerificationResultDescription());
                    textMatchResultResponseData.put("verificationResult",tmrr.getVerificationResult());
                    hostDataResponseData.put("textMatchResult",textMatchResultResponseData);
                }catch(Exception e){}

                try {
                    SexOffendersResponse sor = hdr.getSexOffenders();

                    try {
                        ProfilesResponse pr2 = sor.getProfiles();
                        profilesResponse2Data.put("city",pr2.getCity());
                        profilesResponse2Data.put("country",pr2.getCountry());
                        profilesResponse2Data.put("fullName",pr2.getFullName());
                        profilesResponse2Data.put("firstName",pr2.getFirstName());
                        profilesResponse2Data.put("middleName",pr2.getMiddleName());
                        profilesResponse2Data.put("address",pr2.getAddress());
                        profilesResponse2Data.put("convictionType",pr2.getConvictionType());
                        profilesResponse2Data.put("countryCode",pr2.getCountryCode());
                        profilesResponse2Data.put("countryName",pr2.getCountryName());
                        profilesResponse2Data.put("dobOfBirth",pr2.getDobOfBirth());
                        profilesResponse2Data.put("drivingLicenseVerificationResult",pr2.getDrivingLicenseVerificationResult());
                        profilesResponse2Data.put("internalId",pr2.getInternalId());
                        profilesResponse2Data.put("internalIdCriminalRecords",pr2.getInternalIdCriminalRecords());
                        profilesResponse2Data.put("lastName",pr2.getLastName());
                        profilesResponse2Data.put("photoUrl",pr2.getPhotoUrl());
                        profilesResponse2Data.put("postalCode",pr2.getPostalCode());
                        profilesResponse2Data.put("sex",pr2.getSex());
                        profilesResponse2Data.put("source",pr2.getSource());
                        profilesResponse2Data.put("state",pr2.getState());
                        profilesResponse2Data.put("street1",pr2.getStreet1());
                        profilesResponse2Data.put("street2",pr2.getStreet2());
                        profilesResponse2Data.put("verificationResult",pr2.getVerificationResult());
                        sexOffendersResponseData.put("profiles",profilesResponse2Data);
                    }catch(Exception e){}

                    hostDataResponseData.put("sexOffenders",sexOffendersResponseData);
                }catch(Exception e){}

                try {
                    WlsResultResponse wrr = hdr.getWlsresult();
                    wlsResultResponseData.put("createdOnWLS",wrr.getCreatedOnWLS());
                    wlsResultResponseData.put("orderIdWLS",wrr.getOrderIdWLS());
                    wlsResultResponseData.put("resultCountWLS",wrr.getResultCountWLS());
                    wlsResultResponseData.put("productIdWLS",wrr.getProductIdWLS());
                    wlsResultResponseData.put("orderStatusWLS",wrr.getOrderStatusWLS());
                    wlsResultResponseData.put("orderUrlWLS",wrr.getOrderUrlWLS());
                    hostDataResponseData.put("wlsresult",wlsResultResponseData);
                }catch(Exception e){}

                responseCustomerData.put("hostDataResponse",hostDataResponseData);
            }catch(Exception e){}

            jo.put("responseCustomerData", responseCustomerData);
        }catch(Exception e){}

        try {
            JSONObject responseCustomerVerifyData = new JSONObject();
            JSONObject extractedIdData = new JSONObject();
            JSONObject extractedPersonalData = new JSONObject();
            JSONObject hostDataResponseData = new JSONObject();
            JSONObject criminalRecordResponseData = new JSONObject();
            JSONObject aliasesResponseData = new JSONObject();
            JSONObject offensesResponseData = new JSONObject();
            JSONObject profilesResponseData = new JSONObject();
            JSONObject nmResultResponseResponseData = new JSONObject();
            JSONObject pepResultResponseData = new JSONObject();
            JSONObject textMatchResultResponseData = new JSONObject();
            JSONObject sexOffendersResponseData = new JSONObject();
            JSONObject profilesResponse2Data = new JSONObject();
            JSONObject wlsResultResponseData = new JSONObject();

            ResponseCustomerData rcvd = response.getResult().getResponseCustomerVerifyData();

            try {
                ExtractedIdData eid = rcvd.getExtractedIdData();
                extractedIdData.put("barcodeDataParsed",eid.getBarcodeDataParsed());
                extractedIdData.put("idCountry",eid.getIdCountry());
                extractedIdData.put("idDateOfBirth",eid.getIdDateOfBirth());
                extractedIdData.put("idDateOfBirthFormatted",eid.getIdDateOfBirthFormatted());
                extractedIdData.put("idDateOfBirthNonEng",eid.getIdDateOfBirthNonEng());
                extractedIdData.put("idExpirationDate",eid.getIdExpirationDate());
                extractedIdData.put("idExpirationDateFormatted",eid.getIdExpirationDateFormatted());
                extractedIdData.put("idExpirationDateNonEng",eid.getIdExpirationDateNonEng());
                extractedIdData.put("idIssueCountry",eid.getIdIssueCountry());
                extractedIdData.put("idIssueDate",eid.getIdIssueDate());
                extractedIdData.put("idIssueDateNonEng",eid.getIdIssueDateNonEng());
                extractedIdData.put("idNumber",eid.getIdNumber());
                extractedIdData.put("idNumberNonEng",eid.getIdNumberNonEng());
                extractedIdData.put("idNumber1",eid.getIdNumber1());
                extractedIdData.put("idNumber2",eid.getIdNumber2());
                extractedIdData.put("idNumber2NonEng",eid.getIdNumber2NonEng());
                extractedIdData.put("idNumber3",eid.getIdNumber3());
                extractedIdData.put("idState",eid.getIdState());
                extractedIdData.put("idType",eid.getIdType());
                extractedIdData.put("mrzData",eid.getMrzData());
                responseCustomerVerifyData.put("extractedIdData",extractedIdData);
            }catch(Exception e){}

            try {
                ExtractedPersonalData epd = rcvd.getExtractedPersonalData();
                extractedPersonalData.put("addressLine1",epd.getAddressLine1());
                extractedPersonalData.put("addressLine1NonEng",epd.getAddressLine1NonEng());
                extractedPersonalData.put("addressLine2",epd.getAddressLine2());
                extractedPersonalData.put("addressLine2NonEng",epd.getAddressLine2NonEng());
                extractedPersonalData.put("city",epd.getCity());
                extractedPersonalData.put("addressNonEng",epd.getAddressNonEng());
                extractedPersonalData.put("country",epd.getCountry());
                extractedPersonalData.put("district",epd.getDistrict());
                extractedPersonalData.put("dob",epd.getDob());
                extractedPersonalData.put("email",epd.getEmail());
                extractedPersonalData.put("enrolledDate",epd.getEnrolledDate());
                extractedPersonalData.put("firstName",epd.getFirstName());
                extractedPersonalData.put("firstNameNonEng",epd.getFirstNameNonEng());
                extractedPersonalData.put("gender",epd.getGender());
                extractedPersonalData.put("lastName",epd.getLastName());
                extractedPersonalData.put("lastName2",epd.getLastName2());
                extractedPersonalData.put("lastNameNonEng",epd.getLastNameNonEng());
                extractedPersonalData.put("name",epd.getName());
                extractedPersonalData.put("phone",epd.getPhone());
                extractedPersonalData.put("uniqueNumber",epd.getUniqueNumber());
                extractedPersonalData.put("middleName",epd.getMiddleName());
                extractedPersonalData.put("middleNameNonEng",epd.getMiddleNameNonEng());
                responseCustomerVerifyData.put("extractedPersonalData",extractedPersonalData);
            }catch(Exception e){}

            try {
                HostDataResponse hdr = rcvd.getHostData();

                try {
                    CriminalRecordResponse crr = hdr.getCriminalRecord();

                    try {
                        AliasesResponse ar = crr.getAliasesResponse();
                        aliasesResponseData.put("firstName",ar.getFirstName());
                        aliasesResponseData.put("middleName",ar.getMiddleName());
                        aliasesResponseData.put("lastName",ar.getLastName());
                        aliasesResponseData.put("fullName",ar.getFullName());
                        criminalRecordResponseData.put("aliases",aliasesResponseData);
                    }catch(Exception e){}

                    try {
                        OffensesResponse or = crr.getOffensesResponse();
                        offensesResponseData.put("addmissionDate",or.getAddmissionDate());
                        offensesResponseData.put("ageOfVictim",or.getAgeOfVictim());
                        offensesResponseData.put("arrestingAgency",or.getArrestingAgency());
                        offensesResponseData.put("caseNumber",or.getCaseNumber());
                        offensesResponseData.put("category",or.getCategory());
                        offensesResponseData.put("chargeFillingDate",or.getChargeFillingDate());
                        offensesResponseData.put("closedDate",or.getClosedDate());
                        offensesResponseData.put("code",or.getCode());
                        offensesResponseData.put("counts",or.getCounts());
                        offensesResponseData.put("courts",or.getCourts());
                        offensesResponseData.put("dateConvicted",or.getDateConvicted());
                        offensesResponseData.put("dateOfCrime",or.getDateOfCrime());
                        offensesResponseData.put("dateOfWarrant",or.getDateOfWarrant());
                        offensesResponseData.put("description",or.getDescription());
                        offensesResponseData.put("dispositionDate",or.getDispositionDate());
                        offensesResponseData.put("dispostion",or.getDispostion());
                        offensesResponseData.put("facility",or.getFacility());
                        offensesResponseData.put("jurisdication",or.getJurisdication());
                        offensesResponseData.put("prisonerNumber",or.getPrisonerNumber());
                        offensesResponseData.put("relationshipToVictim",or.getRelationshipToVictim());
                        offensesResponseData.put("releaseDate",or.getReleaseDate());
                        offensesResponseData.put("section",or.getSection());
                        offensesResponseData.put("sentence",or.getSentence());
                        offensesResponseData.put("sentenceDate",or.getSentenceDate());
                        offensesResponseData.put("subsection",or.getSubsection());
                        offensesResponseData.put("title",or.getTitle());
                        offensesResponseData.put("warrantDate",or.getWarrantDate());
                        offensesResponseData.put("warrantNumber",or.getWarrantNumber());
                        offensesResponseData.put("weaponsUsed",or.getWeaponsUsed());
                        criminalRecordResponseData.put("offenses",offensesResponseData);
                    }catch(Exception e){}

                    try {
                        ProfilesResponse pr = crr.getProfiles();
                        profilesResponseData.put("city",pr.getCity());
                        profilesResponseData.put("country",pr.getCountry());
                        profilesResponseData.put("fullName",pr.getFullName());
                        profilesResponseData.put("firstName",pr.getFirstName());
                        profilesResponseData.put("middleName",pr.getMiddleName());
                        profilesResponseData.put("address",pr.getAddress());
                        profilesResponseData.put("convictionType",pr.getConvictionType());
                        profilesResponseData.put("countryCode",pr.getCountryCode());
                        profilesResponseData.put("countryName",pr.getCountryName());
                        profilesResponseData.put("dobOfBirth",pr.getDobOfBirth());
                        profilesResponseData.put("drivingLicenseVerificationResult",pr.getDrivingLicenseVerificationResult());
                        profilesResponseData.put("internalId",pr.getInternalId());
                        profilesResponseData.put("internalIdCriminalRecords",pr.getInternalIdCriminalRecords());
                        profilesResponseData.put("lastName",pr.getLastName());
                        profilesResponseData.put("photoUrl",pr.getPhotoUrl());
                        profilesResponseData.put("postalCode",pr.getPostalCode());
                        profilesResponseData.put("sex",pr.getSex());
                        profilesResponseData.put("source",pr.getSource());
                        profilesResponseData.put("state",pr.getState());
                        profilesResponseData.put("street1",pr.getStreet1());
                        profilesResponseData.put("street2",pr.getStreet2());
                        profilesResponseData.put("verificationResult",pr.getVerificationResult());
                        criminalRecordResponseData.put("profiles",profilesResponseData);
                    }catch(Exception e){}

                    hostDataResponseData.put("criminalRecord",criminalRecordResponseData);
                }catch(Exception e){}

                try {
                    NmResultResponse nrr = hdr.getNmresult();
                    nmResultResponseResponseData.put("createdOnNM",nrr.getCreatedOnNM());
                    nmResultResponseResponseData.put("orderIdNM",nrr.getOrderIdNM());
                    nmResultResponseResponseData.put("resultCountNM",nrr.getResultCountNM());
                    nmResultResponseResponseData.put("orderStatusNM",nrr.getOrderStatusNM());
                    nmResultResponseResponseData.put("orderUrlNM",nrr.getOrderUrlNM());
                    nmResultResponseResponseData.put("productIdNM",nrr.getProductIdNM());
                    nmResultResponseResponseData.put("vital4APIHostTried",nrr.getVital4APIHostTried());
                    hostDataResponseData.put("nmResult",nmResultResponseResponseData);
                }catch(Exception e){}

                try {
                    PepResultResponse prr = hdr.getPepresult();
                    pepResultResponseData.put("createdOnPEP",prr.getCreatedOnPEP());
                    pepResultResponseData.put("orderIdPEP",prr.getOrderIdPEP());
                    pepResultResponseData.put("resultCountPEP",prr.getResultCountPEP());
                    pepResultResponseData.put("productId_PEP",prr.getProductId_PEP());
                    pepResultResponseData.put("orderUrlPEP",prr.getOrderUrlPEP());
                    pepResultResponseData.put("orderStatus_PEP",prr.getOrderStatus_PEP());
                    hostDataResponseData.put("pepresult",pepResultResponseData);
                }catch(Exception e){}

                try {
                    TextMatchResultResponse tmrr = hdr.getTextMatchResult();
                    textMatchResultResponseData.put("address",tmrr.getAddress());
                    textMatchResultResponseData.put("addressCityMatch",tmrr.getAddressCityMatch());
                    textMatchResultResponseData.put("addressLine1Match",tmrr.getAddressLine1Match());
                    textMatchResultResponseData.put("addressLine2Match",tmrr.getAddressLine2Match());
                    textMatchResultResponseData.put("addressZIP4Match",tmrr.getAddressZIP4Match());
                    textMatchResultResponseData.put("addressStateCodeMatch",tmrr.getAddressStateCodeMatch());
                    textMatchResultResponseData.put("addressZIP5Match",tmrr.getAddressZIP5Match());
                    textMatchResultResponseData.put("documentCategoryMatch",tmrr.getDocumentCategoryMatch());
                    textMatchResultResponseData.put("driverLicenseExpirationDateMatch",tmrr.getDriverLicenseExpirationDateMatch());
                    textMatchResultResponseData.put("driverLicenseIssueDateMatch",tmrr.getDriverLicenseIssueDateMatch());
                    textMatchResultResponseData.put("driverLicenseNumberMatch",tmrr.getDriverLicenseNumberMatch());
                    textMatchResultResponseData.put("hostTried",tmrr.getHostTried());
                    textMatchResultResponseData.put("identiFraudHostTried",tmrr.getIdentiFraudHostTried());
                    textMatchResultResponseData.put("personBirthDateMatch",tmrr.getPersonBirthDateMatch());
                    textMatchResultResponseData.put("personFirstNameExactMatch",tmrr.getPersonFirstNameExactMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyAlternateMatch",tmrr.getPersonFirstNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personFirstNameFuzzyPrimaryMatch",tmrr.getPersonFirstNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personLastNameExactMatch",tmrr.getPersonLastNameExactMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyAlternateMatch",tmrr.getPersonLastNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personLastNameFuzzyPrimaryMatch",tmrr.getPersonLastNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personMiddleInitialMatch",tmrr.getPersonMiddleInitialMatch());
                    textMatchResultResponseData.put("personMiddleNameExactMatch",tmrr.getPersonMiddleNameExactMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyAlternateMatch",tmrr.getPersonMiddleNameFuzzyAlternateMatch());
                    textMatchResultResponseData.put("personMiddleNameFuzzyPrimaryMatch",tmrr.getPersonMiddleNameFuzzyPrimaryMatch());
                    textMatchResultResponseData.put("personSexCodeMatch",tmrr.getPersonSexCodeMatch());
                    textMatchResultResponseData.put("servicePresent",tmrr.getServicePresent());
                    textMatchResultResponseData.put("thirdPartyVerificationResultDescription",tmrr.getThirdPartyVerificationResultDescription());
                    textMatchResultResponseData.put("verificationResult",tmrr.getVerificationResult());
                    hostDataResponseData.put("textMatchResult",textMatchResultResponseData);
                }catch(Exception e){}

                try {
                    SexOffendersResponse sor = hdr.getSexOffenders();

                    try {
                        ProfilesResponse pr2 = sor.getProfiles();
                        profilesResponse2Data.put("city",pr2.getCity());
                        profilesResponse2Data.put("country",pr2.getCountry());
                        profilesResponse2Data.put("fullName",pr2.getFullName());
                        profilesResponse2Data.put("firstName",pr2.getFirstName());
                        profilesResponse2Data.put("middleName",pr2.getMiddleName());
                        profilesResponse2Data.put("address",pr2.getAddress());
                        profilesResponse2Data.put("convictionType",pr2.getConvictionType());
                        profilesResponse2Data.put("countryCode",pr2.getCountryCode());
                        profilesResponse2Data.put("countryName",pr2.getCountryName());
                        profilesResponse2Data.put("dobOfBirth",pr2.getDobOfBirth());
                        profilesResponse2Data.put("drivingLicenseVerificationResult",pr2.getDrivingLicenseVerificationResult());
                        profilesResponse2Data.put("internalId",pr2.getInternalId());
                        profilesResponse2Data.put("internalIdCriminalRecords",pr2.getInternalIdCriminalRecords());
                        profilesResponse2Data.put("lastName",pr2.getLastName());
                        profilesResponse2Data.put("photoUrl",pr2.getPhotoUrl());
                        profilesResponse2Data.put("postalCode",pr2.getPostalCode());
                        profilesResponse2Data.put("sex",pr2.getSex());
                        profilesResponse2Data.put("source",pr2.getSource());
                        profilesResponse2Data.put("state",pr2.getState());
                        profilesResponse2Data.put("street1",pr2.getStreet1());
                        profilesResponse2Data.put("street2",pr2.getStreet2());
                        profilesResponse2Data.put("verificationResult",pr2.getVerificationResult());
                        sexOffendersResponseData.put("profiles",profilesResponse2Data);
                    }catch(Exception e){}

                    hostDataResponseData.put("sexOffenders",sexOffendersResponseData);
                }catch(Exception e){}

                try {
                    WlsResultResponse wrr = hdr.getWlsresult();
                    wlsResultResponseData.put("createdOnWLS",wrr.getCreatedOnWLS());
                    wlsResultResponseData.put("orderIdWLS",wrr.getOrderIdWLS());
                    wlsResultResponseData.put("resultCountWLS",wrr.getResultCountWLS());
                    wlsResultResponseData.put("productIdWLS",wrr.getProductIdWLS());
                    wlsResultResponseData.put("orderStatusWLS",wrr.getOrderStatusWLS());
                    wlsResultResponseData.put("orderUrlWLS",wrr.getOrderUrlWLS());
                    hostDataResponseData.put("wlsresult",wlsResultResponseData);
                }catch(Exception e){}
                responseCustomerVerifyData.put("hostDataResponse",hostDataResponseData);
            }catch(Exception e){}

            jo.put("responseCustomerVerifyData", responseCustomerVerifyData);
        }catch(Exception e){}

        try {
            JSONObject additionalCustomerLiveCheckResponseData = new JSONObject();

            AdditionalCustomerLiveCheckResponseData aclcrd = response.getResult().getAdditionalData();
            additionalCustomerLiveCheckResponseData.put("liveFaceDetectionFlag",aclcrd.getLiveFaceDetectionFlag());

            jo.put("additionalCustomerLiveCheckResponseData", additionalCustomerLiveCheckResponseData);
        }catch(Exception e){}

        return jo.toString();
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
      super.handleOnActivityResult(requestCode, resultCode, data);
    }

}
