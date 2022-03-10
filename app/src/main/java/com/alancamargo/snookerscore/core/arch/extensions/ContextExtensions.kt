package com.alancamargo.snookerscore.core.arch.extensions

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.appcompat.app.AppCompatActivity

private const val VIBRATION_DURATION = 50L

inline fun <reified T : AppCompatActivity> Context.createIntent(): Intent {
    return Intent(this, T::class.java)
}

fun Context.vibrate() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        val vibrationEffect = VibrationEffect.createOneShot(
            VIBRATION_DURATION,
            VibrationEffect.DEFAULT_AMPLITUDE
        )

        vibrator.vibrate(vibrationEffect)
    } else {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VIBRATION_DURATION)
    }
}