package com.example.antidiabet1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class CringeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(packageName, MODE_PRIVATE)

        var appOpenedCount = prefs.getInt("app_opened_count", 1)
        appOpenedCount += 1
        prefs.edit().putInt("app_opened_count", appOpenedCount).commit()
        Log.d("--> openCount", appOpenedCount.toString())

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("startPosition", "fromCringeActivity")
        startActivity(intent)
    }
}