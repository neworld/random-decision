package lt.neworld.randomdecision.extensions

/**
 * @author Andrius Semionovas
 * @since 2016-04-17
 */

fun Int.normalize(): Int {
    if (this > 0)
        return 1
    else if (this < 0)
        return -1
    else
        return 0
}