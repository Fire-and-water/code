package com.example.fireandwater

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat

class Game(context: Context, height: Int, width: Int) : SurfaceView(context),
    SurfaceHolder.Callback {
    private var gameLoop: GameLoop? = null
    private var player: Player? = null
    private var leftButton: GameButton? = null
    private var rightButton: GameButton? = null
    private var jumpButton: GameButton? = null
    private var level: GameLevel? = null
    private val widthScaleCoefficient: Float = (width / 1920).toFloat()
    private val heightScaleCoefficient: Float = (height / 1080).toFloat()


    init {
        holder.addCallback(this)
        setGameLevel()
        gameLoop = GameLoop(this, holder)
        player = Player(
            context,
            500f * widthScaleCoefficient,
            300f * heightScaleCoefficient,
            100f,
            100f,
            level!!
        )
        leftButton =
            GameButton(100f * widthScaleCoefficient, 900f * heightScaleCoefficient, 150f, 150f)
        rightButton =
            GameButton(350f * widthScaleCoefficient, 900f * heightScaleCoefficient, 150f, 150f)
        jumpButton =
            GameButton(1600f * widthScaleCoefficient, 900f * heightScaleCoefficient, 150f, 150f)
        focusable = FOCUSABLE
    }

    private fun setGameLevel() {
        val listOfLevelObjects = mutableListOf<LevelObject>()
        listOfLevelObjects.add(
            LevelObject(
                0f * widthScaleCoefficient,
                0f * heightScaleCoefficient,
                1920f,
                100f
            )
        )
        listOfLevelObjects.add(
            LevelObject(
                0f * widthScaleCoefficient,
                0f * heightScaleCoefficient,
                100f,
                1080f
            )
        )
        listOfLevelObjects.add(
            LevelObject(
                1820f * widthScaleCoefficient,
                0f * heightScaleCoefficient,
                100f,
                1080f
            )
        )
        listOfLevelObjects.add(
            LevelObject(
                0f * widthScaleCoefficient,
                880f * heightScaleCoefficient,
                1920f,
                200f
            )
        )
        listOfLevelObjects.add(
            LevelObject(
                1120f * widthScaleCoefficient,
                480f * heightScaleCoefficient,
                800f,
                50f
            )
        )
        listOfLevelObjects.add(
            LevelObject(
                0f * widthScaleCoefficient,
                280f * heightScaleCoefficient,
                800f,
                50f
            )
        )
        listOfLevelObjects.add(
            LevelObject(
                0f * widthScaleCoefficient,
                640f * heightScaleCoefficient,
                800f,
                50f
            )
        )
        level = GameLevel(listOfLevelObjects)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        gameLoop?.startLoop()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                checkButtonPressed(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                checkButtonPressed(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                unpressButtons()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun unpressButtons(x: Float, y: Float) {
        leftButton?.unpressButton(x, y)
        rightButton?.unpressButton(x, y)
        jumpButton?.unpressButton(x, y)
    }

    private fun unpressButtons() {
        leftButton?.unpressButton()
        rightButton?.unpressButton()
        jumpButton?.unpressButton()
    }

    private fun checkButtonPressed(x: Float, y: Float) {
        leftButton?.checkIsPressed(x, y)
        rightButton?.checkIsPressed(x, y)
        jumpButton?.checkIsPressed(x, y)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        level?.draw(canvas)
        player?.draw(canvas)
        leftButton?.draw(canvas)
        rightButton?.draw(canvas)
        jumpButton?.draw(canvas)
        drawUPS(canvas)
        drawFPS(canvas)
    }

    private fun drawUPS(canvas: Canvas?) {
        val averageUPS: String = gameLoop?.getAverageUPS().toString()
        val paint = Paint()
        val color = ContextCompat.getColor(context, R.color.white)
        paint.color = color
        paint.textSize = 50F
        canvas?.drawText("UPS: $averageUPS", 100F, 100F, paint)
    }

    private fun drawFPS(canvas: Canvas?) {
        val averageFPS: String = gameLoop?.getAverageFPS().toString()
        val paint = Paint()
        val color = ContextCompat.getColor(context, R.color.white)
        paint.color = color
        paint.textSize = 50F
        canvas?.drawText("FPS: $averageFPS", 100F, 200F, paint)
    }

    private fun buttonsUpdate() {
        leftButton?.update()
        rightButton?.update()
        jumpButton?.update()
        level?.update()
    }

    fun update() {
        buttonsUpdate()

        player?.movingLeft = leftButton?.isPressed!!
        player?.movingRight = rightButton?.isPressed!!
        if (player?.isOnGround!!) {
            player?.isJumping = jumpButton?.isPressed!!
        }
        player?.update()
    }
}
