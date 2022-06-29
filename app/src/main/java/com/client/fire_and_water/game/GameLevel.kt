package com.client.fire_and_water.game

import android.graphics.Canvas

class GameLevel(val listOfLevelObjects: MutableList<LevelObject>) {

    fun draw(canvas: Canvas?) {
        for (levelObject in listOfLevelObjects) {
            levelObject.draw(canvas)
        }
    }

    fun update() {
        //TODO
    }
}