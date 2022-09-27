package io.ionic.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.idmission.sdk2.capture.IdMissionCaptureLauncher;
import com.idmission.sdk2.capture.presentation.camera.helpers.ProcessedCapture;
import com.idmission.sdk2.identityproofing.IdentityProofingSDK;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CallPluginActivity extends Activity {

  private List<ProcessedCapture> processedCaptures = new ArrayList<>();
  public static String result;
  private IdMissionCaptureLauncher launcher = new IdMissionCaptureLauncher();
  boolean resultDisplayed = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int serviceID = getIntent().getIntExtra("serviceID", 20);
    if(serviceID==20){
      IdentityProofingSDK.INSTANCE.idValidation(CallPluginActivity.this);
    }else if(serviceID==10){
      IdentityProofingSDK.INSTANCE.idValidationAndMatchFace(CallPluginActivity.this);
    }else if(serviceID==185){
      IdentityProofingSDK.INSTANCE.identifyCustomer(CallPluginActivity.this, null, null);
    }else if(serviceID==660){
      IdentityProofingSDK.INSTANCE.liveFaceCheck(CallPluginActivity.this);
    }else if(serviceID==50){
      String uniqueCustomerNumber = getIntent().getStringExtra("uniqueCustomerNumber");
      IdentityProofingSDK.INSTANCE.idValidationAndcustomerEnroll(CallPluginActivity.this, uniqueCustomerNumber);
    }else if(serviceID==175){
      String uniqueCustomerNumber = getIntent().getStringExtra("uniqueCustomerNumber");
      IdentityProofingSDK.INSTANCE.customerEnrollBiometrics(CallPluginActivity.this, uniqueCustomerNumber);
    }else if(serviceID==105){
      String uniqueCustomerNumber = getIntent().getStringExtra("uniqueCustomerNumber");
      IdentityProofingSDK.INSTANCE.customerVerification(CallPluginActivity.this, uniqueCustomerNumber);
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data != null) {
      if (requestCode == IdMissionCaptureLauncher.CAPTURE_REQUEST_CODE) {
        try{
          Parcelable[] processedCaptures = data.getExtras().getParcelableArray(IdMissionCaptureLauncher.EXTRA_PROCESSED_CAPTURES);

          JSONObject jo = new JSONObject();
          jo.put("Image1", processedCaptures[0].toString());
          if(processedCaptures.length>1){
            jo.put("Image2", processedCaptures[1].toString());
          }
          if(processedCaptures.length>2){
            jo.put("Image3", processedCaptures[2].toString());
          }
          if(processedCaptures.length>3){
            jo.put("Image4", processedCaptures[3].toString());
          }

          result = jo.toString();
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    }
    onBackPressed();
  }
}
