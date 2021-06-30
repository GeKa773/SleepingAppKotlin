package com.gekaradchenko.testforwork.sleepingappkotlin.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gekaradchenko.testforwork.sleepingappkotlin.R
import com.gekaradchenko.testforwork.sleepingappkotlin.Unit
import com.gekaradchenko.testforwork.sleepingappkotlin.database.SleepNight

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?){
    item?.let {
        text = Unit.convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }
}


@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?){
    item?.let {
        text = Unit.convertNumericQualityToString(item.sleepQuality, context.resources)
    }
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?){
    item?.let {
        setImageResource(
            when (item.sleepQuality) {
                0 -> R.drawable.ic_baseline_mood_2
                1 -> R.drawable.ic_baseline_mood_2
                2 -> R.drawable.ic_baseline_mood_2
                3 -> R.drawable.ic_baseline_emoji_1
                4 -> R.drawable.ic_baseline_emoji_1
                5 -> R.drawable.ic_baseline_emoji_1
                else -> R.drawable.ic_baseline_mood_2
            }
        )
    }
}