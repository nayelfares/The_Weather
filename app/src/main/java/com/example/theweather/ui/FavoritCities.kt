package com.example.theweather.ui

import android.app.Dialog
import android.os.*
import android.view.*
import androidx.annotation.Nullable
import androidx.lifecycle.lifecycleScope
import com.example.theweather.R
import com.example.theweather.adapter.FavoriteCityAdapter
import com.example.theweather.room.CityCacheEntity
import com.example.theweather.room.CityDao
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorit_cities.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class FavoritCities : BottomSheetDialogFragment() {

    @Inject
    lateinit var cityDao: CityDao
    lateinit var favoriteCitiesList:ArrayList<CityCacheEntity>
    companion object {
        fun newInstance():FavoritCities{
            val fragment = FavoritCities()
            return fragment
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorit_cities, container, false)
        return view
    }


    lateinit var behavior : BottomSheetBehavior<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            favoriteCitiesList = ArrayList(cityDao.get())
            favoriteCities.adapter = FavoriteCityAdapter(requireActivity(),this@FavoritCities,lifecycleScope,cityDao,favoriteCitiesList)
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null) {
            dialog.window!!.setGravity(Gravity.BOTTOM)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog.setCancelable(false)
        }
        return dialog
    }


}
