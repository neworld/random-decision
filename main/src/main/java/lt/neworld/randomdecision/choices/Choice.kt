package lt.neworld.randomdecision.choices

/**
 * @author neworld
 * @since 09/04/16
 */
data class Choice(
        val title: String,
        val weight: Int,
        val children:MutableList<Choice> = arrayListOf()
) {
    companion object {
        fun of(line: String): Choice {
            val pieces = line.split("\\W+".toRegex(), limit = 2)
            if (pieces.size < 2) {
                throw IllegalArgumentException("Format must be: %d %s")
            }
            return Choice(pieces.last(), pieces.first().toInt())
        }
    }
}