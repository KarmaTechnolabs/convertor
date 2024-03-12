import Foundation

enum FrequencyUnit {
    case hz
    case ehz
    case phz
    case thz
    case ghz
    case mhz
    case khz
    
    static let getAllUnits = [hz, ehz, phz, thz, ghz, mhz, khz]
}

struct Frequency {
    let value: Double
    let unit: FrequencyUnit
    
    init(unit: FrequencyUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: FrequencyUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .hz:
            if to == .ehz {
                output = value / 1000
            } else if to == .phz {
                output = value / 1_000_000
            } else if to == .thz {
                output = value / 1_000_000_000
            } else if to == .ghz {
                output = value / 1_000_000_000_000
            } else if to == .mhz {
                output = value / 1_000_000
            } else if to == .khz {
                output = value / 1_000
            }
        case .ehz:
            if to == .hz {
                output = value * 1000
            } else if to == .phz {
                output = value / 1000
            } else if to == .thz {
                output = value / 1_000_000
            } else if to == .ghz {
                output = value / 1_000_000_000
            } else if to == .mhz {
                output = value / 1_000
            } else if to == .khz {
                output = value / 1_000_000
            }
        case .phz:
            if to == .hz {
                output = value * 1_000_000
            } else if to == .ehz {
                output = value * 1000
            } else if to == .thz {
                output = value / 1000
            } else if to == .ghz {
                output = value / 1_000_000
            } else if to == .mhz {
                output = value / 1000
            } else if to == .khz {
                output = value / 1_000
            }
        case .thz:
            if to == .hz {
                output = value * 1_000_000_000
            } else if to == .ehz {
                output = value * 1_000_000
            } else if to == .phz {
                output = value * 1000
            } else if to == .ghz {
                output = value / 1000
            } else if to == .mhz {
                output = value / 1_000_000
            } else if to == .khz {
                output = value / 1_000_000_000
            }
        case .ghz:
            if to == .hz {
                output = value * 1_000_000_000_000
            } else if to == .ehz {
                output = value * 1_000_000_000
            } else if to == .phz {
                output = value * 1_000_000
            } else if to == .thz {
                output = value * 1000
            } else if to == .mhz {
                output = value / 1_000
            } else if to == .khz {
                output = value / 1_000_000
            }
        case .mhz:
            if to == .hz {
                output = value * 1_000_000
            } else if to == .ehz {
                output = value * 1000
            } else if to == .phz {
                output = value * 1000
            } else if to == .thz {
                output = value / 1000
            } else if to == .ghz {
                output = value / 1_000
            } else if to == .khz {
                output = value / 1000
            }
        case .khz:
            if to == .hz {
                output = value * 1000
            } else if to == .ehz {
                output = value
            } else if to == .phz {
                output = value * 1000
            } else if to == .thz {
                output = value / 1_000
            } else if to == .ghz {
                output = value / 1_000_000
            } else if to == .mhz {
                output = value * 1000
            }
        }
        return output
    }
}
