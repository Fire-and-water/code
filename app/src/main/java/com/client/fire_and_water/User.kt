package com.client.fire_and_water

class User {
    public lateinit var username : String;
    private var id : Int? = null;
    public var experience : Int = 0;
    public var passedLevels : HashMap<Int, Int> = HashMap();

    public  fun User(username: String) {
        this.username = username;
    }

    
}