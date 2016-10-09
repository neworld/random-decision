package lt.neworld.randomdecision2.extensions

import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Andrius Semionovas
 * *
 * @since 2016-04-17
 */
class IntKtTest {

    @Test
    fun testNormalize() {
        assertEquals(-1, -50.normalize())
        assertEquals(0, 0.normalize())
        assertEquals(1, 40.normalize())
    }
}