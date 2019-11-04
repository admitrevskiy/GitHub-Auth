package com.github.admitrevskiy.githubauth.view.helpers

import android.view.View

/**
 * Wrapper for 2 connected views that mutually exclusive each other on the screen
 */
class MutuallyExclusiveViews(bearing: View, support: View) : android.util.Pair<View, View>(bearing, support) {

    /** Check this variable to know state of views */
    var initialState: Boolean = true; private set

    /** Initial state: bearing view is visible; support view is gone */
    init {
        first.visibility = View.VISIBLE
        second.visibility = View.GONE
    }

    /**
     * Excludes views visibility
     *
     * @param bearingViewVisible {@code true} if first (bearing) view should be visible
     */
    fun excludeVisibility(bearingViewVisible: Boolean) {
        first.visibility = if (bearingViewVisible) View.VISIBLE else View.GONE
        second.visibility = if (bearingViewVisible) View.GONE else View.VISIBLE
        initialState = bearingViewVisible
    }
}