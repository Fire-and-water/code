package com.client.fire_and_water.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.client.fire_and_water.Network

class Player(
    var posX: Float,
    var posY: Float,
    val width: Float,
    val height: Float,
    private val game: Game,
    color: Int
) {
    var movingRight: Boolean = false
    var movingLeft: Boolean = false
    private var paint = Paint()
    var isJumping: Boolean = false
    var isOnGround: Boolean = true
    private val gravitationConstant = 2 * 9.8f / 60
    var yDiff = 0f
    private val xDiff = 7.5f

    init {
        paint.color = color
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(posX, posY, posX + width, posY + height, paint)
    }

    fun update() {
        if (isJumping && isOnGround) {
            yDiff = -15F
            isOnGround = false
            isJumping = false
        }
        if (isOnGround) yDiff = 0f
        yDiff += gravitationConstant
        updatePosY(yDiff)
        if (movingLeft && !movingRight) {
            moveLeft()
        }
        if (movingRight && !movingLeft) {
            moveRight()
        }
        game.network.sendMove(posX / game.widthScaleCoefficient, posY / game.heightScaleCoefficient)
    }

    fun updateOther() {

    }

    fun moveLeft() {
        updatePosX(-xDiff)
    }

    fun moveRight() {
        updatePosX(xDiff)
    }

    private fun collidesWithObject(levelObject: LevelObject): Boolean {
        return !((posX + width < levelObject.x || posX > levelObject.x + levelObject.width) ||
                (posY + height < levelObject.y || posY > levelObject.y + levelObject.height))

    }

    private fun updatePosX(x: Float) {
        posX += x
        for (levelObject in game.level!!.listOfLevelObjects) {
            if (collidesWithObject(levelObject)) {
                posX = if (x < 0) {
                    levelObject.x + levelObject.width + 1f
                } else {
                    levelObject.x - width - 1f
                }
            }
        }
    }

    private fun updatePosY(y: Float) {
        posY += y
        var isOnSmth = false
        for (levelObject in game.level!!.listOfLevelObjects) {
            if (collidesWithObject(levelObject)) {
                if (y < 0) {
                    posY = levelObject.y + levelObject.height + 1f
                    yDiff = 0f
                } else {
                    posY = levelObject.y - height - 1f
                    isOnGround = true
                    isOnSmth = true
                }
            }
        }
        if (!isOnSmth) isOnGround = false
    }

    fun updatePos(coords: Network.PlayerCoordinates?) {
        if (coords == null) update()
        else {
            posX = coords.posX.toFloat() * game.widthScaleCoefficient
            posY = coords.posY.toFloat() * game.heightScaleCoefficient
        }
    }
}
