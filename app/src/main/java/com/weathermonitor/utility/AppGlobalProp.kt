package com.weathermonitor.utility

interface AppGlobalProp {
    companion object {
        val APP_BASE_URL: String = "http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=24e580b160591da8b523b78ce7b82fad&units=metric&q="

        val WEATHER_CITIES = arrayOf("Chennai", "Mumbai", "Bangalore", "New Delhi")
        val WEEK_DAYS = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
        val DEGREE = "\u00b0" + " C"
        val DYNAMIC_TIME = ":00:00"
    }

}