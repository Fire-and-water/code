package com.client.fire_and_water.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.client.fire_and_water.MainActivity
import com.client.fire_and_water.Network
import com.client.fire_and_water.R

@SuppressLint("ViewConstructor")
class Game(context: Context, height: Int, width: Int, private val network: Network) : SurfaceView(context),
    SurfaceHolder.Callback {
    private var gameLoop: GameLoop = GameLoop(this, holder)
    private val widthScaleCoefficient: Float = (width.toFloat() / 1920f)
    private val heightScaleCoefficient: Float = (height.toFloat() / 1080f)
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
    private var level: GameLevel? = null
    private var player: Player? = null


    init {
        holder.addCallback(this)
        setGameLevel()
        player = Player(
            500f * widthScaleCoefficient,
            300f * heightScaleCoefficient,
            100f,
            100f,
            level!!
        )
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
        level?.draw(canvas)
        player?.draw(canvas)
        leftButton.draw(canvas)
        rightButton.draw(canvas)
        jumpButton.draw(canvas)
        drawUPS(canvas)
        drawFPS(canvas)

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

        player?.movingLeft = leftButton.isPressed
        if (leftButton.isPressed) network.sendMove("left")
        player?.movingRight = rightButton.isPressed
        if (rightButton.isPressed) network.sendMove("right")
        if (player?.isOnGround!!) {
            player?.isJumping = jumpButton.isPressed
            if(jumpButton.isPressed) network.sendMove("jump")
        }
        player?.update()
    }
}
