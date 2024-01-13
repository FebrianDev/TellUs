//
//  EmailLinkOpener.swift
//  iosApp
//
//  Created by Febrian on 14/01/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
extension UIApplication {
    @objc func openLink(_ link: String) {
        if let url = URL(string: link) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
}
