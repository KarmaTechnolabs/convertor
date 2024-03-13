//
//  AreaConversionViewController.swift
//  Converter
//
//  Created by Karma on 13/03/24.
//

import UIKit

let AREA_USER_DEFAULTS_KEY = "area"
private let AREA_USER_DEFAULTS_MAX_COUNT = 5

class AreaConversionViewController: UIViewController, CustomNumericKeyboardDelegate {

    @IBOutlet weak var viewScroller: UIScrollView!
        @IBOutlet weak var outerStackView: UIStackView!
        @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
        @IBOutlet weak var hectareTextField: UITextField!
        @IBOutlet weak var hectareTextFieldStackView: UIStackView!
        @IBOutlet weak var acreTextField: UITextField!
        @IBOutlet weak var acreTextFieldStackView: UIStackView!
        @IBOutlet weak var squareKilometerTextField: UITextField!
        @IBOutlet weak var squareKilometerTextFieldStackView: UIStackView!
        @IBOutlet weak var squareMeterTextField: UITextField!
        @IBOutlet weak var squareMeterTextFieldStackView: UIStackView!
        @IBOutlet weak var squareYardTextField: UITextField!
        @IBOutlet weak var squareYardTextFieldStackView: UIStackView!
        @IBOutlet weak var squareFootTextField: UITextField!
        @IBOutlet weak var squareFootTextFieldStackView: UIStackView!
        @IBOutlet weak var squareInchTextField: UITextField!
        @IBOutlet weak var squareInchTextFieldStackView: UIStackView!

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
            hectareTextField._lightPlaceholderColor(UIColor.lightText)
            hectareTextField.setAsNumericKeyboard(delegate: self)

            acreTextField._lightPlaceholderColor(UIColor.lightText)
            acreTextField.setAsNumericKeyboard(delegate: self)

            squareKilometerTextField._lightPlaceholderColor(UIColor.lightText)
            squareKilometerTextField.setAsNumericKeyboard(delegate: self)

            squareMeterTextField._lightPlaceholderColor(UIColor.lightText)
            squareMeterTextField.setAsNumericKeyboard(delegate: self)

            squareYardTextField._lightPlaceholderColor(UIColor.lightText)
            squareYardTextField.setAsNumericKeyboard(delegate: self)

            squareFootTextField._lightPlaceholderColor(UIColor.lightText)
            squareFootTextField.setAsNumericKeyboard(delegate: self)

            squareInchTextField._lightPlaceholderColor(UIColor.lightText)
            squareInchTextField.setAsNumericKeyboard(delegate: self)

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
            var unit: AreaUnit?

            if textField.tag == 1 {
                unit = AreaUnit.hectare
            } else if textField.tag == 2 {
                unit = AreaUnit.acre
            } else if textField.tag == 3 {
                unit = AreaUnit.squareKilometer
            } else if textField.tag == 4 {
                unit = AreaUnit.squareMeter
            } else if textField.tag == 5 {
                unit = AreaUnit.squareYard
            } else if textField.tag == 6 {
                unit = AreaUnit.squareFoot
            } else if textField.tag == 7 {
                unit = AreaUnit.squareInch
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
                let conversion = "\(hectareTextField.text!) hectares = \(acreTextField.text!) acres = \(squareKilometerTextField.text!) square kilometers = \(squareMeterTextField.text!) square meters = \(squareYardTextField.text!) square yards = \(squareFootTextField.text!) square feet = \(squareInchTextField.text!) square inches"

                var arr = UserDefaults.standard.array(forKey: AREA_USER_DEFAULTS_KEY) as? [String] ?? []

                if arr.count >= AREA_USER_DEFAULTS_MAX_COUNT {
                    arr = Array(arr.suffix(AREA_USER_DEFAULTS_MAX_COUNT - 1))
                }
                arr.append(conversion)
                UserDefaults.standard.set(arr, forKey: AREA_USER_DEFAULTS_KEY)

                let alert = UIAlertController(title: "Success", message: "The area conversion was successfully saved!", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            } else {
                let alert = UIAlertController(title: "Error", message: "You are trying to save an empty conversion!", preferredStyle: UIAlertController.Style.alert)
                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
            }
        }

        func isTextFieldsEmpty() -> Bool {
            if !(hectareTextField.text?.isEmpty)! && !(acreTextField.text?.isEmpty)! &&
                !(squareKilometerTextField.text?.isEmpty)! && !(squareMeterTextField.text?.isEmpty)! &&
                !(squareYardTextField.text?.isEmpty)! && !(squareFootTextField.text?.isEmpty)! &&
                !(squareInchTextField.text?.isEmpty)! {
                return false
            }
            return true
        }

        func updateTextFields(textField: UITextField, unit: AreaUnit) -> Void {
            if let input = textField.text {
                if input.isEmpty {
                    clearTextFields()
                } else {
                    if let value = Double(input as String) {
                        let area = Area(unit: unit, value: value)

                        for _unit in AreaUnit.allUnits {
                            if _unit == unit {
                                continue
                            }
                            let textField = mapUnitToTextField(unit: _unit)
                            let result = area.convert(unit: _unit)

                            // Rounding off to 4 decimal places
                            let roundedResult = Double(round(10000 * result) / 10000)

                            textField.text = String(roundedResult)
                        }
                    }
                }
            }
        }

        func mapUnitToTextField(unit: AreaUnit) -> UITextField {
            var textField = hectareTextField
            switch unit {
            case .hectare:
                textField = hectareTextField
            case .acre:
                textField = acreTextField
            case .squareKilometer:
                textField = squareKilometerTextField
            case .squareMeter:
                textField = squareMeterTextField
            case .squareYard:
                textField = squareYardTextField
            case .squareFoot:
                textField = squareFootTextField
            case .squareInch:
                textField = squareInchTextField
            }
            return textField!
        }

        func clearTextFields() {
            hectareTextField.text = ""
            acreTextField.text = ""
            squareKilometerTextField.text = ""
            squareMeterTextField.text = ""
            squareYardTextField.text = ""
            squareFootTextField.text = ""
            squareInchTextField.text = ""
        }

        func retractKeyPressed() {
            keyboardWillHide()
        }

        func numericKeyPressed(key: Int) {
            print("Numeric key \(key) pressed!")
        }

        func numericBackspacePressed() {
            print("Backspace pressed!")
        }

        func numericSymbolPressed(symbol: String) {
            print("Symbol \(symbol) pressed!")
        }
    }
