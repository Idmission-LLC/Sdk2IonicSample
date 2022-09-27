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
      loginID = call.getString("username").toString();
      password = call.getString("password").toString();
      merchantID = (long)call.getInt("merchantID");
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
        jo.put("AdditionalData", response.getResult().getAdditionalData());
      }catch(Exception e){}
      try {
        JSONObject responseCustomerData = new JSONObject();
        JSONObject extractedIdData = new JSONObject();
        JSONObject extractedPersonalData = new JSONObject();

        ResponseCustomerData rcd = response.getResult().getResponseCustomerData();

        ExtractedIdData eid = rcd.getExtractedIdData();
        extractedIdData.put("BarcodeDataParsed",eid.getBarcodeDataParsed());
        extractedIdData.put("IdCountry",eid.getIdCountry());
        extractedIdData.put("IdDateOfBirth",eid.getIdDateOfBirth());
        extractedIdData.put("IdDateOfBirthFormatted",eid.getIdDateOfBirthFormatted());
        extractedIdData.put("IdDateOfBirthNonEng",eid.getIdDateOfBirthNonEng());
        extractedIdData.put("IdExpirationDate",eid.getIdExpirationDate());
        extractedIdData.put("IdExpirationDateFormatted",eid.getIdExpirationDateFormatted());
        extractedIdData.put("IdExpirationDateNonEng",eid.getIdExpirationDateNonEng());
        extractedIdData.put("IdIssueCountry",eid.getIdIssueCountry());
        extractedIdData.put("IdIssueDate",eid.getIdIssueDate());
        extractedIdData.put("IdIssueDateNonEng",eid.getIdIssueDateNonEng());
        extractedIdData.put("IdNumber",eid.getIdNumber());
        extractedIdData.put("IdNumberNonEng",eid.getIdNumberNonEng());
        extractedIdData.put("IdNumber1",eid.getIdNumber1());
        extractedIdData.put("IdNumber2",eid.getIdNumber2());
        extractedIdData.put("IdNumber2NonEng",eid.getIdNumber2NonEng());
        extractedIdData.put("IdNumber3",eid.getIdNumber3());
        extractedIdData.put("IdState",eid.getIdState());
        extractedIdData.put("IdType",eid.getIdType());
        extractedIdData.put("MrzData",eid.getMrzData());

        responseCustomerData.put("ExtractedIdData",extractedIdData);

        ExtractedPersonalData epd = rcd.getExtractedPersonalData();
        extractedPersonalData.put("AddressLine1",epd.getAddressLine1());
        extractedPersonalData.put("AddressLine1NonEng",epd.getAddressLine1NonEng());
        extractedPersonalData.put("AddressLine2",epd.getAddressLine2());
        extractedPersonalData.put("AddressLine2NonEng",epd.getAddressLine2NonEng());
        extractedPersonalData.put("City",epd.getCity());
        extractedPersonalData.put("AddressNonEng",epd.getAddressNonEng());
        extractedPersonalData.put("Country",epd.getCountry());
        extractedPersonalData.put("District",epd.getDistrict());
        extractedPersonalData.put("Dob",epd.getDob());
        extractedPersonalData.put("Email",epd.getEmail());
        extractedPersonalData.put("EnrolledDate",epd.getEnrolledDate());
        extractedPersonalData.put("FirstName",epd.getFirstName());
        extractedPersonalData.put("FirstNameNonEng",epd.getFirstNameNonEng());
        extractedPersonalData.put("Gender",epd.getGender());
        extractedPersonalData.put("LastName",epd.getLastName());
        extractedPersonalData.put("LastName2",epd.getLastName2());
        extractedPersonalData.put("LastNameNonEng",epd.getLastNameNonEng());
        extractedPersonalData.put("Name",epd.getName());
        extractedPersonalData.put("Phone",epd.getPhone());
        extractedPersonalData.put("UniqueNumber",epd.getUniqueNumber());
        extractedPersonalData.put("MiddleName",epd.getMiddleName());
        extractedPersonalData.put("MiddleNameNonEng",epd.getMiddleNameNonEng());

        responseCustomerData.put("ExtractedPersonalData",extractedPersonalData);

        HostDataResponse hdr = rcd.getHostData();
        responseCustomerData.put("HostDataResponse",hdr);

        jo.put("ResponseCustomerData", responseCustomerData);
      }catch(Exception e){}
      try {
        JSONObject responseCustomerVerifyData = new JSONObject();
        JSONObject extractedIdData = new JSONObject();
        JSONObject extractedPersonalData = new JSONObject();

        ResponseCustomerData rcvd = response.getResult().getResponseCustomerVerifyData();

        ExtractedIdData eid = rcvd.getExtractedIdData();
        extractedIdData.put("BarcodeDataParsed",eid.getBarcodeDataParsed());
        extractedIdData.put("IdCountry",eid.getIdCountry());
        extractedIdData.put("IdDateOfBirth",eid.getIdDateOfBirth());
        extractedIdData.put("IdDateOfBirthFormatted",eid.getIdDateOfBirthFormatted());
        extractedIdData.put("IdDateOfBirthNonEng",eid.getIdDateOfBirthNonEng());
        extractedIdData.put("IdExpirationDate",eid.getIdExpirationDate());
        extractedIdData.put("IdExpirationDateFormatted",eid.getIdExpirationDateFormatted());
        extractedIdData.put("IdExpirationDateNonEng",eid.getIdExpirationDateNonEng());
        extractedIdData.put("IdIssueCountry",eid.getIdIssueCountry());
        extractedIdData.put("IdIssueDate",eid.getIdIssueDate());
        extractedIdData.put("IdIssueDateNonEng",eid.getIdIssueDateNonEng());
        extractedIdData.put("IdNumber",eid.getIdNumber());
        extractedIdData.put("IdNumberNonEng",eid.getIdNumberNonEng());
        extractedIdData.put("IdNumber1",eid.getIdNumber1());
        extractedIdData.put("IdNumber2",eid.getIdNumber2());
        extractedIdData.put("IdNumber2NonEng",eid.getIdNumber2NonEng());
        extractedIdData.put("IdNumber3",eid.getIdNumber3());
        extractedIdData.put("IdState",eid.getIdState());
        extractedIdData.put("IdType",eid.getIdType());
        extractedIdData.put("MrzData",eid.getMrzData());

        responseCustomerVerifyData.put("ExtractedIdData",extractedIdData);

        ExtractedPersonalData epd = rcvd.getExtractedPersonalData();
        extractedPersonalData.put("AddressLine1",epd.getAddressLine1());
        extractedPersonalData.put("AddressLine1NonEng",epd.getAddressLine1NonEng());
        extractedPersonalData.put("AddressLine2",epd.getAddressLine2());
        extractedPersonalData.put("AddressLine2NonEng",epd.getAddressLine2NonEng());
        extractedPersonalData.put("City",epd.getCity());
        extractedPersonalData.put("AddressNonEng",epd.getAddressNonEng());
        extractedPersonalData.put("Country",epd.getCountry());
        extractedPersonalData.put("District",epd.getDistrict());
        extractedPersonalData.put("Dob",epd.getDob());
        extractedPersonalData.put("Email",epd.getEmail());
        extractedPersonalData.put("EnrolledDate",epd.getEnrolledDate());
        extractedPersonalData.put("FirstName",epd.getFirstName());
        extractedPersonalData.put("FirstNameNonEng",epd.getFirstNameNonEng());
        extractedPersonalData.put("Gender",epd.getGender());
        extractedPersonalData.put("LastName",epd.getLastName());
        extractedPersonalData.put("LastName2",epd.getLastName2());
        extractedPersonalData.put("LastNameNonEng",epd.getLastNameNonEng());
        extractedPersonalData.put("Name",epd.getName());
        extractedPersonalData.put("Phone",epd.getPhone());
        extractedPersonalData.put("UniqueNumber",epd.getUniqueNumber());
        extractedPersonalData.put("MiddleName",epd.getMiddleName());
        extractedPersonalData.put("MiddleNameNonEng",epd.getMiddleNameNonEng());

        responseCustomerVerifyData.put("ExtractedPersonalData",extractedPersonalData);

        HostDataResponse hdr = rcvd.getHostData();
        responseCustomerVerifyData.put("HostDataResponse",hdr);

        jo.put("ResponseCustomerVerifyData", responseCustomerVerifyData);
      }catch(Exception e){}
      try {
        jo.put("ResultData", response.getResult().getResultData());
      }catch(Exception e){}
      try {
        jo.put("Status", response.getResult().getStatus());
      }catch(Exception e){}

      return jo.toString();
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
      super.handleOnActivityResult(requestCode, resultCode, data);
    }

}
