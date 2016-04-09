package lt.neworld.randomdecision

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

/**
 * @author neworld
 * @since 09/04/16
 */

object AppProperties {
    private const val PROP_CATEGORIES_DIR = "categories_dir"

    private val file = File(".properties")

    private val properties by lazy {
        Properties().apply {
            if (!file.exists()) {
                file.createNewFile()
            }
            val fin = FileInputStream(file)
            load(fin)
            fin.close()
        }
    }

    private fun save() {
        val fout = FileOutputStream(file)
        properties.store(fout, "")
        fout.close()
    }

    var categoriesDir: File
        get() = properties.getProperty(PROP_CATEGORIES_DIR).let { File(it ?: ".") }
        set(value) {
            properties.setProperty(PROP_CATEGORIES_DIR, value.canonicalPath)
            save()
        }
}