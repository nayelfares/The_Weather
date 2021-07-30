package com.example.theweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun replaceFragmentFromMain(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                add(R.id.content, fragment)
                addToBackStack(fragment::class.java.simpleName)
            }.
            commitAllowingStateLoss()
    }
}