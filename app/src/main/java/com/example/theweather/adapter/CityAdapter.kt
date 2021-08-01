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
import com.example.theweather.room.CityDao
import com.example.theweather.room.CurrentWeatherDao
import com.example.theweather.room.toCityCacheEntity
import com.example.theweather.ui.CurrentWeatherFragment
import kotlinx.android.synthetic.main.city_item.view.*
import kotlinx.coroutines.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CityAdapter(val activity: Activity,
                  val lifecycleScope: CoroutineScope,
                  var cityDao:CityDao,
                  val cities: ArrayList<City>
                  ): RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {

        holder.cityName.text = cities[position].name

        holder.itemView.setOnClickListener {
            lifecycleScope.launch {
                cityDao.insert(cities[position].toCityCacheEntity())
            }
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