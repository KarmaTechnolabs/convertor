//
//  SpaceConversionViewController.swift
//  Converter
//
//  Created by Karma on 11/03/24.


import UIKit
let SPACE_USER_DEFAULTS_KEY = "space"
private let SPACE_USER_DEFAULTS_MAX_COUNT = 5


class SpaceConversionViewController: UIViewController,CustomNumericKeyboardDelegate  {

        @IBOutlet weak var viewScroller: UIScrollView!
          @IBOutlet weak var outerStackView: UIStackView!
          @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
          @IBOutlet weak var astronomicalUnitTextField: UITextField!
          @IBOutlet weak var astronomicalUnitTextFieldStackView: UIStackView!
          @IBOutlet weak var kilometerTextField: UITextField!
          @IBOutlet weak var kilometerTextFieldStackView: UIStackView!
          @IBOutlet weak var lightYearTextField: UITextField!
          @IBOutlet weak var lightYearTextFieldStackView: UIStackView!
          @IBOutlet weak var lightMinuteTextField: UITextField!
          @IBOutlet weak var lightMinuteTextFieldStackView: UIStackView!
          @IBOutlet weak var lightSecondTextField: UITextField!
          @IBOutlet weak var lightSecondTextFieldStackView: UIStackView!
         
          
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
          astronomicalUnitTextField._lightPlaceholderColor(UIColor.lightText)
          astronomicalUnitTextField.setAsNumericKeyboard(delegate: self)
          
          kilometerTextField._lightPlaceholderColor(UIColor.lightText)
          kilometerTextField.setAsNumericKeyboard(delegate: self)
          
          lightYearTextField._lightPlaceholderColor(UIColor.lightText)
          lightYearTextField.setAsNumericKeyboard(delegate: self)
          
          lightMinuteTextField._lightPlaceholderColor(UIColor.lightText)
          lightMinuteTextField.setAsNumericKeyboard(delegate: self)
          
          lightSecondTextField._lightPlaceholderColor(UIColor.lightText)
          lightSecondTextField.setAsNumericKeyboard(delegate: self)
          
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
          var unit: SpaceUnit?
          
          if textField.tag == 1 {
              unit = SpaceUnit.astronomicalUnit
          } else if textField.tag == 2 {
              unit = SpaceUnit.kilometer
          } else if textField.tag == 3 {
              unit = SpaceUnit.lightYear
          } else if textField.tag == 4 {
              unit = SpaceUnit.lightMinute
          } else if textField.tag == 5 {
              unit = SpaceUnit.lightSecond
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
                        let conversion = "\(astronomicalUnitTextField.text!) astronomical units = \(kilometerTextField.text!) kilometers = \(lightYearTextField.text!) light years = \(lightMinuteTextField.text!) light minutes = \(lightSecondTextField.text!) light seconds"
                        
                        var arr = UserDefaults.standard.array(forKey: SPACE_USER_DEFAULTS_KEY) as? [String] ?? []
                        
                        if arr.count >= SPACE_USER_DEFAULTS_MAX_COUNT {
                            arr = Array(arr.suffix(SPACE_USER_DEFAULTS_MAX_COUNT - 1))
                        }
                        arr.append(conversion)
                        UserDefaults.standard.set(arr, forKey: SPACE_USER_DEFAULTS_KEY)
                        
                        let alert = UIAlertController(title: "Success", message: "The space conversion was successfully saved!", preferredStyle: UIAlertController.Style.alert)
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
                if !(astronomicalUnitTextField.text?.isEmpty)! && !(kilometerTextField.text?.isEmpty)! &&
                    !(lightYearTextField.text?.isEmpty)! && !(lightMinuteTextField.text?.isEmpty)! &&
                    !(lightSecondTextField.text?.isEmpty)! {
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
      func updateTextFields(textField: UITextField, unit: SpaceUnit) -> Void {
          if let input = textField.text {
              if input.isEmpty {
                  clearTextFields()
              } else {
                  if let value = Double(input as String) {
                  let space = Space(unit: unit, value: value)
                  
                  for _unit in SpaceUnit.allUnits {
                      if _unit == unit {
                          continue
                      }
                      let textField = mapUnitToTextField(unit: _unit)
                      let result = space.convert(unit: _unit)
                      
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
      func mapUnitToTextField(unit: SpaceUnit) -> UITextField {
          var textField = astronomicalUnitTextField
          switch unit {
          case .astronomicalUnit:
              textField = astronomicalUnitTextField
          case .kilometer:
              textField = kilometerTextField
          case .lightYear:
              textField = lightYearTextField
          case .lightMinute:
              textField = lightMinuteTextField
          case .lightSecond:
              textField = lightSecondTextField
          }
          return textField!
      }
      
      /// This function clears all the text fields
      func clearTextFields() {
          astronomicalUnitTextField.text = ""
          kilometerTextField.text = ""
          lightYearTextField.text = ""
          lightMinuteTextField.text = ""
          lightSecondTextField.text = ""
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
