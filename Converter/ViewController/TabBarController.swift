//
//  TabBarController.swift
// Converter
//
//  Created by karma on 21/01/2024.
//

import UIKit

class TabBarController: UITabBarController, UITabBarControllerDelegate {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // instruct UITabBarController subclass to handle its own delegate methods
        self.delegate = self
    }
    
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        if viewController is UINavigationController {
            print("Conversions tab")
        } else if viewController is SaveViewController {
            print("Save tab")
        } else if viewController is HistoryViewController {
            print("History tab")
        } else if viewController is ConstantsViewController {
            print("Constants tab")
        }
    }
}

