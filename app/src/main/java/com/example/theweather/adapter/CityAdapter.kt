package com.example.theweather.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.MainActivity
import com.example.theweather.R
import com.example.theweather.model.City
import com.example.theweather.ui.CurrentWeatherFragment
import kotlinx.android.synthetic.main.city_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CityAdapter( val activity: Activity, val cities: ArrayList<City>): RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {

        holder.cityName.text = cities[position].name

        holder.itemView.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.popBackStackImmediate()
            (activity as MainActivity).addFragmentFromMain(CurrentWeatherFragment.newInstance(cities[position].url))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return cities.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cityName: TextView = itemView.cityName

    }
}