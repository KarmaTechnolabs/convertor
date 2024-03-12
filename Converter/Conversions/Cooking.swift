//
//  Cooking.swift
//  Converter
//
//  Created by Karma on 11/03/24.

//

import Foundation

enum CookingUnit {
    case ml
    case tspn
    case tbsp
    case cup
    case floz

    static let allUnits = [ml, tspn, tbsp, cup, floz]
}

struct CookingMeasurement {
    let value: Double
    let unit: CookingUnit

    init(unit: CookingUnit, value: Double) {
        self.value = value
        self.unit = unit
    }

    func convert(unit to: CookingUnit) -> Double {
        var output = 0.0

        switch unit {
        case .ml:
            if to == .tspn {
                output = value * 0.202884
            } else if to == .tbsp {
                output = value * 0.067628
            } else if to == .cup {
                output = value * 0.00422675
            } else if to == .floz {
                output = value * 0.033814
            }
        case .tspn:
            if to == .ml {
                output = value * 4.92892
            } else if to == .tbsp {
                output = value * 0.333333
            } else if to == .cup {
                output = value * 0.0205372
            } else if to == .floz {
                output = value * 0.166667
            }
        case .tbsp:
            if to == .ml {
                output = value * 14.7868
            } else if to == .tspn {
                output = value * 3
            } else if to == .cup {
                output = value * 0.0616115
            } else if to == .floz {
                output = value * 0.5
            }
        case .cup:
            if to == .ml {
                output = value * 236.588
            } else if to == .tspn {
                output = value * 48
            } else if to == .tbsp {
                output = value * 16
            } else if to == .floz {
                output = value * 8
            }
        case .floz:
            if to == .ml {
                output = value * 29.5735
            } else if to == .tspn {
                output = value * 6
            } else if to == .tbsp {
                output = value * 2
            } else if to == .cup {
                output = value * 0.125
            }
        }

        return output
    }
}
