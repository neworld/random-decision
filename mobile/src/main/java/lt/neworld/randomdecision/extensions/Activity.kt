package lt.neworld.randomdecision.extensions

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.widget.Toast

private var lastDialog: Dialog? = null

/**
 * @author Andrius Semionovas
 * @since 2016-04-17
 */

fun Activity.showProgress(message: CharSequence) {
    hideProgress()
    lastDialog = ProgressDialog(this).apply {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setCancelable(false)
        setMessage(message)
        show()
    }
}

fun Activity.hideProgress() {
    lastDialog?.run { dismiss() }
    lastDialog = null
}

fun Activity.showToast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
