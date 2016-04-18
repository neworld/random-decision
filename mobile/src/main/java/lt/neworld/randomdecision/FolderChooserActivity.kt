package lt.neworld.randomdecision

import android.app.ListActivity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.Metadata
import lt.neworld.randomdecision.comparators.FolderChooserComparator
import lt.neworld.randomdecision.dropbox.DropBoxHelper
import lt.neworld.randomdecision.entities.Path
import lt.neworld.randomdecision.extensions.hideProgress
import lt.neworld.randomdecision.extensions.showProgress
import lt.neworld.randomdecision.extensions.showToast
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers

class FolderChooserActivity : ListActivity() {

    val dropBoxHelper by lazy {
        DropBoxHelper(this, { onTokenReady() })
    }

    lateinit var currentPath: Path

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentPath = Path(dropBoxHelper.path)
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
            val file = adapterView.getItemAtPosition(pos) as Metadata
            currentPath = Path(file.pathDisplay)
            refreshAll()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.folder_chooser, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_folder_chooser_bank -> back()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        dropBoxHelper.onResume()
    }

    private fun back(): Boolean {
        currentPath = currentPath.back()
        refreshAll()
        return true
    }

    private fun onTokenReady() {
        refreshAll()
    }

    private fun refreshAll() {
        refresh()
        refreshTitle()
    }

    private fun refreshTitle() {
        actionBar.title = currentPath.currentNode
    }

    private fun refresh() {
        Log.d(TAG, "Current path: $currentPath")
        showProgress("Progress...")
        dropBoxHelper.listFiles(currentPath.path)
                .map { it.entries.filter { it is FolderMetadata || it.name.endsWith(".choices") } }
                .map { it.sortedWith(FolderChooserComparator()) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe(object : Subscriber<List<Metadata>>() {
                    override fun onNext(t: List<Metadata>) {
                        listAdapter = FolderAdapter(t)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "Failed load files", e)
                        showToast(e.message ?: "Something bad happened")
                    }
                })
    }

    inner class FolderAdapter(
            list: List<Metadata>
    ) : ArrayAdapter<Metadata>(this, android.R.layout.simple_list_item_1, list) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val item = getItem(position)
            return super.getView(position, convertView, parent).let { it as TextView }.apply {
                text = item.name
                if (item is FolderMetadata) {
                    setTextColor(Color.BLACK)
                    setTypeface(null, Typeface.NORMAL)
                } else {
                    setTextColor(Color.GRAY)
                    setTypeface(null, Typeface.ITALIC)
                }
            }
        }
    }

    companion object {
        private const val TAG = "FolderChooserActivity"
    }
}
