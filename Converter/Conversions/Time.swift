//
//  Time.swift
//  Converter

//
//  Created by karma on 25/01/2024.



import Foundation

enum TimeUnit {
    case hour
    case minute
    case second
    case millisecond
    case microsecond
    
    static let getAllUnits = [hour, minute, second, millisecond, microsecond]
}

struct Time {
    let value: Double
    let unit: TimeUnit
    
    init(unit: TimeUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: TimeUnit) -> Double {
        var output = 0.0
        
        switch unit {
        case .hour:
            if to == .minute {
                output = value * 60
            } else if to == .second {
                output = value * 3600
            } else if to == .millisecond {
                output = value * 3.6e+6
            } else if to == .microsecond {
                output = value * 3.6e+9
            }
        case .minute:
            if to == .hour {
                output = value / 60
            } else if to == .second {
                output = value * 60
            } else if to == .millisecond {
                output = value * 60000
            } else if to == .microsecond {
                output = value * 60000000
            }
        case .second:
            if to == .hour {
                output = value / 3600
            } else if to == .minute {
                output = value / 60
            } else if to == .millisecond {
                output = value * 1000
            } else if to == .microsecond {
                output = value * 1e+6
            }
        case .millisecond:
            if to == .hour {
                output = value / 3.6e+6
            } else if to == .minute {
                output = value / 60000
            } else if to == .second {
                output = value / 1000
            } else if to == .microsecond {
                output = value * 1000
            }
        case .microsecond:
            if to == .hour {
                output = value / 3.6e+9
            } else if to == .minute {
                output = value / 60000000
            } else if to == .second {
                output = value / 1e+6
            } else if to == .millisecond {
                output = value / 1000
            }
        }
        
        return output
    }
}

