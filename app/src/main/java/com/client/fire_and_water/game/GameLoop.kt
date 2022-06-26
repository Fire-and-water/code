package com.client.fire_and_water.game

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameLoop(var game: Game, private var surfaceHolder: SurfaceHolder) : Thread() {
    private var isRunning: Boolean = false
    private var averageUPS: Double = 0.0
    private var averageFPS: Double = 0.0
    private val MAX_UPS = 60F
    private val UPS_PERIOD = 1e3/MAX_UPS

    fun getAverageUPS(): Double {
        return averageUPS
    }

    fun getAverageFPS(): Double {
        return averageFPS
    }

    fun startLoop() {
        isRunning = true
        start()
    }

    override fun run() {
        super.run()

        var updateCount = 0
        var frameCount = 0
        var startTime : Long
        var elapsedTime : Long
        var sleepTime : Long

        var canvas : Canvas? = null
        startTime = System.currentTimeMillis()

        while (isRunning) {
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    game.update()
                    updateCount++
                    game.draw(canvas)
                }
            } catch (e : IllegalArgumentException) {
                e.printStackTrace()
            }finally {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                    frameCount++
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }

            elapsedTime = System.currentTimeMillis() - startTime
            sleepTime = (updateCount * UPS_PERIOD - elapsedTime).toLong()
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime)
                } catch (e : InterruptedException) {
                    e.printStackTrace()
                }
            }

            while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                game.update()
                updateCount++
                elapsedTime = System.currentTimeMillis() - startTime
                sleepTime = (updateCount * UPS_PERIOD - elapsedTime).toLong()
            }

            elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime >= 1000) {
                averageUPS = updateCount / (1E-3 * elapsedTime)
                averageFPS = frameCount / (1E-3 *  elapsedTime)
                updateCount = 0
                frameCount = 0
                startTime = System.currentTimeMillis()
            }
        }
    }
}
