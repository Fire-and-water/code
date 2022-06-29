package com.client.fire_and_water

import android.graphics.Color
import android.graphics.Paint

class Diamond(private val posX: Float, private val posY: Float) {
    private val radius = 30f
    private val paint = Paint()
    init {
        paint.color = Color.CYAN;
    }
}