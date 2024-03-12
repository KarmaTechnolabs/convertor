//
//  Speed.swift
// Converter
//
//  Created by karma on 25/01/2024.


import Foundation

enum SpeedUnit {
    case ms
    case kmh
    case mih
    case kn
    
    static let getAllUnits = [ms, kmh, mih, kn]
}

struct Speed {
    let value: Double
    let unit: SpeedUnit
    
    init(unit: SpeedUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: SpeedUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .ms:
            if to == .kmh {
                output = value * 5793.638
            } else if to == .mih {
                output = value * 3600
            } else if to == .kn {
                output = value / 3128.314
            }
        case .kmh:
            if to == .ms {
                output = value / 3.6
            } else if to == .mih {
                output = value / 1.609
            } else if to == .kn {
                output = value / 1.852
            }
        case .mih:
            if to == .ms {
                output = value / 2.237
            } else if to == .kmh {
                output = value *  1.609
            } else if to == .kn {
                output = value / 1.151
            }
        case .kn:
            if to == .ms {
                output = value / 1.944
            } else if to == .kmh {
                output = value * 1.852
            } else if to == .mih {
                output = value * 1.151
            }
        }
        return output
    }
}

