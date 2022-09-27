//
//  UserDefaults+IDentitySample.swift
//  IDentitySample
//
//  Created by Stefan Kaczmarek on 10/23/21.
//

import IDentitySDK_Swift

struct ServiceOptions: Codable {
    enum ManualReviewRequired: Int, Codable {
        case yes, no, forced
    }

    var manualReviewRequired: ManualReviewRequired
    var bypassAgeValidation: Bool
    var deDuplicationRequired: Bool
    var bypassNameMatching: Bool
    var postDataAPIRequired: Bool
    var sendInputImagesInPost: Bool
    var sendProcessedImagesInPost: Bool
    var needImmediateResponse: Bool
    var deduplicationSynchronous: Bool
    var verifyDataWithHost: Bool
    var idBackImageRequired: Bool
    var stripSpecialCharacters: Bool

    static var `default`: ServiceOptions {
        ServiceOptions(manualReviewRequired: .no,
                       bypassAgeValidation: false,
                       deDuplicationRequired: false,
                       bypassNameMatching: true,
                       postDataAPIRequired: false,
                       sendInputImagesInPost: false,
                       sendProcessedImagesInPost: false,
                       needImmediateResponse: false,
                       deduplicationSynchronous: false,
                       verifyDataWithHost: false,
                       idBackImageRequired: true,
                       stripSpecialCharacters: true)
    }
}

extension UserDefaults {
    static let defaultTemplateModelBaseURL = "https://demo.idmission.com/IDS/service/"
    static let defaultAPIBaseURL = "https://apidemo.idmission.com/"

    static let defaultLoginId = ""
    static let defaultPassword = ""
    static let defaultMerchantId = ""

    private static let templateModelBaseURLKey = "templateModelBaseURL"
    static var templateModelBaseURL: String {
        get { standard.string(forKey: templateModelBaseURLKey) ?? defaultTemplateModelBaseURL }
        set { standard.set(newValue, forKey: templateModelBaseURLKey) }
    }

    private static let apiBaseURLKey = "apiBaseURL"
    static var apiBaseURL: String {
        get { standard.string(forKey: apiBaseURLKey) ?? defaultAPIBaseURL }
        set { standard.set(newValue, forKey: apiBaseURLKey) }
    }

    private static let loginIdKey = "loginId"
    static var loginId: String {
        get { standard.string(forKey: loginIdKey) ?? defaultLoginId }
        set { standard.set(newValue, forKey: loginIdKey) }
    }

    private static let passwordKey = "password"
    static var password: String {
        get { standard.string(forKey: passwordKey) ?? defaultPassword }
        set { standard.set(newValue, forKey: passwordKey) }
    }

    private static let merchantIdKey = "merchantId"
    static var merchantId: String {
        get { standard.string(forKey: merchantIdKey) ?? defaultMerchantId }
        set { standard.set(newValue, forKey: merchantIdKey) }
    }

    // MARK: - Service Options

    private static let serviceOptionsKey = "serviceOptions"
    static var serviceOptions: ServiceOptions {
        get {
            if let data = standard.data(forKey: serviceOptionsKey),
                let options = try? JSONDecoder().decode(ServiceOptions.self, from: data) {
                return options
            } else {
                return .default
            }
        }
        set {
            guard let data = try? JSONEncoder().encode(newValue) else { return }
            standard.set(data, forKey: serviceOptionsKey)
        }
    }

    // MARK: -

    private static let debugModeKey = "debugMode"
    static var debugMode: Bool {
        get { standard.bool(forKey: debugModeKey) }
        set { standard.set(newValue, forKey: debugModeKey) }
    }

    private static let capture4KKey = "capture4K"
    static var capture4K: Bool {
        get { standard.bool(forKey: capture4KKey) }
        set { standard.set(newValue, forKey: capture4KKey) }
    }
}
