//
//  IdentitySdkPlugin.swift
//  App
//
//  Created by Pranjal Lamba on 02/01/22.
//

import Foundation
import Capacitor

@objc(IdentitySdkPlugin)
public class IdentitySdkPlugin: CAPPlugin {
    
    @objc func getResult(_ call: CAPPluginCall) {
        call.reject("Reject")
    }
    
    @objc func idm_sdk_init(_ call: CAPPluginCall) {
        let loginId: String = call.getString("username") ?? UserDefaults.loginId
        UserDefaults.standard.set(String(loginId), forKey: "loginId")
        let password: String = call.getString("password") ?? UserDefaults.password
        UserDefaults.standard.set(String(password), forKey: "password")
        let merchantId: String = call.getString("merchantID") ?? UserDefaults.merchantId
        UserDefaults.standard.set(String(merchantId), forKey: "merchantId")
        IDentitySDKHelper().initializeSDK(call: call)
    }
    
    @objc func idm_sdk_serviceID20(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
          let instance: UIViewController = (self.bridge?.viewController!)!
          IDentitySDKHelper().startIDValidations(call: call, instance: instance);
        }
    }
    
    @objc func idm_sdk_serviceID10(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
          let instance: UIViewController = (self.bridge?.viewController!)!
          IDentitySDKHelper().startIDValidationAndMatchFaces(call: call, instance: instance);
        }
    }
    
    @objc func idm_sdk_serviceID50(_ call: CAPPluginCall) {
        let uniqueCustomerNumber: Int = call.getInt("uniqueCustomerNumber") ?? 0
        UserDefaults.standard.set(String(uniqueCustomerNumber), forKey: "uniqueCustomerNumber")
        DispatchQueue.main.async {
          let instance: UIViewController = (self.bridge?.viewController!)!
          IDentitySDKHelper().startIDValidationAndCustomerEnrolls(call: call, instance: instance);
        }
    }
    
    @objc func idm_sdk_serviceID175(_ call: CAPPluginCall) {
        let uniqueCustomerNumber: Int = call.getInt("uniqueCustomerNumber") ?? 0
        UserDefaults.standard.set(String(uniqueCustomerNumber), forKey: "uniqueCustomerNumber")
        DispatchQueue.main.async {
          let instance: UIViewController = (self.bridge?.viewController!)!
          IDentitySDKHelper().startCustomerEnrollBiometricss(call: call, instance: instance);
        }
    }
    
    @objc func idm_sdk_serviceID105(_ call: CAPPluginCall) {
        let uniqueCustomerNumber: Int = call.getInt("uniqueCustomerNumber") ?? 0
        UserDefaults.standard.set(String(uniqueCustomerNumber), forKey: "uniqueCustomerNumber")
        DispatchQueue.main.async {
          let instance: UIViewController = (self.bridge?.viewController!)!
          IDentitySDKHelper().startCustomerVerifications(call: call, instance: instance);
        }
    }
    
    @objc func idm_sdk_serviceID185(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
          let instance: UIViewController = (self.bridge?.viewController!)!
          IDentitySDKHelper().startIdentifyCustomers(call: call, instance: instance);
        }
    }
    
    @objc func idm_sdk_serviceID660(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
          let instance: UIViewController = (self.bridge?.viewController!)!
          IDentitySDKHelper().startLiveFaceChecks(call: call, instance: instance);
        }
    }
    
    @objc func sendResponseTo(call: CAPPluginCall, result: String) {
        call.resolve([
            "result": result
        ])
    }
}
