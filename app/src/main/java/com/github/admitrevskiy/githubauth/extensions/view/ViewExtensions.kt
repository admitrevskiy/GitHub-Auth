package com.github.admitrevskiy.githubauth.extensions.view

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import android.app.Activity
import android.view.inputmethod.InputMethodManager

/**
 * Inverses visibility of view
 *
 * Sets visibility to GONE if view visibility was VISIBLE
 * Sets visibility to VISIBLE if view visibility was GONE
 */
fun View.inverseVisibility() {
    if (this.visibility == View.GONE) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

/**
 * Sets value text to TextView with string resource with given id or hides TextView if value is null
 */
fun <T> TextView.setTextOrHide(@StringRes id: Int, value: T) {
    value?.let {
        this.text = context.getString(id, it.toString())
        return
    }

    this.visibility = View.GONE
}

/**
 * Hides soft keyboard
 */
fun Activity.hideKeyboard() {
    this.currentFocus?.let { v ->
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}