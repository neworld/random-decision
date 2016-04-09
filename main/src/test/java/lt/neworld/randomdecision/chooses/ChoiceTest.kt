package lt.neworld.randomdecision.chooses

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

/**
 * @author neworld
 * @since 09/04/16
 */
class ChoiceTest {
    @Test
    fun testOf_emptyString_exception() {
        assertFails { Choice.of("") }
    }

    @Test
    fun testOf_simple() {
        val choice = Choice.of("1 foo")

        assertEquals(Choice("foo", 1), choice)
    }

    @Test
    fun testOf_missingWeight_exception() {
        assertFails { Choice.of("foo") }
    }

    @Test
    fun testOf_missingTitle_exception() {
        assertFails { Choice.of("1") }
    }

    @Test
    fun testOf_twoWordsTitle() {
        val choice = Choice.of("1 foo bar")

        assertEquals(Choice("foo bar", 1), choice)
    }

    @Test
    fun testOf_doubleSpace() {
        val choice = Choice.of("1  foo")

        assertEquals(Choice("foo", 1), choice)
    }
}