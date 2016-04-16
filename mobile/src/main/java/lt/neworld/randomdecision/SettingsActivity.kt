package lt.neworld.randomdecision

import android.app.ListActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.uri_list_item.view.*

class SettingsActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings_add -> openFilePicker().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            CODE_GET_CONTENT -> add(data.data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, CODE_GET_CONTENT)
    }

    private fun createAdapter() {
        listAdapter = UriListAdapter().apply {
            addAll(items)
        }
    }

    private fun add(uri: Uri) {
        items += uri
        createAdapter()
    }

    private fun delete(uri: Uri) {
        items = items.filter { it != uri }
        createAdapter()
    }

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    var items: List<Uri>
        get() = prefs.getStringSet(PREF_KEY_URI_LIST, setOf()).map { Uri.parse(it) }.toList()
        set(items) {
            prefs.edit()
                    .putStringSet(PREF_KEY_URI_LIST, items.map { it.toString() }.toSet())
                    .apply()
        }

    inner class UriListAdapter : ArrayAdapter<Uri>(this, 0) {
        val inflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view = convertView ?: inflater.inflate(R.layout.uri_list_item, parent, false).apply {
                uri_list_item_delete.setOnClickListener { delete(tag as Uri) }
            }
            val item = getItem(position)
            view.uri_list_item_title.text = item.toString()
            view.tag = item
            return view
        }
    }

    companion object {
        const val CODE_GET_CONTENT = 1
    }
}
