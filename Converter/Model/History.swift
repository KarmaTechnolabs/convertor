//
//  History.swift
//  UITextField+PlaceholderColor.swift
// Converter
//
//  Created by karma on 25/01/2024.

import UIKit

class History {
    let type: String
    let icon: UIImage
    let conversion: String
    
    init(type: String, icon: UIImage, conversion: String) {
        self.type = type
        self.icon = icon
        self.conversion = conversion
    }
    
    func getHistoryType() -> String {
        return type
    }
    
    func getHistoryIcon() -> UIImage {
        return icon
    }
    
    func getHistoryConversion() -> String {
        return conversion
    }
}
