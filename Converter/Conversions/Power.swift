import Foundation

enum PowerUnit {
    case watt
    case kilowatt
    case horsepower

    static let allUnits = [watt, kilowatt, horsepower]
}

struct Power {
    let value: Double
    let unit: PowerUnit

    init(unit: PowerUnit, value: Double) {
        self.value = value
        self.unit = unit
    }

    func convert(unit to: PowerUnit) -> Double {
        var output = 0.0

        switch unit {
        case .watt:
            if to == .kilowatt {
                output = value / 1000
            } else if to == .horsepower {
                output = value / 745.7
            }
        case .kilowatt:
            if to == .watt {
                output = value * 1000
            } else if to == .horsepower {
                output = value * 1.341
            }
        case .horsepower:
            if to == .watt {
                output = value * 745.7
            } else if to == .kilowatt {
                output = value / 1.341
            }
        }

        return output
    }
}

let power = Power(unit: .watt, value: 1000)
let convertValue = power.convert(unit: .kilowatt) // Output: 1.0
