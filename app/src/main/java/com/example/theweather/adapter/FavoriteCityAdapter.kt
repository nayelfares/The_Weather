package com.example.theweather.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.MainActivity
import com.example.theweather.R
import com.example.theweather.room.CityCacheEntity
import com.example.theweather.room.CityDao
import com.example.theweather.ui.CurrentWeatherFragment
import com.example.theweather.ui.FavoritCities
import kotlinx.android.synthetic.main.city_item.view.cityName
import kotlinx.android.synthetic.main.favorite_city_item.view.*
import kotlinx.coroutines.*

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FavoriteCityAdapter(
    private val activity: Activity,
    private val favoritCities: FavoritCities,
    private val lifecycleScope: CoroutineScope,
    var cityDao:CityDao,
    val cities: ArrayList<CityCacheEntity>
                  ): RecyclerView.Adapter<FavoriteCityAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: FavoriteCityAdapter.ViewHolder, position: Int) {

        holder.cityName.text = cities[position].name

        holder.itemView.setOnClickListener {
            favoritCities.dismiss()
            (activity as MainActivity).supportFragmentManager.popBackStackImmediate()
            activity.addFragmentFromMain(CurrentWeatherFragment.newInstance(cities[position].url))
        }

        holder.delete.setOnClickListener {
            lifecycleScope.launch {
                cityDao.delete(cities[position].id)
                activity.runOnUiThread {
                    cities.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_city_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return cities.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cityName   = itemView.cityName
        internal var delete     = itemView.delete
    }
}