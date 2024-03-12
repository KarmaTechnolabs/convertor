//
//  Length.swift
//  Converter
//
//  Created by Karma on 11/03/24.

//

import Foundation

enum BloodsugarUnit {
    case mmolPerL
    case mgPerdL
    
    static let getAllUnits = [mmolPerL, mgPerdL]
}

struct BloodsugarMeasure {
    let value: Double
    let unit: BloodsugarUnit
    
    init(unit: BloodsugarUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: BloodsugarUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .mmolPerL:
            if to == .mgPerdL {
                output = value * 18
            }
        case .mgPerdL:
            if to == .mmolPerL {
                output = value / 18
            }
        }
        return output
    }
}
