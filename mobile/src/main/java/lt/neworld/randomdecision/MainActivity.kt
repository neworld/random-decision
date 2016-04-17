package lt.neworld.randomdecision

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dropbox.core.android.Auth
import lt.neworld.randomdecision.dropbox.DropBoxHelper

class MainActivity : Activity() {

    val dropBoxHelper by lazy {
        DropBoxHelper(this, { onTokenReady() })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()

        dropBoxHelper.onResume()
    }

    private fun onTokenReady() {
        //later will load here
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main_settings -> openSettings().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettings() {
        if (dropBoxHelper.accessToken == null) {
            Auth.startOAuth2Authentication(this, getString(R.string.dropbox_apy_key))
        } else {
            val intent = Intent(this, FolderChooserActivity::class.java)
            startActivity(intent)
        }
    }
}
