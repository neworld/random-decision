package lt.neworld.randomdecision.extensions

/**
 * @author Andrius Semionovas
 * @since 2016-04-17
 */

inline fun <reified T : CharSequence?> T.emptyToNull(): T? {
    if (this.isNullOrEmpty()) {
        return null
    } else {
        return this
    }
}