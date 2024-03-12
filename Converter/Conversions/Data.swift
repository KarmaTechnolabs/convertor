//
//  Data.swift
//  Converter
//
//  Created by Karma on 07/03/24.


import Foundation
enum DataUnit {
    case bit
    case byte
    case kilobyte
    case megabyte
    case gigabyte
    case terabyte
    
    static let allUnits = [bit, byte, kilobyte, megabyte, gigabyte, terabyte]
}

struct DataSize {
    let value: Double
    let unit: DataUnit
    
    init(unit: DataUnit, value: Double) {
        self.value = value
        self.unit = unit
    }
    
    func convert(unit to: DataUnit) -> Double {
        var output = 0.0
        
        switch unit {
            case .bit:
                switch to {
                case .byte:
                    output = value / 8
                case .kilobyte:
                    output = value / 8 / 1024
                case .megabyte:
                    output = value / 8 / 1024 / 1024
                case .gigabyte:
                    output = value / 8 / 1024 / 1024 / 1024
                case .terabyte:
                    output = value / 8 / 1024 / 1024 / 1024 / 1024
                default:
                    break
                }
            case .byte:
                switch to {
                case .bit:
                    output = value * 8
                case .kilobyte:
                    output = value / 1024
                case .megabyte:
                    output = value / 1024 / 1024
                case .gigabyte:
                    output = value / 1024 / 1024 / 1024
                case .terabyte:
                    output = value / 1024 / 1024 / 1024 / 1024
                default:
                    break
                }
            case .kilobyte:
                switch to {
                case .bit:
                    output = value * 8 * 1024
                case .byte:
                    output = value * 1024
                case .megabyte:
                    output = value / 1024
                case .gigabyte:
                    output = value / 1024 / 1024
                case .terabyte:
                    output = value / 1024 / 1024 / 1024
                default:
                    break
                }
            case .megabyte:
                switch to {
                case .bit:
                    output = value * 8 * 1024 * 1024
                case .byte:
                    output = value * 1024 * 1024
                case .kilobyte:
                    output = value * 1024
                case .gigabyte:
                    output = value / 1024
                case .terabyte:
                    output = value / 1024 / 1024
                default:
                    break
                }
            case .gigabyte:
                switch to {
                case .bit:
                    output = value * 8 * 1024 * 1024 * 1024
                case .byte:
                    output = value * 1024 * 1024 * 1024
                case .kilobyte:
                    output = value * 1024 * 1024
                case .megabyte:
                    output = value * 1024
                case .terabyte:
                    output = value / 1024
                default:
                    break
                }
            case .terabyte:
                switch to {
                case .bit:
                    output = value * 8 * 1024 * 1024 * 1024 * 1024
                case .byte:
                    output = value * 1024 * 1024 * 1024 * 1024
                case .kilobyte:
                    output = value * 1024 * 1024 * 1024
                case .megabyte:
                    output = value * 1024 * 1024
                case .gigabyte:
                    output = value * 1024
                default:
                    break
                }
            }
            
            return output
        }
}

let dataSize = DataSize(unit: .kilobyte, value: 1)
let convertedValue = dataSize.convert(unit: .byte) // Output: 1024
