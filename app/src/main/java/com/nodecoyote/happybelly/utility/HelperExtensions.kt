package com.nodecoyote.happybelly.utility

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun Context.buttonFeedback() {
    if (Build.VERSION.SDK_INT >= 26) {
        val buttonFeedback = this@buttonFeedback.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        buttonFeedback.vibrate(VibrationEffect.createOneShot(50, 3))
    }
}