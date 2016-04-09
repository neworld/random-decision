package lt.neworld.randomdecision.chooses

import java.security.SecureRandom
import java.util.*

/**
 * @author neworld
 * @since 09/04/16
 */

class RandomPicker(val rootChoice: Choice) {
    fun pick(): List<Choice> {
        val random = Random()
        val result = arrayListOf(rootChoice)
        while (result.last().children.size > 1) {
            val children = result.last().children
            val sum = children.sumBy { it.weight }
            var ticket = random.nextInt(sum)
            result += children.first {
                ticket -= it.weight
                ticket < 0
            }
        }
        return result
    }
}