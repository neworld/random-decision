package lt.neworld.randomdecision.extensions

import android.net.Uri
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import kotlin.test.assertEquals

/**
 * @author Andrius Semionovas
 * *
 * @since 2016-04-17
 */
@RunWith(RobolectricGradleTestRunner::class)
class UriKtTest {

    @Test
    fun testGetFileName() {
        val fixture = Uri.parse("http://foo.bar/filename.txt")

        val value = fixture.fileName

        assertEquals("filename", value)
    }
}