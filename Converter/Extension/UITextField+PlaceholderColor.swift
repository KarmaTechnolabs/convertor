//
//  UITextField+PlaceholderColor.swift
// Converter
//
//  Created by karma on 25/01/2024.
//
import UIKit


// MARK: - This extension can be used to make the placeholder in UIText field to be lighter.
extension UITextField {
    
    
    /// This function sets the passed in color to the text field placeholder.
    ///
    /// - Parameter color: The colour to be set.
    func _lightPlaceholderColor(_ color: UIColor) {
        var placeholderText = ""
        if self.placeholder != nil{
            placeholderText = self.placeholder!
        }
        self.attributedPlaceholder = NSAttributedString(string: placeholderText, attributes: [NSAttributedString.Key.foregroundColor : color])
    }
}
