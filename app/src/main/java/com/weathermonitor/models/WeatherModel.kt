package com.weathermonitor.models

class WeatherModel {

    private var city: String? = null
    private var date: String? = null
    private var temprature: String? = null
    private var minTemp: String? = null
    private var maxTemp: String? = null
    private var pressure: String? = null
    private var humidity: String? = null
    private var windSpeed: String? = null
    private var windDeg: String? = null

    constructor(city: String?, date: String?, temprature: String?, minTemp: String?, maxTemp: String?, pressure: String?, humidity: String?, windSpeed: String?, windDeg: String?) {
        this.city = city
        this.date = date
        this.temprature = temprature
        this.minTemp = minTemp
        this.maxTemp = maxTemp
        this.pressure = pressure
        this.humidity = humidity
        this.windSpeed = windSpeed
        this.windDeg = windDeg
    }

    fun getWindDeg(): String? {
        return windDeg
    }

    fun getWindSpeed(): String? {
        return windSpeed
    }

    fun getHumidity(): String? {
        return humidity
    }

    fun getPressure(): String? {
        return pressure
    }

    fun getMaxTemp(): String? {
        return maxTemp
    }

    fun getMinTemp(): String? {
        return minTemp
    }

    fun getTemprature(): String? {
        return temprature
    }

    fun getDate(): String? {
        return date
    }

    fun getCity(): String? {
        return city
    }


}