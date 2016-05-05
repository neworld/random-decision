package lt.neworld.randomdecision

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dropbox.core.android.Auth
import lt.neworld.randomdecision.comparators.FolderChooserComparator
import lt.neworld.randomdecision.dropbox.DropBoxHelper
import lt.neworld.randomdecision.extensions.hideProgress
import lt.neworld.randomdecision.extensions.showProgress
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import lt.neworld.randomdecision.choices.Builder
import lt.neworld.randomdecision.choices.Choice
import lt.neworld.randomdecision.extensions.showToast
import rx.Observable
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : Activity() {

    val dropBoxHelper by lazy {
        DropBoxHelper(this, { onTokenReady() })
    }

    val adapter by lazy {
        ChoiceAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.adapter = adapter
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
        showProgress("Progress...")
        dropBoxHelper.listFiles(dropBoxHelper.path)
                .map { it.entries.filter { it.name.endsWith(".choices") } }
                .map { it.sortedWith(FolderChooserComparator()) }
                .flatMapIterable { it }
                .flatMap {
                    dropBoxHelper
                            .openFile(it.pathLower)
                            .zipWith(Observable.just(it.pathDisplay.split("/").last()), {
                                t1: InputStream, t2: String -> t2 to t1
                            })
                }
                .map { Builder(it.first, InputStreamReader(it.second)).build() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    adapter.clear()
                    adapter.setNotifyOnChange(false)
                }
                .doOnTerminate { hideProgress() }
                .subscribe(object : Subscriber<Choice>() {
                    override fun onCompleted() {
                        adapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        showToast(e.message ?: "Something bad happened")
                    }

                    override fun onNext(t: Choice) {
                        adapter.add(t)
                    }

                })
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

    inner class ChoiceAdapter() : ArrayAdapter<Choice>(this, android.R.layout.simple_list_item_1) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val item = getItem(position)
            return super.getView(position, convertView, parent).let { it as TextView }.apply {
                text = item.title
            }
        }
    }

}
