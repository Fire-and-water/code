package com.example.fireandwater

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.provider.CalendarContract

class GameButton(
    private var posX: Float,
    private var posY: Float,
    private val width: Float,
    private val height: Float
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
        if (x > posX && x < posX + width && y > posY && y < posY + height) {
            isPressed = true
            paint.color = Color.GREEN
        } else {
            unpressButton()
        }
    }

    fun unpressButton() {
        if (isPressed) {
            isPressed = false
            paint.color = Color.YELLOW
        }
    }

    fun unpressButton(x : Float, y : Float) {
        if (isPressed && x > posX && x < posX + width && y > posY && y < posY + height) {
            isPressed = false
            paint.color = Color.YELLOW
        }
    }

}
