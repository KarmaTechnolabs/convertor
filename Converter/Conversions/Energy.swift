//
//  Energy.swift
//  Converter
//
//  Created by Karma on 06/03/24.


enum EnergyUnit {
    case joule
    case kilojoule
    case kilowattHour
    case calorie
    case electronVolt
    case ton
    
    static let allUnits = [joule, kilojoule, kilowattHour, calorie, electronVolt, ton]
}

struct Energy {
    let value: Double
    let unit: EnergyUnit
    
    init(unit: EnergyUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: EnergyUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .joule:
            if to == .kilojoule {
                output = value / 1000
            } else if to == .kilowattHour {
                output = value / 3600000
            } else if to == .calorie {
                output = value / 4.184
            } else if to == .electronVolt {
                output = value * 6.242e+12
            } else if to == .ton {
                output = value / 4.184e+9
            }
        case .kilojoule:
            if to == .joule {
                output = value * 1000
            } else if to == .kilowattHour {
                output = value / 3.6
            } else if to == .calorie {
                output = value * 239.006
            } else if to == .electronVolt {
                output = value * 6.242e+15
            } else if to == .ton {
                output = value / 4.184e+6
            }
        case .kilowattHour:
            if to == .joule {
                output = value * 3600000
            } else if to == .kilojoule {
                output = value * 3.6
            } else if to == .calorie {
                output = value * 860.421
            } else if to == .electronVolt {
                output = value * 2.247e+22
            } else if to == .ton {
                output = value * 860.421 / 4.184e+9
            }
        case .calorie:
            if to == .joule {
                output = value * 4.184
            } else if to == .kilojoule {
                output = value * 0.004184
            } else if to == .kilowattHour {
                output = value / 860.421
            } else if to == .electronVolt {
                output = value * 2.613e+19
            } else if to == .ton {
                output = value / 4.184e+6
            }
        case .electronVolt:
            if to == .joule {
                output = value / 6.242e+12
            } else if to == .kilojoule {
                output = value / 6.242e+15
            } else if to == .kilowattHour {
                output = value / 2.247e+22
            } else if to == .calorie {
                output = value / 2.613e+19
            } else if to == .ton {
                output = value / 2.613e+16
            }

        case .ton:
            if to == .joule {
                output = value * 4.184e+9
            } else if to == .kilojoule {
                output = value * 4.184e+6
            } else if to == .kilowattHour {
                output = value * 4.184e+6 / 3600000
            } else if to == .calorie {
                output = value * 4.184e+6 / 4.184
            } else if to == .electronVolt {
                output = value * 4.184e+6 * 6.242e+12
            }

        }
        
        return output
    }
}

