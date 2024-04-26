package com.example.articleappassignment.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Utility class containing constants and helper methods used throughout the application.
 */
class Constants {

    companion object {
        const val API_URL =
            "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"

        // Error message for API response error
        const val API_RESPONSE_ERROR = "error occurred"

        // Date and time format for input and output
        private const val INPUT_DATE_AND_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val OUTPUT_DATE_AND_TIME_FORMAT = "dd-MMM-yyyy\nhh:mm:ss a"

        // Placeholder text for unavailable time
        private const val TIME_NOT_AVAILABLE = "Time Not Available"
        const val SORT_BY = "sort_by"
        const val NEW_TO_OLD = "0"

        // Constants for notification handling
        const val NOTIFICATION_REQUEST_CODE = 0
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL"
        const val NOTIFICATION_CHANNEL_NAME = "NEWS_NOTIFICATION"


        /**
         * Converts a given time string to a formatted date and time string.
         * @param time The time string to be formatted.
         * @return The formatted time string or "Time Not Available" if parsing fails.
         */
        fun getFormattedDateAndTime(time: String): String {
            val inputDateAndTimeFormat =
                SimpleDateFormat(INPUT_DATE_AND_TIME_FORMAT, Locale.getDefault())
            val outputDateAndTimeFormat =
                SimpleDateFormat(OUTPUT_DATE_AND_TIME_FORMAT, Locale.getDefault())
            try {
                val date = inputDateAndTimeFormat.parse(time) ?: return TIME_NOT_AVAILABLE
                return outputDateAndTimeFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                return TIME_NOT_AVAILABLE
            }
        }


        /**
         * Converts the published time represented as a string into milliseconds since January 1, 1970, 00:00:00 GMT.
         *
         * @param publishedAt The published time to be converted, represented as a string.
         *                    It should follow the format specified by the `INPUT_DATE_AND_TIME_FORMAT`.
         *                    If the input is empty or null, the function returns 0.
         * @return The published time converted to milliseconds. Returns 0 if the input string is empty or null,
         *         or if there's an error during the conversion process.
         */
        fun convertPublishedTimeToMilliseconds(publishedAt: String): Long {
            if (TextUtils.isEmpty(publishedAt)) {
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