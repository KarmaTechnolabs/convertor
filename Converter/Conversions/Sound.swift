//
//  Sound.swift
//  Converter
//
//  Created by Karma on 18/03/24.
//

import Foundation
enum SoundUnit {
    case decibel
    case bel
    case neper

    static let allUnits = [decibel, bel, neper]
}

struct Sound {
    let value: Double
    let unit: SoundUnit

    init(unit: SoundUnit, value: Double) {
        self.value = value
        self.unit = unit
    }

    func convert(unit to: SoundUnit) -> Double {
        var output = 0.0

        switch unit {
        case .decibel:
            if to == .bel {
                output = value / 10
            } else if to == .neper {
                output = value / 8.686
            }
        case .bel:
            if to == .decibel {
                output = value * 10
            } else if to == .neper {
                output = value / 2.3026
            }
        case .neper:
            if to == .decibel {
                output = value * 8.686
            } else if to == .bel {
                output = value * 2.3026
            }
        }

        return output
    }
}

let sound = Sound(unit: .decibel, value: 80)
let convertedSoundValue = sound.convert(unit: .bel) // Output: 8.0
