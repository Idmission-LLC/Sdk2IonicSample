//
//  IDentitySDKHelper.swift
//  IDMissionSDK2_React
//
//  Created by Pranjal Lamba on 29/11/21.
//

import Foundation
import IDentitySDK_Swift
import IDCapture_Swift
import SelfieCapture_Swift
import Capacitor

class IDentitySDKHelper : NSObject{
  
    @IBAction func initializeSDK(call: CAPPluginCall){

    let loginId = UserDefaults.standard.string(forKey: "loginId") ?? ""
    let password = UserDefaults.standard.string(forKey: "password") ?? ""
    let merchantId = UserDefaults.standard.string(forKey: "merchantId") ?? ""
    IDentitySDK.templateModelBaseURL = UserDefaults.templateModelBaseURL
    IDentitySDK.apiBaseURL = UserDefaults.apiBaseURL
    IDentitySDK.initializeSDK(loginId: loginId, password: password, merchantId: merchantId) { error in
        if let error = error {
            print(error.localizedDescription)
            let iDMissionSDK = IdentitySdkPlugin()
            iDMissionSDK.sendResponseTo(call: call, result: error.localizedDescription)
        } else {
            print("SDK successfully initialized")
            let iDMissionSDK = IdentitySdkPlugin()
            iDMissionSDK.sendResponseTo(call: call, result: "SDK successfully initialized")
        }
    }
  }
  
  // 20 - ID Validation
    @IBAction func startIDValidations(call: CAPPluginCall, instance: UIViewController) {
        ViewController().startIDValidation(call: call, instance: instance);
  }
 
  // 10 - ID Validation and Match Face
  @IBAction func startIDValidationAndMatchFaces(call: CAPPluginCall, instance: UIViewController) {
    ViewController().startIDValidationAndMatchFace(call: call, instance: instance);
  }
  
  // 50 - ID Validation And Customer Enroll
  @IBAction func startIDValidationAndCustomerEnrolls(call: CAPPluginCall, instance: UIViewController) {
    ViewController().startIDValidationAndCustomerEnroll(call: call, instance: instance);
  }
  
  // 175 - Customer Enroll Biometrics
  @IBAction func startCustomerEnrollBiometricss(call: CAPPluginCall, instance: UIViewController) {
    ViewController().startCustomerEnrollBiometrics(call: call, instance: instance);
  }
  
  // 105 - Customer Verification
  @IBAction func startCustomerVerifications(call: CAPPluginCall, instance: UIViewController) {
    ViewController().startCustomerVerification(call: call, instance: instance);
  }
  
  // 185 - Identify Customer
    @IBAction func startIdentifyCustomers(call: CAPPluginCall, instance: UIViewController) {
        ViewController().startIdentifyCustomer(call: call, instance: instance);
  }
  
  // 660 - Live Face Check
    @IBAction func startLiveFaceChecks(call: CAPPluginCall, instance: UIViewController) {
        ViewController().startLiveFaceCheck(call: call, instance: instance);
  }
}
