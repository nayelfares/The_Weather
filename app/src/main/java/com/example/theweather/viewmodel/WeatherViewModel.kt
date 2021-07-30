package com.example.theweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweather.base.DataState
import com.example.theweather.intent.WeatherIntent
import com.example.theweather.model.City
import com.example.theweather.repository.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
@HiltViewModel
class WeatherViewModel
@Inject
constructor(
    private val weatherRepo: WeatherRepo
): ViewModel()  {
    val userIntent = Channel<WeatherIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }
    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when(it){
                    is WeatherIntent.GetSuggestions-> getSuggestionsUpdateStates(it.query)
                }
            }
        }
    }
    private val _suggestionsDataState: MutableLiveData<DataState<ArrayList<City>>> = MutableLiveData()
    val suggestionsDataState: LiveData<DataState<ArrayList<City>>>
        get() = _suggestionsDataState


    fun getSuggestionsUpdateStates(query: String) {
        viewModelScope.launch {
            weatherRepo.getSuggestions(query)
                .onEach { dataState ->
                    _suggestionsDataState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}