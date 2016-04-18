package lt.neworld.randomdecision.entities

/**
 * @author Andrius Semionovas
 * @since 2016-04-18
 */
data class Path(val path: String) {
    fun back(): Path {
        val newPath = path.split("/").dropLast(1).joinToString("/")
        return Path(newPath)
    }

    val isRoot: Boolean
        get() = path.trim('/').isEmpty()

    val currentNode: String
        get() = if (isRoot) "root" else path.split("/").last()

    override fun toString(): String {
        return path
    }
}