//
//  FirstViewController.swift
// Converter
//
//  Created by karma on 03/01/2024.
//

import UIKit
import GoogleMobileAds

class ConvertionsViewController: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate,GADBannerViewDelegate {
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var introView: UIView!
    @IBOutlet weak var stepsImages: UIImageView!
    @IBOutlet weak var pagecontroll: UIPageControl!
    @IBOutlet weak var step1AnotherDesc: UILabel!
    @IBOutlet weak var skipBtn: UIButton!
    @IBOutlet weak var nextBtn: UIButton!
    @IBOutlet weak var descLbl: UILabel!
    
    @IBOutlet weak var headingLbl: UILabel!
    var getStartedButton: UIButton?
    let introCompletedKey = "introCompleted"

    var number = 1
    var conversions = [Conversion]()
    var bannerView: GADBannerView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view, typically from a nib.
        generateConversions()
        bannerView = GADBannerView(adSize: GADAdSizeBanner)
        addBannerViewToView(bannerView)
        
        bannerView.adUnitID = "ca-app-pub-1829431093631944/3966390500"
        bannerView.rootViewController = self
        bannerView.load(GADRequest())
        bannerView.delegate = self
        
        collectionView.layer.cornerRadius = 30.0
           collectionView.layer.borderWidth = 1.0
           collectionView.layer.borderColor = UIColor(red: 0.25, green: 0.25, blue: 0.25, alpha: 1.00).cgColor
           collectionView.layer.masksToBounds = true
        introView.translatesAutoresizingMaskIntoConstraints = false
           view.addSubview(introView)
           
           NSLayoutConstraint.activate([
               introView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
               introView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
               introView.topAnchor.constraint(equalTo: view.topAnchor),
               introView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
           ])
            stepsImages.image = UIImage(named: "step1")
                pagecontroll.numberOfPages = 3
                pagecontroll.currentPage = 0
                let i = UserDefaults.standard.string(forKey: "tutorial")
                if i == "done" {
                    stepsImages.isHidden = true
                   pagecontroll.isHidden = true
                }
        if UserDefaults.standard.bool(forKey: introCompletedKey) {
               // Intro has been completed, hide the introView
               introView.isHidden = true
           } else {
               // Intro has not been completed, show the introView
               introView.isHidden = false
           }

    }
    override func viewWillAppear(_ animated: Bool) {
           super.viewWillAppear(animated)
           bannerView.load(GADRequest())
        if introView.isHidden {
               // IntroView is hidden, show navigation bar and tab bar
               navigationController?.setNavigationBarHidden(false, animated: false)
               tabBarController?.tabBar.isHidden = false
           } else {
               // IntroView is visible, hide navigation bar and tab bar
               navigationController?.setNavigationBarHidden(true, animated: false)
               tabBarController?.tabBar.isHidden = true
           }
       }
  
    func bannerViewDidReceiveAd(_ bannerView: GADBannerView) {
        addBannerViewToView(bannerView)
    }
    
       func bannerView(_ bannerView: GADBannerView, didFailToReceiveAdWithError error: Error) {
           print("Failed to load ad: \(error.localizedDescription)")
       }
       
    func addBannerViewToView(_ bannerView: GADBannerView) {
        bannerView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(bannerView)
        view.addConstraints(
          [NSLayoutConstraint(item: bannerView,
                              attribute: .bottom,
                              relatedBy: .equal,
                              toItem: view.safeAreaLayoutGuide,
                              attribute: .bottom,
                              multiplier: 1,
                              constant: 0),
           NSLayoutConstraint(item: bannerView,
                              attribute: .centerX,
                              relatedBy: .equal,
                              toItem: view,
                              attribute: .centerX,
                              multiplier: 1,
                              constant: 0)
          ])
       }
    @IBAction func swipeAction(_ sender: Any) {
        print("swipe work")
        if number < 4 {
            number += 1
            getStartedButton?.isHidden = true
        }
        
        if number == 4 {
                    // Disable further swiping after the last image
            pagecontroll.isUserInteractionEnabled = false
           
        }
        
        if number <= 3 {
            stepsImages.image = UIImage(named: "step\(String(number))")
            pagecontroll.currentPage = number - 1
            updateLabel()
            updateHeadingLabel()
            
            if number == 3 {
                step1AnotherDesc.isHidden = true
                pagecontroll.isHidden = true
                skipBtn.isHidden = true
                nextBtn.isHidden = true
                
                let getStartedButton = UIButton(type: .system)
                getStartedButton.frame = CGRect(x: 100, y: 650, width: 180, height: 50)
                getStartedButton.setTitle("Get Started", for: .normal)
                if #available(iOS 15.0, *) {
                    getStartedButton.backgroundColor = UIColor.systemCyan
                } else {
                    // Fallback on earlier versions
                }
                getStartedButton.layer.cornerRadius = 10
                getStartedButton.setTitleColor(UIColor.white, for: .normal)
                getStartedButton.addTarget(self, action: #selector(getStartedButtonTapped), for: .touchUpInside)
                view.addSubview(getStartedButton)
            }
        } else {
            // Show the last image statically
            stepsImages.image = UIImage(named: "step3")
        }
    }
   

    func updateLabel() {
        switch number {
        case 1:
            descLbl.text = "Convertor app covers a wide range of unit categories.It provides a simple and intuitive interface for easy unit conversion."
            step1AnotherDesc.text = "Converer offline access and customization options make it a convenient tool for users."
        case 2:
            descLbl.text = "Easy to search all measurable unit and its value in Converter."
            step1AnotherDesc.isHidden = true
        case 3:
            descLbl.text = "With Converter easy-to-use interface and quick access to a wide range of units, the app helps users save time when converting between different units.."
            step1AnotherDesc.isHidden = true
        default:
            descLbl.text = "Converting Value"
        }
    }
    func updateHeadingLabel() {
        switch number {
        case 1:
            headingLbl.text = "Easy To Access"
        case 2:
            headingLbl.text = "Easy To Search"
        case 3:
            headingLbl.text = "Easy To Manage"
        default:
            headingLbl.text = "Converting Value"
        }
    }
    
    @objc func getStartedButtonTapped() {
           introView.isHidden = true
            navigationController?.setNavigationBarHidden(false, animated: false)
            tabBarController?.tabBar.isHidden = false
            getStartedButton?.isEnabled = false // Disable the existing getStartedButton
            UserDefaults.standard.set(true, forKey: introCompletedKey)
            getStartedButton?.removeFromSuperview() // Remove the getStartedButton from the view
       

    }

   @IBAction func nextScreen(_ sender: UIButton) {
        if number < 3 {
               number += 1
               updateUIForCurrentScreen()
           } else {
               // Handle the last screen or any other action you want
           }
    }
    func updateUIForCurrentScreen() {
        stepsImages.image = UIImage(named: "step\(number)")
        pagecontroll.currentPage = number - 1
        updateLabel()
        updateHeadingLabel()
        
        if number == 3 {
            step1AnotherDesc.isHidden = true
            pagecontroll.isHidden = true
            skipBtn.isHidden = true
            nextBtn.isHidden = true
            
            let getStartedButton = UIButton(type: .system)
            getStartedButton.frame = CGRect(x: 100, y: 650, width: 180, height: 50)
            getStartedButton.setTitle("Get Started", for: .normal)
            if #available(iOS 15.0, *) {
                getStartedButton.backgroundColor = UIColor.systemCyan
            } else {
                // Fallback on earlier versions
            }
            getStartedButton.layer.cornerRadius = 10
            getStartedButton.setTitleColor(UIColor.white, for: .normal)
            getStartedButton.addTarget(self, action: #selector(getStartedButtonTapped), for: .touchUpInside)
            view.addSubview(getStartedButton)
        }
    }
    @IBAction func SkipIntroScreen(_ sender: UIButton) {
        introView.isHidden = true
    }
    func generateConversions() {
        let weight = Conversion(name: "Weight", icon: UIImage(named: "icon_weight")!, segueID: "goToWeightConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let temperature = Conversion(name: "Tempertaure", icon: UIImage(named: "icon_temperature")!, segueID: "goToTemperatureConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let volume = Conversion(name: "Volume", icon: UIImage(named: "icon_volume")!, segueID: "goToVolumeConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let distance = Conversion(name: "Distance", icon: UIImage(named: "icon_distance")!, segueID: "goToDistanceConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let speed = Conversion(name: "Speed", icon: UIImage(named: "icon_speed")!, segueID: "goToSpeedConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let time = Conversion(name: "Time", icon: UIImage(named: "icon_time")!, segueID: "goToTimeConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let energy = Conversion(name: "Energy", icon: UIImage(named: "icon_energy")!, segueID: "goToEnergyConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let data = Conversion(name: "Data", icon: UIImage(named: "icon_data")!, segueID: "goToDataConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let power = Conversion(name: "Power", icon: UIImage(named: "icon_power")!, segueID: "goToPowerConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let frequency = Conversion(name: "Frequency", icon: UIImage(named: "icon_frequency")!, segueID: "goToFrequencyConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let bloodsugar = Conversion(name: "Bloodsugar", icon: UIImage(named: "icon_bloodsugar")!, segueID: "goToBloodsugarConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let cooking = Conversion(name: "Cooking", icon: UIImage(named: "icon_cooking")!, segueID: "goToCookingConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let space = Conversion(name: "Space", icon: UIImage(named: "icon_space")!, segueID: "goToSpaceConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let area = Conversion(name: "Area", icon: UIImage(named: "icon_area")!, segueID: "goToAreaConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let sound = Conversion(name: "Sound", icon: UIImage(named: "icon_sound")!, segueID: "goToSoundConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
        let resistance = Conversion(name: "Resistance", icon: UIImage(named: "icon_resistance")!, segueID: "goToResistanceConversion", cellColour: UIColor(red: 30/255, green: 30/255, blue: 30/255, alpha: 1.00))
       
       
        conversions += [weight, temperature, volume, distance, speed, time,energy,data,power,frequency,bloodsugar,cooking,space,area,sound,resistance]
    }
    
    /// This function returns the conversions count to be used in the collection view.
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return conversions.count
    }
    
    /// This function generates the collection view cell.
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "Cell", for: indexPath) as! ConversionViewCell
        cell.conversionName.text = conversions[indexPath.row].getConversionName()
        cell.conversionIcon.image = conversions[indexPath.row].getConversionIcon()
        
        //Card(cell) styles
        cell.contentView.backgroundColor = conversions[indexPath.row].getCellColour()
       cell.contentView.layer.cornerRadius = 10.0
        cell.contentView.layer.borderWidth = 1.0
        cell.contentView.layer.borderColor = UIColor(red: 0.25, green: 0.25, blue: 0.25, alpha: 1.00).cgColor
        cell.contentView.layer.masksToBounds = false
        
        return cell
    }
    
    // This function is called when an item in the collection view is selected.
    // performSegue() method will help navigate to the specified conversion page.
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        performSegue(withIdentifier: conversions[indexPath.row].getSegueID(), sender: self)
    }
}

