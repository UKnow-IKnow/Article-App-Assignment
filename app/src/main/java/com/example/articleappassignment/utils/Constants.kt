package com.example.articleappassignment.utils

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import java.util.TimeZone

class Constants {

    companion object {
        const val API_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
        const val API_RESPONSE_ERROR = "error occurred"
        private const val INPUT_DATE_AND_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val OUTPUT_DATE_AND_TIME_FORMAT = "dd-MMM-yyyy\nhh:mm:ss a"
        private const val TIME_NOT_AVAILABLE = "Time Not Available"
        const val SORT_BY = "sort_by"
        const val NEW_TO_OLD = "0"


        fun getFormattedDateAndTime(time : String) : String {
            val inputDateAndTimeFormat = SimpleDateFormat(INPUT_DATE_AND_TIME_FORMAT, Locale.getDefault())
            val outputDateAndTimeFormat = SimpleDateFormat(OUTPUT_DATE_AND_TIME_FORMAT, Locale.getDefault())
            try {
                val date = inputDateAndTimeFormat.parse(time) ?: return TIME_NOT_AVAILABLE
                return outputDateAndTimeFormat.format(date)
            } catch (e : Exception){
                e.printStackTrace()
                return TIME_NOT_AVAILABLE
            }
        }



        fun convertPublishedTimeToMilliseconds(publishedAt: String): Long {
            if (TextUtils.isEmpty(publishedAt)){
                return 0
            }
            val dateFormat = SimpleDateFormat(INPUT_DATE_AND_TIME_FORMAT, Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            try {
                val date = dateFormat.parse(publishedAt)
                return date?.time ?: 0
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return 0
        }
    }
}