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

    }
    override func viewWillAppear(_ animated: Bool) {
           super.viewWillAppear(animated)
           bannerView.load(GADRequest())
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
       
       
       
        conversions += [weight, temperature, volume, distance, speed, time,energy,data,power,frequency,bloodsugar,cooking,space,area]
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

