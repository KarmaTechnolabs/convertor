//
//  HistoryTableViewCell.swift
// Converter
//
//  Created by karma on 15/02/2024.
//

import UIKit

class HistoryTableViewCell: UITableViewCell {

    @IBOutlet weak var historyConversionText: UILabel!
    @IBOutlet weak var historyTypeIcon: UIImageView!
    
    override func layoutSubviews() {
        super.layoutSubviews()
        contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8))
    }
}
