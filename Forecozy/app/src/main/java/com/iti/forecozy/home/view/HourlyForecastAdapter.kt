package com.iti.forecozy.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.forecozy.databinding.ItemHourlyForecastBinding
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.utils.unitconverter.UnitConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HourlyForecastAdapter(
    private val unitConverter: UnitConverter,
    private val tempUnit: TemperatureUnit
) : RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    private var forecastItems = listOf<ForecastItem>()

    fun submitList(items: List<ForecastItem>) {
        forecastItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val binding = ItemHourlyForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HourlyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.bind(forecastItems[position])
    }

    override fun getItemCount() = forecastItems.size

    inner class HourlyForecastViewHolder(private val binding: ItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ForecastItem) {
            binding.apply {
                val date = Date(item.timestamp * 1000L)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                timeText.text = timeFormat.format(date)

                temperatureText.text = unitConverter.convertTemperature(
                    item.main.temp,
                    tempUnit
                )

                Glide.with(itemView)
                    .load("https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png")
                    .into(weatherIconSmall)
            }
        }
    }
}