package com.client.fire_and_water

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import android.view.Menu
import com.client.fire_and_water.databinding.ActivityMainBinding
import com.client.fire_and_water.game.Game


class MainActivity : AppCompatActivity() {
    var network : Network = Network()
    var turnOffBackButton : Boolean = false
    var role : UserClient.Role = UserClient.Role.FIRE

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    var gameId = 0

    fun pressBack() {
        super.onBackPressed()
    }
    override fun onBackPressed() {
        if (!turnOffBackButton)
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}