
import UIKit

let WEIGHTS_USER_DEFAULTS_KEY = "weight"
private let WEIGHTS_USER_DEFAULTS_MAX_COUNT = 5

class WeightConversionViewController: UIViewController, CustomNumericKeyboardDelegate {
    
    
    @IBOutlet var textFields: [UITextField]!
    @IBOutlet weak var outerStackView: UIStackView!
    @IBOutlet weak var outerStackViewTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var weightViewScroller: UIScrollView!
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
        
        // Set Text Field Styles and Properties
        for textField in textFields {
            textField._lightPlaceholderColor(UIColor.lightText)
            textField.setAsNumericKeyboard(delegate: self)
        }
        
        // Add an observer to track keyboard show event
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)),
                                               name: UIResponder.keyboardWillShowNotification, object: nil)
    }
    
    @objc func keyboardWillHide() {
        view.endEditing(true)
        
        UIView.animate(withDuration: 0.25) {
            self.outerStackViewTopConstraint.constant = self.outerStackViewTopConstraintDefaultHeight
            self.view.layoutIfNeeded()
        }
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        guard let activeTextField = findFirstResponder(inView: self.view) as? UITextField else { return }
        self.activeTextField = activeTextField
        
        let activeTextFieldSuperView = activeTextField.superview ?? activeTextField.superview?.superview
        
        if let info = notification.userInfo {
            let keyboard: CGRect = info[UIResponder.keyboardFrameEndUserInfoKey] as! CGRect
            
            let targetY = view.frame.size.height - keyboard.height - 15 - activeTextField.frame.size.height
            let initialY = outerStackView.frame.origin.y + (activeTextFieldSuperView?.frame.origin.y ?? 0) + activeTextField.frame.origin.y
            
            if initialY > targetY {
                let diff = targetY - initialY
                let targetOffsetForTopConstraint = outerStackViewTopConstraint.constant + diff
                
                UIView.animate(withDuration: 0.25) {
                    self.outerStackViewTopConstraint.constant = targetOffsetForTopConstraint
                    self.view.layoutIfNeeded()
                }
            }
            
            var contentInset: UIEdgeInsets = self.weightViewScroller.contentInset
            contentInset.bottom = keyboard.size.height
            weightViewScroller.contentInset = contentInset
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
        var unit: WeightUnit?
        
        if textField.tag == 1 {
            unit = WeightUnit.kilogram
        } else if textField.tag == 2 {
            unit = WeightUnit.gram
        } else if textField.tag == 3 {
            unit = WeightUnit.ounce
        } else if textField.tag == 4 {
            unit = WeightUnit.pound
        } else if textField.tag == 5 {
            unit = WeightUnit.stone
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
        guard !isTextFieldsEmpty() else {
            showAlert(title: "Error", message: "You are trying to save an empty conversion!")
            return
        }
        
        let conversion = "\(textFields[0].text!) kg = \(textFields[1].text!) g = \(textFields[2].text!) oz =  \(textFields[3].text!) lb & \(textFields[4].text!) stones"
        
        var arr = UserDefaults.standard.array(forKey: WEIGHTS_USER_DEFAULTS_KEY) as? [String] ?? []
        
        if arr.count >= WEIGHTS_USER_DEFAULTS_MAX_COUNT {
            arr = Array(arr.suffix(WEIGHTS_USER_DEFAULTS_MAX_COUNT - 1))
        }
        arr.append(conversion)
        UserDefaults.standard.set(arr, forKey: WEIGHTS_USER_DEFAULTS_KEY)
        
        showAlert(title: "Success", message: "The weight conversion was successfully saved!")
    }
    
    func showAlert(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func isTextFieldsEmpty() -> Bool {
        return textFields.allSatisfy { $0.text?.isEmpty ?? true }
    }
    
    func updateTextFields(textField: UITextField, unit: WeightUnit) {
        guard let input = textField.text, !input.isEmpty, let value = Double(input) else {
            clearTextFields()
            return
        }
        
        let weight = Weight(unit: unit, value: value)
        
        for _unit in WeightUnit.getAllUnits where _unit != unit {
            if let textField = mapUnitToTextField(unit: _unit) {
                let result = weight.convert(unit: _unit)
                let roundedResult = Double(round(10000 * result) / 10000)
                textField.text = String(roundedResult)
        }
        }
    }
    
    func mapUnitToTextField(unit: WeightUnit) -> UITextField? {
        switch unit {
        case .kilogram: return textFields.first
        case .gram: return textFields[1]
        case .ounce: return textFields[2]
        case .pound: return textFields[3]
        case .stone: return textFields[4]
        }
    }
    
    
    func clearTextFields() {
        textFields.forEach { $0.text = "" }
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

extension Float {
    var cleanValue: String {
        return self.truncatingRemainder(dividingBy: 1) == 0 ? String(format: "%.0f", self) : String(self)
    }
}
