package lt.neworld.randomdecision

import lt.neworld.randomdecision.chooses.Choice
import lt.neworld.randomdecision.chooses.RandomPicker

/**
 * @author neworld
 * @since 13/04/16
 */

class CLI {
    val choices: List<Choice> by lazy {
        ChoicesLoaderFromDisk(AppProperties.categoriesDir).load()
    }

    fun run() {
        printChoices()
        val choice = askForChoice()
        val picked = RandomPicker(choice).pick()
        println("Picked: ${picked.map { it.title }.joinToString(" -> ")}")
    }

    private fun askForChoice(): Choice {
        while (true) {
            try {
                print("Select choice: ")
                val index = readLine()!!.toInt()
                return choices[index - 1]
            } catch (e: Exception) {
                println("Just must enter correct number of choice")
            }
        }
    }

    private fun printChoices() {
        println("Available choices:")
        choices.forEachIndexed { index, choice ->
            println("${index + 1}: ${choice.title}")
        }
        println()
    }
}