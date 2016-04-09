package lt.neworld.randomdecision

import java.awt.BorderLayout
import java.awt.FlowLayout
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

    fun run() {
        JFrame("Random decision").apply {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            isVisible = true
            contentPane.add(content)
            pack()
        }
    }

    companion object {
        const val PADDING = 12
    }
}