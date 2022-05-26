package com.client.fire_and_water

import android.widget.Toast

fun makeToast(str: String, activity: MainActivity) {
    activity.runOnUiThread(Runnable {
        Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
    })
}
