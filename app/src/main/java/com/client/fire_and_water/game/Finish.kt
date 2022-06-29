package com.client.fire_and_water.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Finish(
    private val posX: Float,
    private val posY: Float,
    private val width: Float,
    private val height: Float
) {
    private val paint: Paint = Paint()

    init {
        paint.color = Color.WHITE
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(posX, posY, posX + width, posY + height, paint)
    }

    fun containsBoth(fire: Player?, water: Player?): Boolean {
        return fire!!.posX >= posX && fire.posX + fire.width <= posX + width && fire.posY >= posY && fire.posY + fire.height <= posY + height &&
                water!!.posX >= posX && water.posX + water.width <= posX + width && water.posY >= posY && water.posY + water.height <= posY + height
    }

    fun changeColor(green: Int) {
        paint.color = green
    }
}
