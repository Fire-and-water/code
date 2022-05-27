package com.client.fire_and_water

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class LevelObject(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val isPenetrable : Boolean = false
) {
    private val paint : Paint = Paint()
    init {
        paint.color = Color.BLUE
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(x, y, x + width, y + height, paint)
    }

}