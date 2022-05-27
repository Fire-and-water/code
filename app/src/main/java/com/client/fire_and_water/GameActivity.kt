package com.client.fire_and_water

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics

class GameActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        setContentView(Game(this, height, width))
    }
}
