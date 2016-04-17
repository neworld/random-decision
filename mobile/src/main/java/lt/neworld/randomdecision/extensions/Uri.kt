package lt.neworld.randomdecision.extensions

import android.net.Uri

/**
 * @author Andrius Semionovas
 * @since 2016-04-17
 */

val Uri.fileName: String
    get() = this.lastPathSegment.split('.').dropLast(1).joinToString(".")