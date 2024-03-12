//
//  LengthConversionViewController.swift
//  Converter
//
//  Created by Karma on 11/03/24.


import UIKit


  let BLOOD_SUGAR_USER_DEFAULTS_KEY = "bloodSugar"
  private let BLOOD_SUGAR_USER_DEFAULTS_MAX_COUNT = 5


class BloodsugarConversionViewController: UIViewController,CustomNumericKeyboardDelegate {
        
        @IBOutlet weak var bloodSugarViewScroller: UIScrollView!
        @IBOutlet weak var outerStackView: UIStackView!
        @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
        @IBOutlet weak var mmolPerLTextField: UITextField!
        @IBOutlet weak var mmolPerLTextFieldStackView: UIStackView!
        @IBOutlet weak var mgPerdLTextField: UITextField!
        @IBOutlet weak var mgPerdLTextFieldStackView: UIStackView!
        
        var activeTextField = UITextField()
        var outerStackViewTopConstraintDefaultHeight: CGFloat = 17.0
        var textFieldKeyBoardGap = 20
        var keyBoardHeight:CGFloat = 0
        
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
            mmolPerLTextField._lightPlaceholderColor(UIColor.lightText)
            mmolPerLTextField.setAsNumericKeyboard(delegate: self)
            
            mgPerdLTextField._lightPlaceholderColor(UIColor.lightText)
            mgPerdLTextField.setAsNumericKeyboard(delegate: self)
            
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
        /// inorder to reach bottom text fields.
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
                    let keyboard:CGRect = info[UIResponder.keyboardFrameEndUserInfoKey] as! CGRect
                    
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
                    
                    var contentInset:UIEdgeInsets = self.bloodSugarViewScroller.contentInset
                    contentInset.bottom = keyboard.size.height
                    bloodSugarViewScroller.contentInset = contentInset
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
    /// function and it listens to the editing changed event of text fields.
    ///
    /// - Warning: The tags have been set in the storyboard to make the function generic.
    ///
    /// - Parameter textField: The changed text field.
    @IBAction func handleTextFieldChange(_ textField: UITextField) {
         var unit: BloodsugarUnit?
         
         if textField.tag == 1 {
             unit = BloodsugarUnit.mmolPerL
         } else if textField.tag == 2 {
             unit = BloodsugarUnit.mgPerdL
         }
         
         if unit != nil {
             updateTextFields(textField: textField, unit: unit!)
         }
     }
     
     /// This function checks if the text fields are empty.
     ///
     /// - Returns: True if the text fields are empty, false otherwise.
     func isTextFieldsEmpty() -> Bool {
         if !(mmolPerLTextField.text?.isEmpty)! && !(mgPerdLTextField.text?.isEmpty)! {
             return false
         }
         return true
     }

        /// This function handles the saving of conversions on to the userdefaults.
        /// Only 5 conversions will be saved under each type.
        /// It checks if the text fields are filled and saves the conversion in user
        /// defaults and if the text fields aren't empty an alert will be shown.
        ///
        /// - Parameter sender: The navigation button item.
        @IBAction func handleSaveButtonClick(_ sender: UIBarButtonItem) {
            if !isTextFieldsEmpty() {
                let conversion = "\(mmolPerLTextField.text!) mmol/L = \(mgPerdLTextField.text!) mg/dL"
                
                var arr = UserDefaults.standard.array(forKey: BLOOD_SUGAR_USER_DEFAULTS_KEY) as? [String] ?? []
                
                if arr.count >= BLOOD_SUGAR_USER_DEFAULTS_MAX_COUNT {
                    arr = Array(arr.suffix(BLOOD_SUGAR_USER_DEFAULTS_MAX_COUNT - 1))
                }
                arr.append(conversion)
                UserDefaults.standard.set(arr, forKey: BLOOD_SUGAR_USER_DEFAULTS_KEY)
                
                let alert = UIAlertController(title: "Success", message: "The blood sugar conversion was successully saved!", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            } else {
                let alert = UIAlertController(title: "Error", message: "You are trying to save an empty conversion!", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            }
        }
        
       
       
        /// This function modifies all the text fields accordingly based on the changed text field.
        ///
        /// Usage:
        ///
        ///     updateTextFields(textField: textField, unit: unit!)
        ///
        /// - Parameter textField: The changed text field.
        ///             unit: The temperature unit of the changed text field.
        func updateTextFields(textField: UITextField, unit: BloodsugarUnit) -> Void {
            if let input = textField.text {
                if input.isEmpty {
                    clearTextFields()
                } else {
                    if let value = Double(input as String) {
                        let bloodSugar = BloodsugarMeasure(unit: unit, value: value)
                        
                        for _unit in BloodsugarUnit.getAllUnits {
                            if _unit == unit {
                                continue
                            }
                            let textField = mapUnitToTextField(unit: _unit)
                            let result = bloodSugar.convert(unit: _unit)
                            
                            // Rounding off to 4 decimal places
                            let roundedResult = Double(round(10000 * result) / 10000)
                            
                            textField.text = String(roundedResult)
                        }
                    }
                }
            }
        }
        
        /// This function maps the respective blood sugar unit to the respective UITextField.
        ///
        /// Usage:
        ///
        ///     let textField = mapUnitToTextField(unit: _unit)
        ///
        /// - Parameter unit: The blood sugar unit.
        ///
        /// - Returns: A UITextField corresponding to the blood sugar unit.
        func mapUnitToTextField(unit: BloodsugarUnit) -> UITextField {
            var textField = mmolPerLTextField
            switch unit {
            case .mmolPerL:
                textField = mmolPerLTextField
            case .mgPerdL:
                textField = mgPerdLTextField
            }
            return textField!
        }
        
        /// This function clears all the text fields
        func clearTextFields() {
            mmolPerLTextField.text = ""
            mgPerdLTextField.text = ""
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
        /// and will be triggered when the symobol buttons are pressed on the custom keyboard.
        func numericSymbolPressed(symbol: String) {
            print("Symbol \(symbol) pressed!")
        }
    }

