package com.client.fire_and_water

import java.util.*

class User {
    var uuid: Int = 0
    lateinit var username : String

    var experience : Int = 0
    var passedLevels : HashMap<Int, Int> = HashMap() //player`s passed levels and points player got for them

    enum class Role {
        FIRE, WATER
    }

    enum class UserStep {
        W, A, S, D
    }
}