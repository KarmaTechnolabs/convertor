//
//  Resistance.swift
//  Converter
//
//  Created by Karma on 18/03/24.
//

import Foundation


enum ResistanceUnit {
    case ohm
    case megohm
    case microohm
    case voltPerAmpere
    case siemens
    case abohm

    static let allUnits = [ohm, megohm, microohm, voltPerAmpere, siemens, abohm]

}

struct Resistance {
    let value: Double
    let unit: ResistanceUnit

    init(unit: ResistanceUnit, value: Double) {
        self.value = value
        self.unit = unit
    }

    func convert(unit to: ResistanceUnit) -> Double {
        var output = 0.0

        switch unit {
        case .ohm:
            if to == .megohm {
                output = value / 1_000_000
            } else if to == .microohm {
                output = value * 1_000_000
            }
        case .megohm:
            if to == .ohm {
                output = value * 1_000_000
            } else if to == .microohm {
                output = value * 1_000
            }
        case .microohm:
            if to == .ohm {
                output = value / 1_000_000
            } else if to == .megohm {
                output = value / 1_000
            }
        case .voltPerAmpere:
            if to == .siemens {
                output = 1 / value
            }
        case .siemens:
            if to == .voltPerAmpere {
                output = 1 / value
            }
        case .abohm:
            if to == .ohm {
                output = value * 10
            }
        }

        return output
    }
}

let resistance = Resistance(unit: .ohm, value: 1_000_000)
let convertedResistanceValue = resistance.convert(unit: .megohm) // Output: 1.0

/*for unit in ResistanceUnit.allUnits {
    print("Unit: \(unit.description())")
}
*/
