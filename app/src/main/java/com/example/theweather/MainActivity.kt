package com.example.theweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.theweather.model.City
import com.example.theweather.ui.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.ArrayList
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var favoriteCities :ArrayList<City> = ArrayList()

    @Inject
    lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (favoriteCities.isEmpty())
            addFragmentFromMain(SearchFragment.newInstance())
        else
            replaceFragmentFromMain(SearchFragment.newInstance())
    }

    fun addFragmentFromMain(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                add(R.id.content, fragment)
            }.
            commitAllowingStateLoss()
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