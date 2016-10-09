package lt.neworld.randomdecision2.choices

import org.junit.Test
import java.io.StringReader
import kotlin.test.assertEquals

/**
 * @author neworld
 * @since 09/04/16
 */
class BuilderTest {
    @Test
    fun testBuild_empty_defaultChoice() {
        val input = StringReader("")

        val actual = Builder("foo", input).build()

        assertEquals(Choice("foo", 1), actual)
    }

    @Test
    fun testBuild_withOneChoice_oneChild() {
        val input = StringReader("2 bar")

        val actual = Builder("foo", input).build()

        val expected = Choice("foo", 1, arrayListOf(Choice("bar", 2)))
        assertEquals(expected, actual)
    }

    @Test
    fun testBuild_notTrimmedInput_success() {
        val input = StringReader("   3 bar   ")

        val actual = Builder("foo", input).build()

        val expected = Choice("foo", 1, arrayListOf(Choice("bar", 3)))
        assertEquals(expected, actual)
    }

    @Test
    fun testBuild_emptyLine_default() {
        val input = StringReader(" ")

        val actual = Builder("foo", input).build()

        assertEquals(Choice("foo", 1), actual)
    }

    @Test
    fun testBuild_withTwoChoices_twoChildren() {
        val input = StringReader("""
            2 foo
            3 bar
        """)

        val actual = Builder("foo", input).build()

        val children = arrayListOf(Choice("foo", 2), Choice("bar", 3))
        val expected = Choice("foo", 1, children)
        assertEquals(expected, actual)
    }

    @Test
    fun testBuild_emptyListOfChildren() {
        val input = StringReader("""
            1 bar >
            END
        """)

        val actual = Builder("foo", input).build()

        val children = arrayListOf(Choice("bar", 1))
        val expected = Choice("foo", 1, children)
        assertEquals(expected, actual)
    }

    @Test
    fun testBuild_childOfChild() {
        val input = StringReader("""
            1 bar >
                2 bar
            END
        """)

        val actual = Builder("foo", input).build()

        val children = arrayListOf(Choice("bar", 1, arrayListOf(Choice("bar", 2))))
        val expected = Choice("foo", 1, children)
        assertEquals(expected, actual)
    }
}