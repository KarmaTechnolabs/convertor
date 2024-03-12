//
//  DataConversionViewController.swift
//  Converter
//
//  Created by Karma on 11/03/24.
//  Copyright © 2024 Brion Silva. All rights reserved.
//


import UIKit

let DATA_USER_DEFAULTS_KEY = "data"
private let DATA_USER_DEFAULTS_MAX_COUNT = 5
class DataConversionViewController: UIViewController, CustomNumericKeyboardDelegate  {
   
    
        @IBOutlet weak var viewScroller: UIScrollView!
        @IBOutlet weak var outerStackView: UIStackView!
        @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
        @IBOutlet weak var bitTextField: UITextField!
        @IBOutlet weak var bitTextFieldStackView: UIStackView!
        @IBOutlet weak var byteTextField: UITextField!
        @IBOutlet weak var byteTextFieldStackView: UIStackView!
        @IBOutlet weak var kbTextField: UITextField!
        @IBOutlet weak var kbTextFieldStackView: UIStackView!
        @IBOutlet weak var mbTextField: UITextField!
        @IBOutlet weak var mbTextFieldStackView: UIStackView!
        @IBOutlet weak var gbTextField: UITextField!
        @IBOutlet weak var gbTextFieldStackView: UIStackView!
        @IBOutlet weak var tbTextField: UITextField!
        @IBOutlet weak var tbTextFieldStackView: UIStackView!

        var activeTextField = UITextField()
        var outerStackViewTopConstraintDefaultHeight: CGFloat = 17.0
        var textFieldKeyBoardGap = 20
        var keyBoardHeight: CGFloat = 0

        override func viewDidLoad() {
            super.viewDidLoad()

            self.view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(keyboardWillHide)))

            if isTextFieldsEmpty() {
                self.navigationItem.rightBarButtonItem!.isEnabled = false;
            }
        }

        override func viewWillAppear(_ animated: Bool) {
            super.viewWillAppear(animated)

            // Set Text Field Styles
            bitTextField._lightPlaceholderColor(UIColor.lightText)
            bitTextField.setAsNumericKeyboard(delegate: self)

            byteTextField._lightPlaceholderColor(UIColor.lightText)
            byteTextField.setAsNumericKeyboard(delegate: self)

            kbTextField._lightPlaceholderColor(UIColor.lightText)
            kbTextField.setAsNumericKeyboard(delegate: self)

            mbTextField._lightPlaceholderColor(UIColor.lightText)
            mbTextField.setAsNumericKeyboard(delegate: self)

            gbTextField._lightPlaceholderColor(UIColor.lightText)
            gbTextField.setAsNumericKeyboard(delegate: self)

            tbTextField._lightPlaceholderColor(UIColor.lightText)
            tbTextField.setAsNumericKeyboard(delegate: self)

            // Add an observer to track keyboard show event
            NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)),
                                                   name: UIResponder.keyboardWillShowNotification, object: nil)
        }

        /// This function will be called by the tap gesture
        /// recognizer and will hide the keyboard and restore
        /// the top constraint back to default.
        @objc func keyboardWillHide() {
            view.endEditing(true)

            UIView.animate(withDuration: 0.25, animations: {
                self.outerStackViewTopConstraint.constant = self.outerStackViewTopConstraintDefaultHeight
                self.view.layoutIfNeeded()
            })
        }

        /// This function will recognize the first responder
        /// and will adjust the ui text field position according
        /// to the height of the keyboard. The scroll will is adjusted
        /// in order to reach bottom text fields.
        ///
        /// Usage:
        ///
        ///     keyboardWillShow(notification:)
        ///
        /// - Parameter NSNotification: notification.
        @objc func keyboardWillShow(notification: NSNotification) {

            let firstResponder = self.findFirstResponder(inView: self.view)

            if firstResponder != nil {
                activeTextField = firstResponder as! UITextField;

                let activeTextFieldSuperView = activeTextField.superview!

                if let info = notification.userInfo {
                    let keyboard: CGRect = info[UIResponder.keyboardFrameEndUserInfoKey] as! CGRect

                    let targetY = view.frame.size.height - keyboard.height - 15 - activeTextField.frame.size.height

                    let initialY = outerStackView.frame.origin.y + activeTextFieldSuperView.frame.origin.y + activeTextField.frame.origin.y

                    if initialY > targetY {
                        let diff = targetY - initialY
                        let targetOffsetForTopConstraint = outerStackViewTopConstraint.constant + diff
                        self.view.layoutIfNeeded()

                        UIView.animate(withDuration: 0.25, animations: {
                            self.outerStackViewTopConstraint.constant = targetOffsetForTopConstraint
                            self.view.layoutIfNeeded()
                        })
                    }

                    var contentInset: UIEdgeInsets = self.viewScroller.contentInset
                    contentInset.bottom = keyboard.size.height
                    viewScroller.contentInset = contentInset
                }
            }
        }

        /// This function finds the first responder in a UIView.
        ///
        /// Usage:
        ///
        ///     let firstResponder = self.findFirstResponder(inView: self.view)
        ///
        /// - Parameter inView: The corresponding UIView.
        ///
        /// - Returns: A UIView or a subview will be returned.
    func findFirstResponder(inView view: UIView) -> UIView? {
           for subView in view.subviews {
               if subView.isFirstResponder {
                   return subView
               }

               if let recursiveSubView = self.findFirstResponder(inView: subView) {
                   return recursiveSubView
               }
           }

           return nil
       }

       /// This function is fired when the text fields are changed. This is a generic
       /// function and it Listens to the editing changed event of text fields.
       ///
       /// - Warning: The tags have been set in the storyboard to make the function generic.
       ///
       /// - Parameter textField: The changed text field.
       @IBAction func handleTextFieldChange(_ textField: UITextField) {
           var unit: DataUnit?

           if textField.tag == 1 {
               unit = DataUnit.bit
           } else if textField.tag == 2 {
               unit = DataUnit.byte
           } else if textField.tag == 3 {
               unit = DataUnit.kilobyte
           } else if textField.tag == 4 {
               unit = DataUnit.megabyte
           } else if textField.tag == 5 {
               unit = DataUnit.gigabyte
           } else if textField.tag == 6 {
               unit = DataUnit.terabyte
           }

           if unit != nil {
               updateTextFields(textField: textField, unit: unit!)
           }

           if isTextFieldsEmpty() {
               self.navigationItem.rightBarButtonItem!.isEnabled = false;
           } else {
               self.navigationItem.rightBarButtonItem!.isEnabled = true;
           }
       }

       /// This function handles the saving of conversions on to the userdefaults.
       /// Only 5 conversions will be saved under each type.
       /// It checks if the text fields are filled and saves the conversion in user
       /// defaults and if the text fields aren't empty an alert will be shown.
       ///
       /// - Parameter sender: The navigation button item.
       @IBAction func handleSaveButtonClick(_ sender: UIBarButtonItem) {
           if !isTextFieldsEmpty() {
               let conversion = "\(bitTextField.text!) bits = \(byteTextField.text!) bytes = \(kbTextField.text!) KB = \(mbTextField.text!) MB = \(gbTextField.text!) GB = \(tbTextField.text!) TB"

               var arr = UserDefaults.standard.array(forKey: DATA_USER_DEFAULTS_KEY) as? [String] ?? []

               if arr.count >= DATA_USER_DEFAULTS_MAX_COUNT {
                   arr = Array(arr.suffix(DATA_USER_DEFAULTS_MAX_COUNT - 1))
               }
               arr.append(conversion)
               UserDefaults.standard.set(arr, forKey: DATA_USER_DEFAULTS_KEY)

               let alert = UIAlertController(title: "Success", message: "The data conversion was successfully saved!", preferredStyle: UIAlertController.Style.alert)
               alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
               self.present(alert, animated: true, completion: nil)
           } else {
               let alert = UIAlertController(title: "Error", message: "You are trying to save an empty conversion!", preferredStyle: UIAlertController.Style.alert)
               alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
               self.present(alert, animated: true, completion: nil)
           }
       }

       /// This is a reusable utility function to check if the text fields are empty.
       ///
       /// - Warning: This function needs to be changed when a new text field is added or removed.
       ///
       /// Usage:
       ///
       ///     if isTextFieldsEmpty() // true | false
       ///
       /// - Returns: true or false
       func isTextFieldsEmpty() -> Bool {
           if !(bitTextField.text?.isEmpty)! && !(byteTextField.text?.isEmpty)! &&
               !(kbTextField.text?.isEmpty)! && !(mbTextField.text?.isEmpty)! &&
               !(gbTextField.text?.isEmpty)! && !(tbTextField.text?.isEmpty)! {
               return false
           }
           return true
       }

       /// This function modifies all the text fields accordingly based on the changed text field.
       ///
       /// Usage:
       ///
       ///     updateTextFields(textField: textField, unit: unit!)
       ///
       /// - Parameter textField: The changed text field.
       ///             unit: The volume unit of the changed text field.
       func updateTextFields(textField: UITextField, unit: DataUnit) -> Void {
           if let input = textField.text {
               if input.isEmpty {
                   clearTextFields()
               } else {
                   if let value = Double(input as String) {
                       let data = DataSize(unit: unit, value: value)

                       for _unit in DataUnit.allUnits {
                           if _unit == unit {
                               continue
                           }
                           let textField = mapUnitToTextField(unit: _unit)
                           let result = data.convert(unit: _unit)

                           // rounding off to 4 decimal places
                           let roundedResult = Double(round(10000 * result) / 10000)

                           textField.text = String(roundedResult)
                       }
                   }
               }
           }
       }

       /// This function maps the respective volume unit to the respective UITextField.
       ///
       /// Usage:
       ///
       ///     let textField = mapUnitToTextField(unit: _unit)
       ///
       /// - Parameter unit: The volume unit.
       ///
       /// - Returns: A UITextField corresponding to the volume unit.
       func mapUnitToTextField(unit: DataUnit) -> UITextField {
           var textField = bitTextField
           switch unit {
           case .bit:
               textField = bitTextField
           case .byte:
               textField = byteTextField
           case .kilobyte:
               textField = kbTextField
           case .megabyte:
               textField = mbTextField
           case .gigabyte:
               textField = gbTextField
           case .terabyte:
               textField = tbTextField
           }
           return textField!
       }

       /// This function clears all the text fields
       func clearTextFields() {
           bitTextField.text = ""
           byteTextField.text = ""
           kbTextField.text = ""
           mbTextField.text = ""
           gbTextField.text = ""
           tbTextField.text = ""
       }

       /// This function is a part of the CustomNumericKeyboardDelegate interface
       /// and will be triggered when the retract button is pressed on the custom keyboard.
       func retractKeyPressed() {
           keyboardWillHide()
       }

       /// This function is a part of the CustomNumericKeyboardDelegate interface
       /// and will be triggered when the numeric buttons are pressed on the custom keyboard.
       func numericKeyPressed(key: Int) {
           print("Numeric key \(key) pressed!")
       }

       /// This function is a part of the CustomNumericKeyboardDelegate interface
       /// and will be triggered when the backspace button is pressed on the custom keyboard.
       func numericBackspacePressed() {
           print("Backspace pressed!")
       }

       /// This function is a part of the CustomNumericKeyboardDelegate interface
       /// and will be triggered when the symbol buttons are pressed on the custom keyboard.
       func numericSymbolPressed(symbol: String) {
           print("Symbol \(symbol) pressed!")
       }
   }

