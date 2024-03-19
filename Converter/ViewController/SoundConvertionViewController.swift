//
//  PowerConversionViewController.swift
//  Converter
//
//  Created by Karma on 11/03/24.


import UIKit
let SOUND_USER_DEFAULTS_KEY = "sound"
private let SOUND_USER_DEFAULTS_MAX_COUNT = 5

class SoundConvertionViewController: UIViewController,CustomNumericKeyboardDelegate {

    @IBOutlet weak var soundViewScroller: UIScrollView!
    @IBOutlet weak var outerStackView: UIStackView!    
    @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var dBTextField: UITextField!
    @IBOutlet weak var dBTextFieldStackView: UIStackView!
    @IBOutlet weak var BelTextField: UITextField!
    @IBOutlet weak var BelTextFieldStackView: UIStackView!
    @IBOutlet weak var NpextField: UITextField!
    @IBOutlet weak var NpTextFieldStackView: UIStackView!

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
         dBTextField._lightPlaceholderColor(UIColor.lightText)
         dBTextField.setAsNumericKeyboard(delegate: self)
         
         BelTextField._lightPlaceholderColor(UIColor.lightText)
         BelTextField.setAsNumericKeyboard(delegate: self)

         NpextField._lightPlaceholderColor(UIColor.lightText)
         NpextField.setAsNumericKeyboard(delegate: self)

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

                 var contentInset:UIEdgeInsets = self.soundViewScroller.contentInset
                 contentInset.bottom = keyboard.size.height
                 soundViewScroller.contentInset = contentInset
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
        var unit: SoundUnit?

        if textField.tag == 1 {
            unit = SoundUnit.decibel
        } else if textField.tag == 2 {
            unit = SoundUnit.bel
        } else if textField.tag == 3 {
            unit = SoundUnit.neper
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
             let conversion = "\(dBTextField.text!) watt = \(BelTextField.text!) kilowatt = \(NpextField.text!) horsepower"

             var arr = UserDefaults.standard.array(forKey: POWER_USER_DEFAULTS_KEY) as? [String] ?? []

             if arr.count >= SOUND_USER_DEFAULTS_MAX_COUNT{
                 arr = Array(arr.suffix(SOUND_USER_DEFAULTS_MAX_COUNT - 1))
             }
             arr.append(conversion)
             UserDefaults.standard.set(arr, forKey: SOUND_USER_DEFAULTS_KEY)

             let alert = UIAlertController(title: "Success", message: "The power conversion was successully saved!", preferredStyle: UIAlertController.Style.alert)
             alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
             self.present(alert, animated: true, completion: nil)
         } else {
             let alert = UIAlertController(title: "Error", message: "You are trying to save an empty conversion!", preferredStyle: UIAlertController.Style.alert)
             alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
             self.present(alert, animated: true, completion: nil)
         }
     }

     func isTextFieldsEmpty() -> Bool {
         if !(dBTextField.text?.isEmpty)! && !(BelTextField.text?.isEmpty)! &&
             !(NpextField.text?.isEmpty)! {
             return false
         }
         return true
     }

     func updateTextFields(textField: UITextField, unit: SoundUnit) -> Void {
         if let input = textField.text {
             if input.isEmpty {
                 clearTextFields()
             } else {
                 if let value = Double(input as String) {
                     let sound = Sound(unit: unit, value: value)

                     for _unit in SoundUnit.allUnits {
                         if _unit == unit {
                             continue
                         }
                         let textField = mapUnitToTextField(unit: _unit)
                         let result = sound.convert(unit: _unit)

                         // Rounding off to 4 decimal places
                         let roundedResult = Double(round(10000 * result) / 10000)

                         textField.text = String(roundedResult)
                     }
                 }
             }
         }
     }

     func mapUnitToTextField(unit: SoundUnit) -> UITextField {
         var textField = dBTextField
         switch unit {
         case .decibel:
             textField = dBTextField
         case .bel:
             textField = BelTextField
         case .neper:
             textField = NpextField
         }
         return textField!
     }

     func clearTextFields() {
         dBTextField.text = ""
         BelTextField.text = ""
         NpextField.text = ""
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
