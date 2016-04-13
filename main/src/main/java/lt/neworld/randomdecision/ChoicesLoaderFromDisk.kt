package lt.neworld.randomdecision

import lt.neworld.randomdecision.chooses.Builder
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @author neworld
 * @since 13/04/16
 */

class ChoicesLoaderFromDisk(val path: File) {
    fun load() = AppProperties.categoriesDir
            .listFiles { file, title -> title.endsWith(FILE_SUFFIX) }
            .map {
                val reader = InputStreamReader(FileInputStream(it), "UTF-8")
                val fileName = it.name.replace(FILE_SUFFIX, "")
                Builder(fileName, reader).build()
            }

    companion object {
        const val FILE_SUFFIX = ".choices"
    }
}