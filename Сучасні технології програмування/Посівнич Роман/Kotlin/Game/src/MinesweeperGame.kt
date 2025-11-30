import javax.swing.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import kotlin.math.abs
import kotlin.random.Random

class MinesweeperGame : JFrame("Minesweeper Pro") {

    private val topPanel = JPanel()
    private val cardsPanel = JPanel(CardLayout())
    private val gamePanel = JPanel()
    private val pausePanel = JPanel()

    private val lblTime = JLabel("⏳ 0")
    private val lblBombsLeft = JLabel("💣 0")
    private val btnNewGame = JButton("🙂")
    private val btnPause = JButton("||")

    private var currentDifficulty = Difficulty.MEDIUM
    private lateinit var board: Array<Array<Cell>>

    private var isGameOver = false
    private var isPaused = false
    private var isFirstClick = true
    private var flagsCount = 0
    private var secondsPlayed = 0
    private var gameTimer: Timer? = null

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        isResizable = false
        contentPane.background = Theme.BG_COLOR

        setupTopPanel()
        setupCenterPanels()

        add(topPanel, BorderLayout.NORTH)
        add(cardsPanel, BorderLayout.CENTER)

        startNewGame(Difficulty.MEDIUM)
    }

    private fun setupTopPanel() {
        topPanel.layout = BorderLayout()
        topPanel.border = BorderFactory.createEmptyBorder(10, 15, 10, 15)
        topPanel.background = Theme.BG_COLOR

        val infoFont = Font("SansSerif", Font.BOLD, 16)
        lblBombsLeft.font = infoFont
        lblTime.font = infoFont

        val btnSize = Dimension(45, 45)

        btnNewGame.font = Font("Segoe UI Emoji", Font.PLAIN, 24)
        configureButton(btnNewGame, btnSize)
        btnNewGame.addActionListener { showDifficultyDialog() }

        btnPause.font = Font("SansSerif", Font.BOLD, 16)
        configureButton(btnPause, btnSize)
        btnPause.addActionListener { togglePause() }

        val buttonsContainer = JPanel(GridLayout(1, 2, 8, 0))
        buttonsContainer.background = Theme.BG_COLOR
        buttonsContainer.add(btnNewGame)
        buttonsContainer.add(btnPause)

        val centerWrapper = JPanel(GridBagLayout())
        centerWrapper.background = Theme.BG_COLOR
        centerWrapper.add(buttonsContainer)

        topPanel.add(lblBombsLeft, BorderLayout.WEST)
        topPanel.add(centerWrapper, BorderLayout.CENTER)
        topPanel.add(lblTime, BorderLayout.EAST)
    }

    private fun configureButton(btn: JButton, size: Dimension) {
        btn.preferredSize = size
        btn.minimumSize = size
        btn.maximumSize = size
        btn.isFocusable = false
        btn.isFocusPainted = false
        btn.background = Color.WHITE
        btn.border = BorderFactory.createLineBorder(Color.GRAY, 1)
        btn.margin = Insets(0, 0, 0, 0)
        btn.horizontalAlignment = SwingConstants.CENTER
        btn.verticalAlignment = SwingConstants.CENTER
    }

    private fun setupCenterPanels() {
        gamePanel.background = Color.GRAY
        gamePanel.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)

        pausePanel.layout = GridBagLayout()
        pausePanel.background = Theme.PAUSE_BG
        val pauseLabel = JLabel("ГРА НА ПАУЗІ")
        pauseLabel.font = Font("Arial", Font.BOLD, 24)
        pauseLabel.foreground = Color.DARK_GRAY
        pausePanel.add(pauseLabel)

        cardsPanel.add(gamePanel, "GAME")
        cardsPanel.add(pausePanel, "PAUSED")
    }

    private fun togglePause() {
        if (isGameOver) return

        val cl = cardsPanel.layout as CardLayout
        isPaused = !isPaused

        if (isPaused) {
            gameTimer?.stop()
            btnPause.text = "▶"
            btnNewGame.isEnabled = false
            cl.show(cardsPanel, "PAUSED")
        } else {
            if (!isFirstClick) gameTimer?.start()
            btnPause.text = "||"
            btnNewGame.isEnabled = true
            cl.show(cardsPanel, "GAME")
        }
    }

    private fun showDifficultyDialog() {
        val options = Difficulty.values()
        val choice = JOptionPane.showOptionDialog(
            this, "Оберіть рівень:", "Налаштування",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options.map { it.label }.toTypedArray(), options[0].label
        )
        if (choice >= 0) startNewGame(options[choice])
    }

    private fun startNewGame(difficulty: Difficulty) {
        currentDifficulty = difficulty
        isGameOver = false
        isPaused = false
        isFirstClick = true
        flagsCount = 0
        secondsPlayed = 0

        lblTime.text = "⏳ 0"
        btnNewGame.text = "🙂"
        btnNewGame.isEnabled = true
        btnPause.text = "||"
        btnPause.isEnabled = true
        updateBombCounter()

        gameTimer?.stop()

        val cl = cardsPanel.layout as CardLayout
        cl.show(cardsPanel, "GAME")

        createBoardUI()
        pack()
        if (width < 450) setSize(450, height)
        setLocationRelativeTo(null)
    }

    private fun createBoardUI() {
        gamePanel.removeAll()
        val rows = currentDifficulty.rows
        val cols = currentDifficulty.cols
        gamePanel.layout = GridLayout(rows, cols)

        board = Array(rows) { r -> Array(cols) { c -> Cell(r, c) } }

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                gamePanel.add(board[r][c])
                board[r][c].addMouseListener(object : MouseAdapter() {
                    override fun mousePressed(e: MouseEvent) {
                        handleMouseClick(e, board[r][c])
                    }
                })
            }
        }
        gamePanel.revalidate()
    }

    private fun placeMines(safeRow: Int, safeCol: Int) {
        var minesPlaced = 0
        while (minesPlaced < currentDifficulty.mines) {
            val r = Random.nextInt(currentDifficulty.rows)
            val c = Random.nextInt(currentDifficulty.cols)
            val isSafeZone = abs(r - safeRow) <= 1 && abs(c - safeCol) <= 1
            if (!board[r][c].isMine && !isSafeZone) {
                board[r][c].isMine = true
                minesPlaced++
            }
        }
    }

    private fun calculateNumbers() {
        val rows = currentDifficulty.rows
        val cols = currentDifficulty.cols
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (!board[r][c].isMine) {
                    board[r][c].neighborMines = countMinesAround(r, c)
                }
            }
        }
    }

    private fun countMinesAround(r: Int, c: Int): Int {
        var count = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val newR = r + i
                val newC = c + j
                if (newR in 0 until currentDifficulty.rows && newC in 0 until currentDifficulty.cols) {
                    if (board[newR][newC].isMine) count++
                }
            }
        }
        return count
    }

    private fun handleMouseClick(e: MouseEvent, cell: Cell) {
        if (isGameOver || cell.isRevealed || isPaused) return

        if (SwingUtilities.isRightMouseButton(e)) {
            cell.toggleFlag()
            if (cell.isFlagged) flagsCount++ else flagsCount--
            updateBombCounter()
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (!cell.isFlagged) {
                if (isFirstClick) {
                    isFirstClick = false
                    placeMines(cell.row, cell.col)
                    calculateNumbers()
                    startTimer()
                }
                btnNewGame.text = "😮"
                Timer(200) { if (!isGameOver) btnNewGame.text = "🙂" }.apply { isRepeats = false; start() }
                openCell(cell)
            }
        }
    }

    private fun startTimer() {
        gameTimer = Timer(1000) {
            secondsPlayed++
            lblTime.text = "⏳ $secondsPlayed"
        }.apply { start() }
    }

    private fun updateBombCounter() {
        val left = currentDifficulty.mines - flagsCount
        lblBombsLeft.text = "💣 $left"
    }

    private fun openCell(cell: Cell) {
        if (cell.isRevealed || cell.isFlagged) return
        cell.setRevealedStyle()

        if (cell.isMine) {
            gameOver(false)
            cell.setBombStyle()
        } else {
            if (cell.neighborMines > 0) {
                cell.text = cell.neighborMines.toString()
                cell.foreground = getColorForNumber(cell.neighborMines)
            } else {
                val r = cell.row
                val c = cell.col
                for (i in -1..1) {
                    for (j in -1..1) {
                        val newR = r + i
                        val newC = c + j
                        if (newR in 0 until currentDifficulty.rows && newC in 0 until currentDifficulty.cols) {
                            openCell(board[newR][newC])
                        }
                    }
                }
            }
            checkWin()
        }
    }

    private fun gameOver(win: Boolean) {
        isGameOver = true
        gameTimer?.stop()
        btnPause.isEnabled = false
        if (win) {
            btnNewGame.text = "😎"
            JOptionPane.showMessageDialog(this, "Перемога! Час: $secondsPlayed сек.")
        } else {
            btnNewGame.text = "😵"
            revealAllMines()
            JOptionPane.showMessageDialog(this, "Вибух! Гру закінчено.")
        }
    }

    private fun revealAllMines() {
        for (r in 0 until currentDifficulty.rows) {
            for (c in 0 until currentDifficulty.cols) {
                if (board[r][c].isMine && !board[r][c].isRevealed) {
                    board[r][c].setRevealedStyle()
                    board[r][c].text = "💣"
                    board[r][c].foreground = Color.BLACK
                }
                if (!board[r][c].isMine && board[r][c].isFlagged) {
                    board[r][c].background = Color.PINK
                    board[r][c].text = "❌"
                }
            }
        }
    }

    private fun checkWin() {
        var unrevealedSafeCells = 0
        val rows = currentDifficulty.rows
        val cols = currentDifficulty.cols
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (!board[r][c].isMine && !board[r][c].isRevealed) unrevealedSafeCells++
            }
        }
        if (unrevealedSafeCells == 0) gameOver(true)
    }

    private fun getColorForNumber(num: Int): Color {
        return when(num) {
            1 -> Color(25, 118, 210)
            2 -> Color(56, 142, 60)
            3 -> Color(211, 47, 47)
            4 -> Color(123, 31, 162)
            5 -> Color(255, 143, 0)
            else -> Color.BLACK
        }
    }
}