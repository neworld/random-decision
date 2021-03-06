package lt.neworld.randomdecision2

import lt.neworld.randomdecision2.choices.Choice
import lt.neworld.randomdecision2.choices.RandomPicker
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Point
import java.awt.Toolkit
import javax.swing.*

fun setupLookAndFeel() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
}

class Window {
    val label = JLabel("Choose category")

    val categories = JPanel(FlowLayout()).apply {
        border = BorderFactory.createEmptyBorder(PADDING, 0, 0, 0)
    }

    val content = JPanel(BorderLayout()).apply {
        border = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
        add(label, BorderLayout.NORTH)
        add(categories, BorderLayout.SOUTH)
    }


    private val chooseDir = JMenuItem("Choose categories dir").apply {
        addActionListener { openCategoriesDirChooser() }
    }

    val menuBar = JMenuBar().apply {
        val menu = JMenu("File")
        menu.add(chooseDir)
        add(menu)
    }

    val frame = JFrame("Random decision").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        contentPane.add(content)
        jMenuBar = this@Window.menuBar
        pack()
    }

    fun run() {
        frame.isVisible = true
        refreshCategories()
    }

    private fun openCategoriesDirChooser() {
        val chooser = JFileChooser(AppProperties.categoriesDir.parent).apply {
            dialogTitle = "Choose categories dir"
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            isAcceptAllFileFilterUsed = false
        }
        val frame = JFrame("").apply {
            defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            contentPane.add(chooser)
            size = chooser.preferredSize
            isVisible = true
        }
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            AppProperties.categoriesDir = chooser.selectedFile.canonicalFile
            println(AppProperties.categoriesDir)
        }
        frame.dispose()
        refreshCategories()
    }

    private fun refreshCategories() {
        categories.removeAll()
        val choices = openChoices()
                .map { choice ->
                    JButton(choice.title).apply {
                        addActionListener { pickChoice(choice) }
                    }
                }
        for (button in choices) {
            categories.add(button)
        }
        frame.pack()
        moveToCenter()
    }

    private fun openChoices(): List<Choice> {
        return ChoicesLoaderFromDisk(AppProperties.categoriesDir).load()
    }

    private var firstMoveToCenter: Boolean = true

    private fun moveToCenter() {
        if (!firstMoveToCenter) {
            return
        }
        firstMoveToCenter = false
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        frame.location = Point((screenSize.width - frame.width) / 2, (screenSize.height - frame.height) / 2)
    }

    private fun pickChoice(choice: Choice) {
        var path = RandomPicker(choice).pick()
        label.text = path.map { it.title }.joinToString(" > ")
        frame.pack()
    }

    companion object {
        const val PADDING = 12
    }
}