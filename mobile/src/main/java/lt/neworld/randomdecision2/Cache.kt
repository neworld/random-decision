package lt.neworld.randomdecision2

import android.content.Context
import lt.neworld.randomdecision2.choices.Builder
import lt.neworld.randomdecision2.choices.Choice
import rx.Observable
import rx.Single
import rx.subjects.PublishSubject
import java.io.File
import java.io.InputStream

/**
 * @author Andrius Semionovas
 * @since 2016-10-26
 */
class Cache(private val context: Context) {
    private val writer = PublishSubject.create<CacheInfo>()

    private var cleared = false

    init {
        writer.subscribe {
            if (!cleared) {
                cacheDir.deleteRecursively()
                cleared = true
            }
            cacheDir.mkdirs()
            val writeTo = File(cacheDir, it.name)
            it.input.copyTo(writeTo.outputStream())
        }
    }

    private val cacheDir by lazy {
        File(context.cacheDir, DIR)
    }

    fun loadFromCache(): Single<List<Choice>> {
        return Observable.just(cacheDir)
                .flatMap { Observable.from(it.listFiles() ?: emptyArray()) }
                .filter { it.name.endsWith(Choice.FILE_EXT) }
                .map {
                    val title = Choice.getName(it.name)
                    val streamReader = it.reader()
                    Builder(title, streamReader).build()
                }
                .toSortedList(Choice::compareTo)
                .toSingle()
    }

    fun saveToCache(name: String, input: InputStream) {
        writer.onNext(CacheInfo(name, input))
    }

    fun release() {
        writer.onCompleted()
    }

    data class CacheInfo(val name: String, val input: InputStream)

    companion object {
        private const val DIR = "choices"
    }
}