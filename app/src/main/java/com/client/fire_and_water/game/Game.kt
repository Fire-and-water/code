package com.client.fire_and_water.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.client.fire_and_water.Network
import com.client.fire_and_water.R

enum class PlayerType {
    FIRE,
    WATER
}

@SuppressLint("ViewConstructor")
class Game(context: Context, height: Int, width: Int, val network: Network, val type: PlayerType) :
    SurfaceView(context),
    SurfaceHolder.Callback {
    private var isOver: Boolean = false
    private var gameLoop: GameLoop = GameLoop(this, holder)
    val widthScaleCoefficient: Float = (width.toFloat() / 1920f)
    val heightScaleCoefficient: Float = (height.toFloat() / 1080f)
    private val victory: Victory = Victory(widthScaleCoefficient, heightScaleCoefficient)
    private var leftButton: GameButton = GameButton(
        100f * widthScaleCoefficient,
        900f * heightScaleCoefficient,
        150f * widthScaleCoefficient,
        150f * heightScaleCoefficient,
        50f * widthScaleCoefficient,
        850f * heightScaleCoefficient,
        250f * widthScaleCoefficient,
        230f * heightScaleCoefficient

    )
    private var rightButton: GameButton = GameButton(
        350f * widthScaleCoefficient,
        900f * heightScaleCoefficient,
        150f * widthScaleCoefficient,
        150f * heightScaleCoefficient,
        300f * widthScaleCoefficient,
        850f * heightScaleCoefficient,
        250f * widthScaleCoefficient,
        230f * heightScaleCoefficient
    )
    private var jumpButton: GameButton = GameButton(
        1600f * widthScaleCoefficient,
        900f * heightScaleCoefficient,
        150f * widthScaleCoefficient,
        150f * heightScaleCoefficient,
        1550f * widthScaleCoefficient,
        850f * heightScaleCoefficient,
        250f * widthScaleCoefficient,
        230f * heightScaleCoefficient
    )
    var level: GameLevel? = null
    private var fire: Player? = null
    private var water: Player? = null
    private val finish: Finish = Finish(
        100f * widthScaleCoefficient,
        440 * heightScaleCoefficient,
        150 * widthScaleCoefficient,
        200 * heightScaleCoefficient
    )


    init {
        holder.addCallback(this)

        setGameLevel()
        fire = Player(
            500f * widthScaleCoefficient,
            300f * heightScaleCoefficient,
            100f,
            100f,
            this,
            Color.rgb(255, 153, 0)
        )
        water = Player(
            1000f * widthScaleCoefficient,
            300f * heightScaleCoefficient,
            100f,
            100f,
            this,
            Color.rgb(65, 136, 210)
        )
        network.startListen(this, type)
    }

    private fun setGameLevel() {
        val listOfLevelObjects = mutableListOf<LevelObject>()
        with(listOfLevelObjects) {
            add(
                LevelObject(
                    0f * widthScaleCoefficient,
                    0f * heightScaleCoefficient,
                    1920f * widthScaleCoefficient,
                    100f * heightScaleCoefficient,
                )
            )
            add(
                LevelObject(
                    0f * widthScaleCoefficient,
                    0f * heightScaleCoefficient,
                    100f * widthScaleCoefficient,
                    1080f * heightScaleCoefficient
                )
            )
            add(
                LevelObject(
                    1820f * widthScaleCoefficient,
                    0f * heightScaleCoefficient,
                    100f * widthScaleCoefficient,
                    1080f * heightScaleCoefficient
                )
            )
            add(
                LevelObject(
                    0f * widthScaleCoefficient,
                    880f * heightScaleCoefficient,
                    1920f * widthScaleCoefficient,
                    200f * heightScaleCoefficient
                )
            )
            add(
                LevelObject(
                    1120f * widthScaleCoefficient,
                    480f * heightScaleCoefficient,
                    800f * widthScaleCoefficient,
                    50f * heightScaleCoefficient
                )
            )
            add(
                LevelObject(
                    0f * widthScaleCoefficient,
                    280f * heightScaleCoefficient,
                    800f * widthScaleCoefficient,
                    50f * heightScaleCoefficient
                )
            )
            add(
                LevelObject(
                    0f * widthScaleCoefficient,
                    640f * heightScaleCoefficient,
                    800f * widthScaleCoefficient,
                    50f * heightScaleCoefficient
                )
            )
        }
        level = GameLevel(listOfLevelObjects)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        gameLoop.startLoop()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {

    }

    //TouchEvents
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointerCount = event?.pointerCount
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                for (i in 0..10) {
                    if (i < pointerCount!!) {
                        checkButtonPressed(event.getX(i), event.getY(i))
                    }
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                unpressButtons()
                for (i in 0..10) {
                    if (i < pointerCount!!) {
                        checkButtonPressed(event.getX(i), event.getY(i))
                    }
                }
                return true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                unpressButtons(event.getX(pointerCount!! - 1), event.getY(pointerCount - 1))
                return true
            }
            MotionEvent.ACTION_UP -> {
                unpressButtons()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun unpressButtons(x: Float, y: Float) {
        leftButton.unpressButton(x, y)
        rightButton.unpressButton(x, y)
        jumpButton.unpressButton(x, y)
    }

    private fun unpressButtons() {
        leftButton.unpressButton()
        rightButton.unpressButton()
        jumpButton.unpressButton()
    }

    private fun checkButtonPressed(x: Float, y: Float) {
        leftButton.checkIsPressed(x, y)
        rightButton.checkIsPressed(x, y)
        jumpButton.checkIsPressed(x, y)
    }

    //method to show everything
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.drawRGB(249, 250, 179)
        level?.draw(canvas)
        finish.draw(canvas)
        fire?.draw(canvas)
        water?.draw(canvas)
        leftButton.draw(canvas)
        rightButton.draw(canvas)
        jumpButton.draw(canvas)
        drawUPS(canvas)
        drawFPS(canvas)
        if (isOver) {
            victory.draw(canvas)
        }
    }

    //this two show ups and fps
    private fun drawUPS(canvas: Canvas?) {
        val averageUPS: String = gameLoop.getAverageUPS().toString()
        val paint = Paint()
        val color = ContextCompat.getColor(context, R.color.white)
        paint.color = color
        paint.textSize = 50F
        canvas?.drawText("UPS: $averageUPS", 100F, 100F, paint)
    }

    private fun drawFPS(canvas: Canvas?) {
        val averageFPS: String = gameLoop.getAverageFPS().toString()
        val paint = Paint()
        val color = ContextCompat.getColor(context, R.color.white)
        paint.color = color
        paint.textSize = 50F
        canvas?.drawText("FPS: $averageFPS", 100F, 200F, paint)
    }

    private fun buttonsUpdate() {
        leftButton.update()
        rightButton.update()
        jumpButton.update()
        level?.update()
    }

    fun update() {
        buttonsUpdate()
        if (type == PlayerType.FIRE) {
            fire?.movingLeft = leftButton.isPressed
//            if (leftButton.isPressed) water?.updateFromServer(network.sendMove("left", type))
            fire?.movingRight = rightButton.isPressed
//            if (rightButton.isPressed) water?.updateFromServer(network.sendMove("right", type))
            if (fire?.isOnGround!!) {
                fire?.isJumping = jumpButton.isPressed
//                if (jumpButton.isPressed) water?.updateFromServer(network.sendMove("jump", type))
            }
            fire?.update()
        } else if (type == PlayerType.WATER) {
            water?.movingLeft = leftButton.isPressed
//            if (leftButton.isPressed) fire?.updateFromServer(network.sendMove("left", type))
            water?.movingRight = rightButton.isPressed
//            if (rightButton.isPressed) fire?.updateFromServer(network.sendMove("right", type))
            if (water?.isOnGround!!) {
                water?.isJumping = jumpButton.isPressed
//                if (jumpButton.isPressed) fire?.updateFromServer(network.sendMove("jump", type))
            }
            water?.update()
        }
        areYouWinningSon()
    }

    private fun areYouWinningSon() {
        if (finish.containsBoth(fire, water)) {
            finish.changeColor(Color.GREEN)
            gameLoop.stopLoop()
            isOver = true
            network.sendWinningMessage()
        }
    }

    fun updateOtherPlayer(coords: Network.PlayerCoordinates?) {
        if (type == PlayerType.FIRE) water?.updatePos(coords)
        else fire?.updatePos(coords)
    }
}
