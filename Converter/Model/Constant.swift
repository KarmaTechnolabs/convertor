//
//  Constant.swift
// Converter
//
//  Created by karma on 15/02/2024.
//
import UIKit

class Constant {
    let name: String
    let formula: NSMutableAttributedString
    let icon: UIImage
    
    init(name: String, formula: NSMutableAttributedString, icon: UIImage) {
        self.name = name
        self.formula = formula
        self.icon = icon
    }
    
    func getName() -> String {
        return name
    }
    
    func getIcon() -> UIImage {
        return icon
    }
    
    func getFormula() -> NSMutableAttributedString {
        return formula
    }
}
