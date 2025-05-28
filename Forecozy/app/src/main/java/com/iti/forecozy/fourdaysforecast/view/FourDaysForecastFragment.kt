package com.iti.forecozy.fourdaysforecast.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iti.forecozy.fourdaysforecast.viewmodel.FourDaysForecastViewModel
import com.iti.forecozy.R

class FourDaysForecastFragment : Fragment() {

    companion object {
        fun newInstance() = FourDaysForecastFragment()
    }

    private val viewModel: FourDaysForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_four_days_forecast, container, false)
    }
}