//
//  CookingConversionViewController.swift
//  Converter
//
//  Created by Karma on 11/03/24.


import UIKit

let COOKING_USER_DEFAULTS_KEY = "cooking"
private let COOKING_USER_DEFAULTS_MAX_COUNT = 5

class CookingConversionViewController: UIViewController, CustomNumericKeyboardDelegate   {

    @IBOutlet weak var cookingViewScroller: UIScrollView!
       @IBOutlet weak var outerStackView: UIStackView!
       @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
       @IBOutlet weak var mlTextField: UITextField!
       @IBOutlet weak var mlTextFieldStackView: UIStackView!
       @IBOutlet weak var tspnTextField: UITextField!
       @IBOutlet weak var tspnTextFieldStackView: UIStackView!
       @IBOutlet weak var tbspTextField: UITextField!
       @IBOutlet weak var tbspTextFieldStackView: UIStackView!
       @IBOutlet weak var cupTextField: UITextField!
       @IBOutlet weak var cupTextFieldStackView: UIStackView!
       @IBOutlet weak var flozTextField: UITextField!
       @IBOutlet weak var flozTextFieldStackView: UIStackView!

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
           mlTextField._lightPlaceholderColor(UIColor.lightText)
           mlTextField.setAsNumericKeyboard(delegate: self)

           tspnTextField._lightPlaceholderColor(UIColor.lightText)
           tspnTextField.setAsNumericKeyboard(delegate: self)

           tbspTextField._lightPlaceholderColor(UIColor.lightText)
           tbspTextField.setAsNumericKeyboard(delegate: self)

           cupTextField._lightPlaceholderColor(UIColor.lightText)
           cupTextField.setAsNumericKeyboard(delegate: self)

           flozTextField._lightPlaceholderColor(UIColor.lightText)
           flozTextField.setAsNumericKeyboard(delegate: self)

           // Add an observer to track keyboard show event
           NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)),
                                                  name: UIResponder.keyboardWillShowNotification, object: nil)
       }
    @objc func keyboardWillHide() {
            view.endEditing(true)

            UIView.animate(withDuration: 0.25, animations: {
                self.outerStackViewTopConstraint.constant = self.outerStackViewTopConstraintDefaultHeight
                self.view.layoutIfNeeded()
            })
        }

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
                    
                    var contentInset:UIEdgeInsets = self.cookingViewScroller.contentInset
                    contentInset.bottom = keyboard.size.height
                    cookingViewScroller.contentInset = contentInset
                }
            }
        }

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

        @IBAction func handleTextFieldChange(_ textField: UITextField) {
            var unit: CookingUnit?
            
            if textField.tag == 1 {
                unit = CookingUnit.ml
            } else if textField.tag == 2 {
                unit = CookingUnit.tspn
            } else if textField.tag == 3 {
                unit = CookingUnit.tbsp
            } else if textField.tag == 4 {
                unit = CookingUnit.cup
            } else if textField.tag == 5 {
                unit = CookingUnit.floz
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

        @IBAction func handleSaveButtonClick(_ sender: UIBarButtonItem) {
            if !isTextFieldsEmpty() {
                let conversion = "\(mlTextField.text!) ml = \(tspnTextField.text!) tspn = \(tbspTextField.text!) tbsp = \(cupTextField.text!) cup = \(flozTextField.text!) fl oz"
                
                var arr = UserDefaults.standard.array(forKey: COOKING_USER_DEFAULTS_KEY) as? [String] ?? []
                
                if arr.count >= COOKING_USER_DEFAULTS_MAX_COUNT {
                    arr = Array(arr.suffix(COOKING_USER_DEFAULTS_MAX_COUNT - 1))
                }
                arr.append(conversion)
                UserDefaults.standard.set(arr, forKey: COOKING_USER_DEFAULTS_KEY)
                
                let alert = UIAlertController(title: "Success", message: "The cooking conversion was successfully saved!", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            } else {
                let alert = UIAlertController(title: "Error", message: "You are trying to save an empty conversion!", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            }
        }

        func isTextFieldsEmpty() -> Bool {
            if !(mlTextField.text?.isEmpty)! && !(tspnTextField.text?.isEmpty)! &&
                !(tbspTextField.text?.isEmpty)! && !(cupTextField.text?.isEmpty)! && !(flozTextField.text?.isEmpty)! {
                return false
            }
            return true
        }

        func updateTextFields(textField: UITextField, unit: CookingUnit) -> Void {
            if let input = textField.text {
                if input.isEmpty {
                    clearTextFields()
                } else {
                    if let value = Double(input as String) {
                        let cookingMeasurement = CookingMeasurement(unit: unit, value: value)

                        for _unit in CookingUnit.allUnits {
                            if _unit == unit {
                                continue
                            }
                            let textField = mapUnitToTextField(unit: _unit)
                            let result = cookingMeasurement.convert(unit: _unit)

                            // Rounding off to 4 decimal places
                            let roundedResult = Double(round(10000 * result) / 10000)

                            textField.text = String(roundedResult)
                        }
                    }
                }
            }
        }

        func mapUnitToTextField(unit: CookingUnit) -> UITextField {
            var textField = mlTextField
            switch unit {
            case .ml:
                textField = mlTextField
            case .tspn:
                textField = tspnTextField
            case .tbsp:
                textField = tbspTextField
            case .cup:
                textField = cupTextField
            case .floz:
                textField = flozTextField
            }
            return textField!
        }

        func clearTextFields() {
            mlTextField.text = ""
            tspnTextField.text = ""
            tbspTextField.text = ""
            cupTextField.text = ""
            flozTextField.text = ""
        }
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
