package lt.neworld.randomdecision.comparators

import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.Metadata
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Andrius Semionovas
 * @since 2016-04-17
 */
class FolderChooserComparatorTest {

    val fixture = FolderChooserComparator()

    @Test
    fun testCompare_bothFolderWithSameName_zero() {
        val left = folder("foo")
        val right = folder("foo")

        assertEquals(0, fixture.compare(left, right))
    }

    @Test
    fun testCompare_bothFilesWithSameName_zero() {
        val left = file("foo")
        val right = file("foo")

        assertEquals(0, fixture.compare(left, right))
    }

    @Test
    fun testCompare_bothFilesDiffName_nonZero() {
        val left = file("foo")
        val right = file("bar")

        assertTrue(fixture.compare(left, right) > 0)
    }

    @Test
    fun testCompare_bothFoldersDiffName_nonZero() {
        val left = folder("bar")
        val right = folder("foo")

        assertTrue(fixture.compare(left, right) < 0)
    }

    @Test
    fun testCompare_leftFileRightFolderDiffName_nonZero() {
        val left = file("bar")
        val right = folder("foo")

        assertTrue(fixture.compare(left, right) > 0)
    }

    @Test
    fun testCompare_caseInsensitive() {
        val left = file("foo")
        val right = file("Foo")

        assertEquals(0, fixture.compare(left, right))
    }

    fun folder(name: String) = FolderMetadata(name, name)
    fun file(name: String) = Metadata(name, "", "", name)
}