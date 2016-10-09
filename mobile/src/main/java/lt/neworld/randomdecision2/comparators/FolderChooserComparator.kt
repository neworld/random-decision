package lt.neworld.randomdecision2.comparators

import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.Metadata
import lt.neworld.randomdecision2.extensions.normalize
import java.util.*

/**
 * @author Andrius Semionovas
 * @since 2016-04-17
 */
class FolderChooserComparator : Comparator<Metadata> {
    override fun compare(lhs: Metadata, rhs: Metadata): Int {
        var lhsWeight = 0
        var rhsWeight = 0

        if (lhs !is FolderMetadata) {
            lhsWeight += 10
        }

        if (rhs !is FolderMetadata) {
            rhsWeight += 10
        }

        val nameWeight = lhs.name.toLowerCase().compareTo(rhs.name.toLowerCase()).normalize()

        return lhsWeight - rhsWeight + nameWeight
    }
}