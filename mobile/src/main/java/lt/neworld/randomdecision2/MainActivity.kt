package lt.neworld.randomdecision2

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
import kotlinx.android.synthetic.main.activity_main.*
import lt.neworld.randomdecision2.choices.Builder
import lt.neworld.randomdecision2.choices.Choice
import lt.neworld.randomdecision2.choices.RandomPicker
import lt.neworld.randomdecision2.dropbox.DropBoxHelper
import lt.neworld.randomdecision2.extensions.hideProgress
import lt.neworld.randomdecision2.extensions.showProgress
import lt.neworld.randomdecision2.extensions.showToast
import rx.Single
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
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
        list.setOnItemClickListener { adapterView, view, index, id ->
            pick(adapter.getItem(index))
        }
    }

    private fun pick(choice: Choice) {
        val path = RandomPicker(choice).pick()
        status.text = path.map { it.title }.joinToString(" -> ")
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
                .toObservable()
                .flatMapIterable { it }
                .flatMap {
                    dropBoxHelper
                            .openFile(it.pathLower)
                            .zipWith(Single.just(it.pathDisplay.split("/").last()), {
                                t1: InputStream, t2: String -> t2 to t1
                            }).toObservable()
                }
                .map {
                    val title = it.first.split(".").dropLast(1).joinToString(".")
                    Builder(title, InputStreamReader(it.second)).build()
                }
                .toSortedList { a, b -> a.title.first() - b.title.first() }
                .flatMapIterable { it }
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
