package lt.neworld.randomdecision2.extensions

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author Andrius Semionovas
 * *
 * @since 2016-04-17
 */
class CharSequenceKtTest {
    @Test
    fun testEmptyToNull_notEmpty_same() {
        assertEquals("foo", "foo".emptyToNull())
    }

    @Test
    fun testEmptyToNull_empty_null() {
        assertNull("".emptyToNull())
    }

    @Test
    fun testEmptyToNull_null_null() {
        assertNull((null as String?).emptyToNull())
    }
}