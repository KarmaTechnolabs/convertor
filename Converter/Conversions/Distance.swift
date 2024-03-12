//
//  Distance.swift
//  Converter
//
//  Created by karma on 21/01/2024.


import Foundation

enum DistanceUnit {
    case meter
    case centimeter
    case millimeter
    case mile
    case yard
    case inch
    
    static let getAllUnits = [meter, centimeter, millimeter, mile, yard, inch]
}

struct Distance {
    let value: Double
    let unit: DistanceUnit
    
    init(unit: DistanceUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: DistanceUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .meter:
            if to == .centimeter {
                output = value * 100
            } else if to == .millimeter {
                output = value * 1000
            } else if to == .mile {
                output = value / 1609.244
            } else if to == .yard {
                output = value * 1.094
            } else if to == .inch {
                output = value * 39.37
            }
        case .centimeter:
            if to == .meter {
                output = value / 100
            } else if to == .millimeter {
                output = value * 10
            } else if to == .mile {
                output = value / 160934.4
            } else if to == .yard {
                output = value / 91.44
            } else if to == .inch {
                output = value / 2.54
            }
        case .millimeter:
            if to == .meter {
                output = value / 1000
            } else if to == .centimeter {
                output = value / 10
            } else if to == .mile {
                output = value / 1.609e+6
            } else if to == .yard {
                output = value / 914.4
            } else if to == .inch {
                output = value / 25.4
            }
        case .mile:
            if to == .meter {
                output = value * 1609.344
            } else if to == .centimeter {
                output = value * 160934.4
            } else if to == .millimeter {
                output = value * 1.609e+6
            } else if to == .yard {
                output = value * 1760
            } else if to == .inch {
                output = value * 63360
            }
        case .yard:
            if to == .meter {
                output = value / 1.094
            } else if to == .centimeter {
                output = value * 91.44
            } else if to == .millimeter {
                output = value * 914.4
            } else if to == .mile {
                output = value / 1760
            } else if to == .inch {
                output = value * 36
            }
        case .inch:
            if to == .meter {
                output = value / 39.37
            } else if to == .centimeter {
                output = value * 2.54
            } else if to == .millimeter {
                output = value * 25.4
            } else if to == .mile {
                output = value / 63360
            } else if to == .yard {
                output = value / 36
            }
        }
            return output
    }
}

