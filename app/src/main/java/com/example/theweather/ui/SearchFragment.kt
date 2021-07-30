package com.example.theweather.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.theweather.R
import com.example.theweather.WeatherViewModel
import com.example.theweather.adapter.CityAdapter
import com.example.theweather.base.BaseFragment
import com.example.theweather.base.DataState
import com.example.theweather.intent.WeatherIntent
import com.example.theweather.model.City
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
class SearchFragment : BaseFragment(R.layout.fragment_search) {

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
    
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
                        results.adapter=CityAdapter(requireContext(),it.data)
                    }
                    is DataState.Error -> {
                        Log.e("Error",it.exception.toString())
                    }
                    else -> {
                    }
                }
            })
    }
}