//
//  Volume.swift
//  Converter
//
//  Created by karma on 21/01/2024.


import Foundation

enum VolumeUnit {
    case litre
    case millilitre
    case gallon
    case pint
    case fluidOunce
    
    static let getAllUnits = [litre, millilitre, gallon, pint, fluidOunce]
}

struct Volume {
    let value: Double
    let unit: VolumeUnit
    
    init(unit: VolumeUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: VolumeUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .litre:
            if to == .millilitre {
                output = value * 1000
            } else if to == .gallon {
                output = value / 3.785
            } else if to == .pint {
                output = value * 1.76
            } else if to == .fluidOunce {
                output = value * 35.195
            }
        case .millilitre:
            if to == .litre {
                output = value / 1000
            } else if to == .gallon {
                output = value / 3785.412
            } else if to == .pint {
                output = value / 568.261
            } else if to == .fluidOunce {
                output = value / 28.413
            }
        case .gallon:
            if to == .litre {
                output = value * 4.546
            } else if to == .millilitre {
                output = value * 4546.09
            } else if to == .pint {
                output = value * 8
            } else if to == .fluidOunce {
                output = value * 160
            }
        case .pint:
            if to == .litre {
                output = value / 1.76
            } else if to == .millilitre {
                output = value * 568.261
            } else if to == .gallon {
                output = value / 8
            } else if to == .fluidOunce {
                output = value * 20
            }
        case .fluidOunce:
            if to == .litre {
                output = value / 35.195
            } else if to == .millilitre {
                output = value * 28.413
            } else if to == .gallon {
                output = value / 160
            } else if to == .pint {
                output = value / 20
            }
        }
        
        return output
    }
}
