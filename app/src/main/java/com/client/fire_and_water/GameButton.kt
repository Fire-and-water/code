package com.client.fire_and_water

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class GameButton(
    private var posX: Float,
    private var posY: Float,
    private val width: Float,
    private val height: Float,
    private val realPosX: Float,
    private val realPosY: Float,
    private val realWidth: Float,
    private val realHeight: Float,
) {
    var isPressed: Boolean = false
    private var paint : Paint = Paint()
    init {
        paint.color = Color.YELLOW
    }
    fun draw(canvas: Canvas?) {
        canvas?.drawRect(posX, posY, posX + width, posY + height, paint)
    }

    fun update() {

    }

    fun checkIsPressed(x: Float, y: Float) {
        if (x > realPosX && x < realPosX + realWidth && y > realPosY && y < realPosY + realHeight) {
            isPressed = true
            paint.color = Color.GREEN
        }
    }

    fun unpressButton() {
        if (isPressed) {
            isPressed = false
            paint.color = Color.YELLOW
        }
    }

    fun unpressButton(x : Float, y : Float) {
        if (isPressed && x > realPosX && x < realPosX + realWidth && y > realPosY && y < realPosY + realHeight) {
            isPressed = false
            paint.color = Color.YELLOW
        }
    }

}
