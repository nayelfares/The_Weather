package com.example.theweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.theweather.ui.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragmentFromMain(SearchFragment.newInstance())
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