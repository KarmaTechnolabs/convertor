import Foundation

enum AreaUnit {
    case hectare
    case acre
    case squareKilometer
    case squareMeter
    case squareYard
    case squareFoot
    case squareInch
    
    static let allUnits = [hectare, acre, squareKilometer, squareMeter, squareYard, squareFoot, squareInch]
}

struct Area {
    let value: Double
    let unit: AreaUnit
    
    init(unit: AreaUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: AreaUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .hectare:
            if to == .acre {
                output = value * 2.47105
            } else if to == .squareKilometer {
                output = value * 0.01
            } else if to == .squareMeter {
                output = value * 10000
            } else if to == .squareYard {
                output = value * 11959.9
            } else if to == .squareFoot {
                output = value * 107639
            } else if to == .squareInch {
                output = value * 1.55e+7
            }
        case .acre:
            if to == .hectare {
                output = value * 0.404686
            } else if to == .squareKilometer {
                output = value * 0.00404686
            } else if to == .squareMeter {
                output = value * 4046.86
            } else if to == .squareYard {
                output = value * 4840
            } else if to == .squareFoot {
                output = value * 43560
            } else if to == .squareInch {
                output = value * 6.273e+6
            }
        case .squareKilometer:
            if to == .hectare {
                output = value * 100
            } else if to == .acre {
                output = value * 247.105
            } else if to == .squareMeter {
                output = value * 1e+6
            } else if to == .squareYard {
                output = value * 1.196e+6
            } else if to == .squareFoot {
                output = value * 1.076e+7
            } else if to == .squareInch {
                output = value * 1.55e+9
            }
        case .squareMeter:
            if to == .hectare {
                output = value * 1e-4
            } else if to == .acre {
                output = value * 0.000247105
            } else if to == .squareKilometer {
                output = value * 1e-6
            } else if to == .squareYard {
                output = value * 1.196
            } else if to == .squareFoot {
                output = value * 10.7639
            } else if to == .squareInch {
                output = value * 1550
            }
        case .squareYard:
            if to == .hectare {
                output = value * 8.3613e-5
            } else if to == .acre {
                output = value * 0.000206612
            } else if to == .squareKilometer {
                output = value * 8.3613e-7
            } else if to == .squareMeter {
                output = value * 0.836127
            } else if to == .squareFoot {
                output = value * 9
            } else if to == .squareInch {
                output = value * 1296
            }
        case .squareFoot:
            if to == .hectare {
                output = value * 9.2903e-6
            } else if to == .acre {
                output = value * 2.2957e-5
            } else if to == .squareKilometer {
                output = value * 9.2903e-8
            } else if to == .squareMeter {
                output = value * 0.092903
            } else if to == .squareYard {
                output = value * 0.111111
            } else if to == .squareInch {
                output = value * 144
            }
        case .squareInch:
            if to == .hectare {
                output = value * 6.4516e-8
            } else if to == .acre {
                output = value * 1.5942e-7
            } else if to == .squareKilometer {
                output = value * 6.4516e-10
            } else if to == .squareMeter {
                output = value * 0.00064516
            } else if to == .squareYard {
                output = value * 0.000771605
            } else if to == .squareFoot {
                output = value * 0.00694444
            }
        }
        
        return output
    }
}
