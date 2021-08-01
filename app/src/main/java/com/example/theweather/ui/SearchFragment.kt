package com.example.theweather.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.theweather.R
import com.example.theweather.WeatherViewModel
import com.example.theweather.adapter.CityAdapter
import com.example.theweather.base.DataState
import com.example.theweather.intent.WeatherIntent
import com.example.theweather.model.City
import com.example.theweather.room.CityDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
    @Inject
    lateinit var cityDao: CityDao

    @Inject
    lateinit var weatherViewModel: WeatherViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        searchView.doOnTextChanged { text, start, before, count ->
            lifecycleScope.launch {
                weatherViewModel.userIntent.send(WeatherIntent.GetSuggestions(searchView.text.toString()))
            }
        }
    }

    private fun subscribeObservers() {
        weatherViewModel.suggestionsDataState.observe(
            viewLifecycleOwner, {
                when (it) {
                    is DataState.Success<ArrayList<City>> -> {
                        results.adapter=CityAdapter(requireActivity(),lifecycleScope,cityDao,it.data)
                    }
                    is DataState.Error -> {
                        Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            })
    }
}