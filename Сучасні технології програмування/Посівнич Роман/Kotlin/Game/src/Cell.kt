import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.border.BevelBorder
import java.awt.Dimension

class Cell(val row: Int, val col: Int) : JButton() {
    var isMine = false
    var isRevealed = false
    var isFlagged = false
    var neighborMines = 0

    init {
        font = Theme.TEXT_FONT
        background = Theme.CELL_CLOSED
        isFocusPainted = false
        // Ефект об'ємної кнопки
        border = BorderFactory.createBevelBorder(BevelBorder.RAISED)
        preferredSize = Dimension(35, 35)
    }

    fun toggleFlag() {
        if (isRevealed) return
        isFlagged = !isFlagged

        if (isFlagged) {
            text = "🚩"
            foreground = java.awt.Color.RED
            background = Theme.CELL_FLAGGED
        } else {
            text = ""
            background = Theme.CELL_CLOSED
        }
    }

    fun setRevealedStyle() {
        isRevealed = true
        background = Theme.CELL_OPENED
        // Робимо рамку плоскою
        border = BorderFactory.createLineBorder(java.awt.Color.GRAY)
        font = Theme.NUMBER_FONT
    }

    fun setBombStyle() {
        background = Theme.CELL_BOMB
        text = "💥"
    }
}