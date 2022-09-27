//
//  IdentitySdkPlugin.m
//  App
//
//  Created by Pranjal Lamba on 02/01/22.
//

#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>
CAP_PLUGIN(IdentitySdkPlugin, "IdentitySdkPlugin",
           CAP_PLUGIN_METHOD(getResult, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_init, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_serviceID20, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_serviceID10, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_serviceID50, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_serviceID175, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_serviceID105, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_serviceID185, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(idm_sdk_serviceID660, CAPPluginReturnPromise);
)
