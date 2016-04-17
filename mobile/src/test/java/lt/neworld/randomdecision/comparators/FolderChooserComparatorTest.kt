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
        var left = folder("foo")
        var right = folder("foo")

        assertEquals(0, fixture.compare(left, right))
    }

    @Test
    fun testCompare_bothFilesWithSameName_zero() {
        var left = file("foo")
        var right = file("foo")

        assertEquals(0, fixture.compare(left, right))
    }

    @Test
    fun testCompare_bothFilesDiffName_nonZero() {
        var left = file("foo")
        var right = file("bar")

        assertTrue(fixture.compare(left, right) > 0)
    }

    @Test
    fun testCompare_bothFoldersDiffName_nonZero() {
        var left = folder("bar")
        var right = folder("foo")

        assertTrue(fixture.compare(left, right) < 0)
    }

    @Test
    fun testCompare_leftFileRightFolderDiffName_nonZero() {
        var left = file("bar")
        var right = folder("foo")

        assertTrue(fixture.compare(left, right) > 0)
    }

    @Test
    fun testCompare_caseInsensitive() {
        var left = file("foo")
        var right = file("Foo")

        assertEquals(0, fixture.compare(left, right))
    }

    fun folder(name: String) = FolderMetadata(name, "", "", name)
    fun file(name: String) = Metadata(name, "", "", name)
}