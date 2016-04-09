package lt.neworld.randomdecision.chooses

import java.io.BufferedReader
import java.io.InputStream
import java.io.Reader
import java.util.*

/**
 * @author neworld
 * @since 09/04/16
 */

class Builder(
        title: String,
        input: Reader
) {

    private val bufferedReader = BufferedReader(input)
    private val root = Choice(title, 1)
    private val stack = Stack<Choice>().apply {
        add(root)
    }

    fun build(): Choice {
        bufferedReader.lines().forEach {
            val prepared = it.trim()
            if (prepared.isEmpty()) {
                return@forEach
            }
            if (prepared == END) {
                stack.pop()
                return@forEach
            }
            val child = Choice.of(prepared.replace(">", "").trim())
            stack.last().children += child
            if (prepared.last() == '>') {
                stack += child
            }
        }

        return root
    }

    companion object {
        const val END = "END"
    }
}