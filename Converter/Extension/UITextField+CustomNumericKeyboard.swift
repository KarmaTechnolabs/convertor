//
//  UITextField+CustomNumericKeyboard.swift
// Converter
//
//  Created by karma on 25/01/2024.
//
import UIKit

private var numericKeyboardDelegate: CustomNumericKeyboardDelegate? = nil


// MARK: - This extension can be used to make UITextFields as a part of the custom numeric keyboard.
extension UITextField: CustomNumericKeyboardDelegate {
    
    /// This function sets the text field as a part of numeric keyboard.
    ///
    /// - Parameter delegate: The deligate
    func setAsNumericKeyboard(delegate: CustomNumericKeyboardDelegate?) {
        let numericKeyboard = CustomNumericKeyboard(frame: CGRect(x: 0, y: 0, width: 0, height: customNKbRecommendedHeight))
        self.inputView = numericKeyboard
        numericKeyboardDelegate = delegate
        numericKeyboard.delegate = self
    }
    
    
    /// This function unsets the text field as a part of numeric keyboard.
    func unsetAsNumericKeyboard() {
        if let numericKeyboard = self.inputView as? CustomNumericKeyboard {
            numericKeyboard.delegate = nil
        }
        self.inputView = nil
        numericKeyboardDelegate = nil
    }
    
    /// This function handles the numeric key press. It inserts the
    /// corresponding numeric value to the text field text.
    ///
    /// - Parameter key: The numeric key pressed.
    internal func numericKeyPressed(key: Int) {
        self.insertText(String(key))
        numericKeyboardDelegate?.numericKeyPressed(key: key)
    }
    
    
    /// This function handles the backspace key press. It removes a charater
    /// from the inserted text in the text field.
    internal func numericBackspacePressed() {
        self.deleteBackward()
        numericKeyboardDelegate?.numericBackspacePressed()
    }
    
    
    /// This function handles the symbol key press. It inserts the
    /// corresponding symbol to the text field text.
    ///
    /// - Parameter symbol: The symbol pressed.
    internal func numericSymbolPressed(symbol: String) {
        self.insertText(String(symbol))
        numericKeyboardDelegate?.numericSymbolPressed(symbol: symbol)
    }
    
    
    /// This function handles the retract keboard key press. It invokes
    /// the retractKeyPressed() method in the numericKeyboardDelegate.
    internal func retractKeyPressed() {
        numericKeyboardDelegate?.retractKeyPressed()
    }
}
