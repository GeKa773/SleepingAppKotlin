package com.gekaradchenko.testforwork.sleepingappkotlin

import android.annotation.SuppressLint
import android.content.res.Resources
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.gekaradchenko.testforwork.sleepingappkotlin.database.SleepNight
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

  object Unit {

   fun formatNights(nights: List<SleepNight>, resources: Resources): Spanned {
        val sb = StringBuilder()
        sb.apply {
            append(resources.getString(R.string.title))
            nights.forEach {
                append("<br>")
                append(resources.getString(R.string.start_time))
                append("\t${convertLongToTime(it.startTimeMilli)}<br>")
                if (it.endTimeMilli != it.startTimeMilli) {
                    append(resources.getString(R.string.end_time))
                    append("\t${convertLongToTime(it.endTimeMilli)}<br>")
                    append(resources.getString(R.string.quality))
                    append("\t ${convertNumericQualityToString(it.sleepQuality, resources)}<br>")
                    append(resources.getString(R.string.hours_slept))
                    //House
                    append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60 / 60}:")
                    //Minute
                    append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60}:")
                    //Secong
                    append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000}<br><br>")

                }

            }
        }
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

    }

    @SuppressLint("SimpleDataFormat")
    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
//        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        val format = SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        return format.format(date)
//        return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm").format(systemTime).toString()
    }

    fun convertNumericQualityToString(quality: Int, resources: Resources): String {
        var qualityString = "OK"
        when (quality) {
            -1 -> qualityString = "__"
            0 -> qualityString = "Very bad"
            1 -> qualityString = "Poor"
            2 -> qualityString = "So-so"
            4 -> qualityString = "Pretty good"
            5 -> qualityString = "Excellent!"
        }
        return qualityString
    }
}