//
//  ConstantTableViewCell.swift
// Converter
//
//  Created by karma on 19/02/2024.
//

import UIKit

class ConstantTableViewCell: UITableViewCell {

    @IBOutlet weak var constantIcon: UIImageView!
    @IBOutlet weak var constantName: UILabel!
    @IBOutlet weak var constantFormula: UILabel!
    
    override func layoutSubviews() {
        super.layoutSubviews()
        contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8))
    }
}
