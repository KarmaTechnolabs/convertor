//
//  Space.swift
//  Converter
//
//  Created by Karma on 11/03/24.

//

import Foundation

enum SpaceUnit {
    case astronomicalUnit
    case kilometer
    case lightYear
    case lightMinute
    case lightSecond
    
    static let allUnits = [astronomicalUnit, kilometer, lightYear, lightMinute, lightSecond]
}

struct Space {
    let value: Double
    let unit: SpaceUnit
    
    init(unit: SpaceUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: SpaceUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .astronomicalUnit:
            if to == .kilometer {
                output = value * 1.496e+8
            } else if to == .lightYear {
                output = value / 63241.077
            } else if to == .lightMinute {
                output = value / 499.005
            } else if to == .lightSecond {
                output = value / 29940.3
            }
        case .kilometer:
            if to == .astronomicalUnit {
                output = value / 1.496e+8
            } else if to == .lightYear {
                output = value / 9.461e+12
            } else if to == .lightMinute {
                output = value / 1.799e+7
            } else if to == .lightSecond {
                output = value / 1.079e+9
            }
        case .lightYear:
            if to == .astronomicalUnit {
                output = value * 63241.077
            } else if to == .kilometer {
                output = value * 9.461e+12
            } else if to == .lightMinute {
                output = value * 0.000188
            } else if to == .lightSecond {
                output = value * 0.011236
            }
        case .lightMinute:
            if to == .astronomicalUnit {
                output = value * 499.005
            } else if to == .kilometer {
                output = value * 1.799e+7
            } else if to == .lightYear {
                output = value * 0.000188
            } else if to == .lightSecond {
                output = value * 59.958
            }
        case .lightSecond:
            if to == .astronomicalUnit {
                output = value * 29940.3
            } else if to == .kilometer {
                output = value * 1.079e+9
            } else if to == .lightYear {
                output = value * 0.011236
            } else if to == .lightMinute {
                output = value / 59.958
            }
        }
        
        return output
    }
}


