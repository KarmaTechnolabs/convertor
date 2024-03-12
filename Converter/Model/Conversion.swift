//
//  Conversion.swift
//  UITextField+PlaceholderColor.swift
// Converter
//
//  Created by karma on 25/01/2024.
//

import UIKit

class Conversion {
    let name: String
    let icon: UIImage
    let segueID: String
    let cellColour: UIColor
    
    
    init(name: String, icon: UIImage, segueID: String, cellColour: UIColor) {
        self.name = name
        self.icon = icon
        self.segueID = segueID
        self.cellColour = cellColour
    }
    
    func getConversionName() -> String {
        return name
    }
    
    func getConversionIcon() -> UIImage {
        return icon
    }
    
    func getSegueID() -> String {
        return segueID
    }
    
    func getCellColour() -> UIColor {
        return cellColour
    }
}
