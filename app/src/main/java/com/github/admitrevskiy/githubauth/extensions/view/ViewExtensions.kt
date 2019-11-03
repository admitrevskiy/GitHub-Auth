package com.github.admitrevskiy.githubauth.extensions.view

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import android.app.Activity
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.inputmethod.InputMethodManager


fun View.swapVisibility() {
    if (this.visibility == View.GONE) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun <T> TextView.setTextOrHide(@StringRes id: Int, value: T) {
    value?.let {
        this.text = context.getString(id, it.toString())
        return
    }

    this.visibility = View.GONE
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}