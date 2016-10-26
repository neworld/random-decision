package lt.neworld.randomdecision2.choices

/**
 * @author neworld
 * @since 09/04/16
 */
data class Choice(
        val title: String,
        val weight: Int,
        val children:MutableList<Choice> = arrayListOf()
): Comparable<Choice> {
    override fun compareTo(other: Choice): Int {
        return this.title.first() - other.title.first()
    }

    companion object {
        fun of(line: String): Choice {
            val pieces = line.split("\\W+".toRegex(), limit = 2)
            if (pieces.size < 2) {
                throw IllegalArgumentException("Format must be: %d %s")
            }
            return Choice(pieces.last(), pieces.first().toInt())
        }

        const val FILE_EXT = ".choices"

        fun getName(fileName: String) = fileName.split(".").dropLast(1).joinToString(".")
    }
}