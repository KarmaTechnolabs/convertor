//
//  EneryConvertionViewController.swift
//  Converter
//
//  Created by Karma on 06/03/24.
//  Copyright © 2024 Brion Silva. All rights reserved.
//

import UIKit

   let ENERGY_USER_DEFAULTS_KEY = "energy"
   private let ENERGY_USER_DEFAULTS_MAX_COUNT = 5
class EneryConvertionViewController: UIViewController,CustomNumericKeyboardDelegate {

            @IBOutlet weak var viewScroller: UIScrollView!
            @IBOutlet weak var outerStackView: UIStackView!
            @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
            @IBOutlet weak var jouleTextField: UITextField!
            @IBOutlet weak var jouleTextFieldStackView: UIStackView!
            @IBOutlet weak var kiloJouleTextField: UITextField!
            @IBOutlet weak var kiloJouleTextFieldStackView: UIStackView!
            @IBOutlet weak var kWHTextField: UITextField!
            @IBOutlet weak var kWHTextFieldStackView: UIStackView!
            @IBOutlet weak var calorieTextField: UITextField!
            @IBOutlet weak var calorieTextFieldStackView: UIStackView!
            @IBOutlet weak var electronVoltTextField: UITextField!
            @IBOutlet weak var electronVoltTextFieldStackView: UIStackView!
            @IBOutlet weak var tonTextField: UITextField!
            @IBOutlet weak var tonTextFieldStackView: UIStackView!
            
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
            jouleTextField._lightPlaceholderColor(UIColor.lightText)
            jouleTextField.setAsNumericKeyboard(delegate: self)
            
            kiloJouleTextField._lightPlaceholderColor(UIColor.lightText)
            kiloJouleTextField.setAsNumericKeyboard(delegate: self)
            
            kWHTextField._lightPlaceholderColor(UIColor.lightText)
            kWHTextField.setAsNumericKeyboard(delegate: self)
            
            calorieTextField._lightPlaceholderColor(UIColor.lightText)
            calorieTextField.setAsNumericKeyboard(delegate: self)
            
            electronVoltTextField._lightPlaceholderColor(UIColor.lightText)
            electronVoltTextField.setAsNumericKeyboard(delegate: self)
            
            tonTextField._lightPlaceholderColor(UIColor.lightText)
            tonTextField.setAsNumericKeyboard(delegate: self)
            
            // ad an observer to track keyboard show event
            NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)),
                                                   name: UIResponder.keyboardWillShowNotification, object: nil)
         }
        
        /// This function will be called by the tap gesture
        /// rrecognizer and will hide the keyboard and restore
        /// the top constrant back to default.
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
                    let keyboard:CGRect = info["UIKeyboardFrameEndUserInfoKey"] as! CGRect
                    
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
                    
                    var contentInset:UIEdgeInsets = self.viewScroller.contentInset
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
        
        /// This function is fired when the text fields are changed. This is a genric
        /// function and it Listens to the editing changed event of text fields.
        ///
        /// - Warning: The tags have been set in the storyboard to make the function generic.
        ///
        /// - Parameter textField: The changed text field.
        @IBAction func handleTextFieldChange(_ textField: UITextField) {
            var unit: EnergyUnit?
            
            if textField.tag == 1 {
                unit = EnergyUnit.joule
            } else if textField.tag == 2 {
                unit = EnergyUnit.kilojoule
            } else if textField.tag == 3 {
                unit = EnergyUnit.kilowattHour
            } else if textField.tag == 4 {
                unit = EnergyUnit.calorie
            } else if textField.tag == 5 {
                unit = EnergyUnit.electronVolt
            } else if textField.tag == 6 {
                unit = EnergyUnit.ton
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
                          let conversion = "\(jouleTextField.text!) joules = \(kiloJouleTextField.text!) kiloJoules = \(kWHTextField.text!) Kilowatthours = \(calorieTextField.text!) calories = \(electronVoltTextField.text!) electronVolts = \(tonTextField.text!) tons"
                          
                          var arr = UserDefaults.standard.array(forKey: TIME_USER_DEFAULTS_KEY) as? [String] ?? []
                          
                          if arr.count >= ENERGY_USER_DEFAULTS_MAX_COUNT {
                              arr = Array(arr.suffix(ENERGY_USER_DEFAULTS_MAX_COUNT - 1))
                          }
                          arr.append(conversion)
                          UserDefaults.standard.set(arr, forKey: ENERGY_USER_DEFAULTS_KEY)
                          
                          let alert = UIAlertController(title: "Success", message: "The time conversion was successfully saved!", preferredStyle: UIAlertController.Style.alert)
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
                  if !(jouleTextField.text?.isEmpty)! && !(kiloJouleTextField.text?.isEmpty)! &&
                      !(kWHTextField.text?.isEmpty)! && !(calorieTextField.text?.isEmpty)! &&
                      !(electronVoltTextField.text?.isEmpty)! && !(tonTextField.text?.isEmpty)! {
                      return false
                  }
                  return true
              }
     
        func updateTextFields(textField: UITextField, unit: EnergyUnit) -> Void {
            if let input = textField.text {
                if input.isEmpty {
                    clearTextFields()
                } else {
                    if let value = Double(input as String) {
                    let energy = Energy(unit: unit, value: value)
                    
                    for _unit in EnergyUnit.allUnits {
                        if _unit == unit {
                            continue
                        }
                        let textField = mapUnitToTextField(unit: _unit)
                        let result = energy.convert(unit: _unit)
                        
                        //rounding off to 4 decimal places
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
        func mapUnitToTextField(unit: EnergyUnit) -> UITextField {
            var textField = jouleTextField
            switch unit {
            case .joule:
                textField = jouleTextField
            case .kilojoule:
                textField = kiloJouleTextField
            case .kilowattHour:
                textField = kWHTextField
            case .calorie:
                textField = calorieTextField
            case .electronVolt:
                textField = electronVoltTextField
            case .ton:
                textField = tonTextField
            }
            return textField!
        }
        
        /// This function clears all the text fields
        func clearTextFields() {
            jouleTextField.text = ""
            kiloJouleTextField.text = ""
            kWHTextField.text = ""
            calorieTextField.text = ""
            electronVoltTextField.text = ""
            tonTextField.text = ""
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
