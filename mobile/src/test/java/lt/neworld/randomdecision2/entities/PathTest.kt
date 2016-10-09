package lt.neworld.randomdecision2.entities

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Andrius Semionovas
 * *
 * @since 2016-04-18
 */
class PathTest {

    @Test
    fun testBack_singleNode_empty() {
        assertEquals(Path(""), Path("/foo").back())
    }

    @Test
    fun testBack_root_root() {
        assertEquals(Path(""), Path("/").back())
        assertEquals(Path(""), Path("").back())
    }

    @Test
    fun testBack_twoNodes_firstNode() {
        assertEquals(Path("/foo"), Path("/foo/bar").back())
    }

    @Test
    fun testGetCurrentNode_root_root() {
        assertEquals("root", Path("").currentNode)
        assertEquals("root", Path("/").currentNode)
    }

    @Test
    fun testGetCurrentNode_node_nodeName() {
        assertEquals("foo", Path("/foo").currentNode)
    }

    @Test
    fun testIsRoot() {
        assertTrue(Path("").isRoot)
        assertTrue(Path("/").isRoot)
        assertFalse(Path("/foo").isRoot)
    }
}