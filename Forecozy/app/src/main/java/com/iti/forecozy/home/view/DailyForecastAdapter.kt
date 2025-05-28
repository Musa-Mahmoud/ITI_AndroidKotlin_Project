package com.iti.forecozy.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.forecozy.databinding.ItemForecastBinding
import com.iti.forecozy.model.dto.settings.TemperatureUnit
import com.iti.forecozy.model.dto.weather.ForecastItem
import com.iti.forecozy.utils.unitconverter.UnitConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyForecastAdapter(
    private val unitConverter: UnitConverter,
    private val tempUnit: TemperatureUnit
) : RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {

    private var forecastItems = listOf<ForecastItem>()

    fun submitList(items: List<ForecastItem>) {
        forecastItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DailyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(forecastItems[position])
    }

    override fun getItemCount() = forecastItems.size

    inner class DailyForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ForecastItem) {
            binding.apply {
                val date = Date(item.timestamp * 1000L)
                val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

                dayText.text = dayFormat.format(date)
                dateText.text = dateFormat.format(date)

                val maxTemp = unitConverter.convertTemperature(item.main.tempMax, tempUnit)
                val minTemp = unitConverter.convertTemperature(item.main.tempMin, tempUnit)
                temperatureRangeText.text = "$maxTemp / $minTemp"

                Glide.with(itemView)
                    .load("https://openweathermap.org/img/wn/${item.weather[0].icon}@4x.png")
                    .into(weatherIcon)
            }
        }
    }
}