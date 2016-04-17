package lt.neworld.randomdecision.dropbox

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dropbox.core.android.Auth

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
    }
}