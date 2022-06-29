package com.client.fire_and_water.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Victory(private val widthScaleCoefficient: Float, private val heightScaleCoefficient: Float) {

    fun draw(canvas: Canvas?) {
        val text = "YOU WIN!!!"
        val x = 460f * widthScaleCoefficient
        val y = 650f * heightScaleCoefficient
        val paint = Paint()
        paint.color = Color.MAGENTA
        paint.textSize = 200f
        canvas?.drawText(text, x, y, paint)
    }

}
