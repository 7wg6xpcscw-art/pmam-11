import java.awt.Color
import java.awt.Font

// Рівні складності
enum class Difficulty(val rows: Int, val cols: Int, val mines: Int, val label: String) {
    EASY(9, 9, 10, "Новачок (9x9)"),
    MEDIUM(16, 16, 40, "Любитель (16x16)"),
    HARD(16, 30, 99, "Професіонал (30x16)")
}

// Тема оформлення
object Theme {
    val BG_COLOR = Color(230, 230, 230)
    val CELL_CLOSED = Color(100, 100, 100) // Темно-сірий
    val CELL_OPENED = Color(250, 250, 250) // Майже білий
    val CELL_FLAGGED = Color(255, 213, 79)
    val CELL_BOMB = Color(229, 57, 53)
    val PAUSE_BG = Color(200, 230, 255)

    val TEXT_FONT = Font("Segoe UI Emoji", Font.BOLD, 16)
    val NUMBER_FONT = Font("SansSerif", Font.BOLD, 18)
}