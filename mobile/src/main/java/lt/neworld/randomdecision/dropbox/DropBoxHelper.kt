package lt.neworld.randomdecision.dropbox

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.android.Auth
import com.dropbox.core.http.OkHttpRequestor
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.ListFolderResult
import rx.Single
import rx.schedulers.Schedulers
import java.io.InputStream

/**
 * @author Andrius Semionovas
 * @since 2016-04-17
 */
class DropBoxHelper(
        val context: Context,
        val onTokenReady: (String) -> Unit
) {

    private val prefs by lazy {
        context.getSharedPreferences(PREFS, MODE_PRIVATE)
    }

    var accessToken: String?
        get() = prefs.getString(KEY_ACCESS_TOKEN, null)
        private set(value) {
            prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()
        }

    var path: String
        get() = prefs.getString(KEY_PATH, "")
        set(value) {
            prefs.edit().putString(KEY_PATH, value).apply()
        }

    val client by lazy {
        if (accessToken == null) {
            throw RuntimeException("Can't get client without access token")
        }

        val config = DbxRequestConfig(
                "random-decision",
                "lt-LT",
                OkHttpRequestor.INSTANCE
        )

        DbxClientV2(config, accessToken)
    }

    fun listFiles(path: String): Single<ListFolderResult> {
        return Single.create<ListFolderResult> {
            val files = client.files().listFolder(path)
            it.onSuccess(files)
        }.subscribeOn(Schedulers.io())
    }

    fun openFile(path: String): Single<InputStream> {
        return Single.create<InputStream> {
            val inputStream = client.files().download(path).inputStream
            it.onSuccess(inputStream)
        }.subscribeOn(Schedulers.io())
    }

    fun onResume() {
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token()
        }

        accessToken?.run {
            onTokenReady(this)
        }
    }

    companion object {
        private const val PREFS = "dropbox"
        private const val KEY_ACCESS_TOKEN = "access-token"
        private const val KEY_PATH = "path"
    }
}