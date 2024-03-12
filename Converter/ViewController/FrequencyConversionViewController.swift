//
//  FrequencyConversionViewController.swift
//  Converter
//
//  Created by Karma on 11/03/24.


import UIKit

let FREQUENCY_USER_DEFAULTS_KEY = "frequency"
private let FREQUENCY_USER_DEFAULTS_MAX_COUNT = 5

class FrequencyConversionViewController: UIViewController, CustomNumericKeyboardDelegate {

    @IBOutlet weak var frequencyViewScroller: UIScrollView!
        @IBOutlet weak var outerStackView: UIStackView!
        @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
        @IBOutlet weak var hzTextField: UITextField!
        @IBOutlet weak var hzTextFieldStackView: UIStackView!
        @IBOutlet weak var ehzTextField: UITextField!
        @IBOutlet weak var ehzTextFieldStackView: UIStackView!
        @IBOutlet weak var phzTextField: UITextField!
        @IBOutlet weak var phzTextFieldStackView: UIStackView!
        @IBOutlet weak var thzTextField: UITextField!
        @IBOutlet weak var thzTextFieldStackView: UIStackView!
        @IBOutlet weak var ghzTextField: UITextField!
        @IBOutlet weak var ghzTextFieldStackView: UIStackView!
        @IBOutlet weak var mhzTextField: UITextField!
        @IBOutlet weak var mhzTextFieldStackView: UIStackView!
        @IBOutlet weak var khzTextField: UITextField!
        @IBOutlet weak var khzTextFieldStackView: UIStackView!

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
            hzTextField._lightPlaceholderColor(UIColor.lightText)
            hzTextField.setAsNumericKeyboard(delegate: self)

            ehzTextField._lightPlaceholderColor(UIColor.lightText)
            ehzTextField.setAsNumericKeyboard(delegate: self)

            phzTextField._lightPlaceholderColor(UIColor.lightText)
            phzTextField.setAsNumericKeyboard(delegate: self)

            thzTextField._lightPlaceholderColor(UIColor.lightText)
            thzTextField.setAsNumericKeyboard(delegate: self)

            ghzTextField._lightPlaceholderColor(UIColor.lightText)
            ghzTextField.setAsNumericKeyboard(delegate: self)

            mhzTextField._lightPlaceholderColor(UIColor.lightText)
            mhzTextField.setAsNumericKeyboard(delegate: self)

            khzTextField._lightPlaceholderColor(UIColor.lightText)
            khzTextField.setAsNumericKeyboard(delegate: self)

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
        /// and will adjust the UI text field position according
        /// to the height of the keyboard. The scroll will is adjusted
        /// in order to reach the bottom text fields.
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

                    var contentInset:UIEdgeInsets = self.frequencyViewScroller.contentInset
                    contentInset.bottom = keyboard.size.height
                    frequencyViewScroller.contentInset = contentInset
                }
            }
        }

        /// This function finds the first responder in a UIView.
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
            var unit: FrequencyUnit?

            if textField.tag == 1 {
                unit = FrequencyUnit.hz
            } else if textField.tag == 2 {
                unit = FrequencyUnit.ehz
            } else if textField.tag == 3 {
                unit = FrequencyUnit.phz
            } else if textField.tag == 4 {
                unit = FrequencyUnit.thz
            } else if textField.tag == 5 {
                unit = FrequencyUnit.ghz
            } else if textField.tag == 6 {
                unit = FrequencyUnit.mhz
            } else if textField.tag == 7 {
                unit = FrequencyUnit.khz
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

        /// This function handles the saving of conversions to the user defaults.
        /// Only 5 conversions will be saved under each type.
        /// It checks if the text fields are filled and saves the conversion in user
        /// defaults and if the text fields aren't empty an alert will be shown.
        ///
        /// - Parameter sender: The navigation button item.
        @IBAction func handleSaveButtonClick(_ sender: UIBarButtonItem) {
            if !isTextFieldsEmpty() {
                let conversion = "\(hzTextField.text!) Hz = \(ehzTextField.text!) eHz = \(phzTextField.text!) pHz = \(thzTextField.text!) THz = \(ghzTextField.text!) GHz = \(mhzTextField.text!) MHz = \(khzTextField.text!) kHz"

                var arr = UserDefaults.standard.array(forKey: FREQUENCY_USER_DEFAULTS_KEY) as? [String] ?? []

                if arr.count >= FREQUENCY_USER_DEFAULTS_MAX_COUNT {
                    arr = Array(arr.suffix(FREQUENCY_USER_DEFAULTS_MAX_COUNT - 1))
                }
                arr.append(conversion)
                UserDefaults.standard.set(arr, forKey: FREQUENCY_USER_DEFAULTS_KEY)

                let alert = UIAlertController(title: "Success", message: "The frequency conversion was successfully saved!", preferredStyle: UIAlertController.Style.alert)
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
        /// - Returns: true or false
        func isTextFieldsEmpty() -> Bool {
            if !(hzTextField.text?.isEmpty)! && !(ehzTextField.text?.isEmpty)! &&
                !(phzTextField.text?.isEmpty)! && !(thzTextField.text?.isEmpty)! &&
                !(ghzTextField.text?.isEmpty)! && !(mhzTextField.text?.isEmpty)! &&
                !(khzTextField.text?.isEmpty)! {
                return false
            }
            return true
        }

        /// This function modifies all the text fields accordingly based on the changed text field.
        ///
        /// - Parameter textField: The changed text field.
        ///             unit: The frequency unit of the changed text field.
        func updateTextFields(textField: UITextField, unit: FrequencyUnit) -> Void {
            if let input = textField.text {
                if input.isEmpty {
                    clearTextFields()
                } else {
                    if let value = Double(input as String) {
                        let frequency = Frequency(unit: unit, value: value)

                        for _unit in FrequencyUnit.getAllUnits {
                            if _unit == unit {
                                continue
                            }
                            let textField = mapUnitToTextField(unit: _unit)
                            let result = frequency.convert(unit: _unit)

                            // rounding off to 4 decimal places
                            let roundedResult = Double(round(10000 * result) / 10000)

                            textField.text = String(roundedResult)
                        }
                    }
                }
            }
        }

        /// This function maps the respective frequency unit to the respective UITextField.
        ///
        /// - Parameter unit: The frequency unit.
        ///
        /// - Returns: A UITextField corresponding to the frequency unit.
        func mapUnitToTextField(unit: FrequencyUnit) -> UITextField {
            var textField = hzTextField
            switch unit {
            case .hz:
                textField = hzTextField
            case .ehz:
                textField = ehzTextField
            case .phz:
                textField = phzTextField
            case .thz:
                textField = thzTextField
            case .ghz:
                textField = ghzTextField
            case .mhz:
                textField = mhzTextField
            case .khz:
                textField = khzTextField
            }
            return textField!
        }

        /// This function clears all the text fields
        func clearTextFields() {
            hzTextField.text = ""
            ehzTextField.text = ""
            phzTextField.text = ""
            thzTextField.text = ""
            ghzTextField.text = ""
            mhzTextField.text = ""
            khzTextField.text = ""
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
