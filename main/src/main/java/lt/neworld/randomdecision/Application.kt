package lt.neworld.randomdecision

import lt.neworld.randomdecision.chooses.Builder
import lt.neworld.randomdecision.chooses.Choice
import lt.neworld.randomdecision.chooses.RandomPicker
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.swing.*

/**
 * @author neworld
 * @since 09/04/16
 */

fun main(args: Array<String>) {
    Application().run()
}

class Application {
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
        jMenuBar = this@Application.menuBar
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
        val choices = AppProperties.categoriesDir
                .listFiles { file, title -> title.endsWith(FILE_SUFFIX) }
                .map {
                    val reader = InputStreamReader(FileInputStream(it))
                    val fileName = it.name.replace(FILE_SUFFIX, "")
                    Builder(fileName, reader).build()
                }
                .map { choice ->
                    JButton(choice.title).apply {
                        addActionListener { pickChoice(choice) }
                    }
                }
        for (button in choices) {
            categories.add(button)
        }
        frame.pack()
    }

    private fun pickChoice(choice: Choice) {
        var path = RandomPicker(choice).pick()
        label.text = path.map { it.title }.joinToString(" > ")
        frame.pack()
    }

    companion object {
        const val FILE_SUFFIX = ".choices"
        const val PADDING = 12
    }
}