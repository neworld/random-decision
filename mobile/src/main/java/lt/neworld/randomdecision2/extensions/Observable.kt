package lt.neworld.randomdecision2.extensions

import rx.Observer
import rx.subjects.Subject

/**
 * @author Andrius Semionovas
 * @since 2016-10-26
 */

fun <T, R> Subject<T, R>.ignoreOnComplete(): Observer<T> {
    return object : Observer<T> {
        override fun onError(e: Throwable) {
            this@ignoreOnComplete.onError(e)
        }

        override fun onCompleted() {
        }

        override fun onNext(t: T) {
            this@ignoreOnComplete.onNext(t)
        }
    }
}