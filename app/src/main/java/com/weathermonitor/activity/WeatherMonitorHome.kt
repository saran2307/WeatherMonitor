package com.weathermonitor.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.weathermonitor.R
import com.weathermonitor.models.WeatherModel
import com.weathermonitor.utility.AppGlobalProp
import com.weathermonitor.utility.CustomApplication
import kotlinx.android.synthetic.main.activity_weather_monitor_home.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WeatherMonitorHome : AppCompatActivity() {


    var navigationCounter = 0
    var progressDialog: ProgressDialog? = null
    var weatherDetails: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_monitor_home)

        try {
            progressDialog = ProgressDialog(this)
            progressDialog?.setCancelable(false)
            showProgress()
            if (isNetworkConnected()) {
                imgLeftNavigation.setOnClickListener(View.OnClickListener {
                    showProgress()
                    navigationCounter--
                    updateCities()
                    getWeatherReport()

                })
                imgRightNavigation.setOnClickListener(View.OnClickListener {
                    showProgress()
                    navigationCounter++
                    updateCities()
                    getWeatherReport()
                    updateDays()
                })

                llTodayWeather.setOnClickListener(View.OnClickListener {

                    llAdditionDetails.visibility = View.VISIBLE
                })

                updateCities()
                getWeatherReport()
                //updateDays()
            } else {
                println("://no network connection")
                Toast.makeText(this, "Check your network connection.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

    }

    fun updateCities() {
        try {
            txtCity.setText(AppGlobalProp.WEATHER_CITIES.get(navigationCounter))
            if (navigationCounter == 0) {
                imgLeftNavigation.visibility = View.INVISIBLE

            } else if (navigationCounter >= 3) {
                imgRightNavigation.visibility = View.INVISIBLE
            } else {
                imgLeftNavigation.visibility = View.VISIBLE
                imgRightNavigation.visibility = View.VISIBLE

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgress() {
        try {
            progressDialog?.setTitle("Loading..!")
            progressDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateDays() {

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val timeFormat = SimpleDateFormat("HH")
            val calendar = Calendar.getInstance()
            dateFormat.calendar = calendar


            for (i in 1..3) {
                calendar.add(Calendar.DATE, 1)

                dateFormat.format(calendar.timeInMillis)
                val today = calendar.time
                val reportDate = dateFormat.format(today)
                var reportTime = timeFormat.format(today)
                val day = calendar.get(Calendar.DAY_OF_WEEK)
                val remainder = reportTime.toInt() % 3
                println("://after calculating reportTime " + reportTime)
                if (remainder != 0) {
                    println(":// check remainder " + remainder)
                    reportTime = (reportTime.toInt() - remainder).toString()
                    println("://after calculating report time " + reportTime)

                    if (reportTime.toInt() <= 9) {
                        reportTime = "0" + reportTime
                    }

                }


                println(":// current day " + AppGlobalProp.WEEK_DAYS.get(day - 1))
                println(":// reportTime " + reportTime)
                weatherDetails = CustomApplication.dbHelper?.getCurrentWeather(AppGlobalProp.WEATHER_CITIES.get(navigationCounter), reportDate + " " + reportTime + AppGlobalProp.DYNAMIC_TIME)//+""+AppGlobalProp.DEGREE
                txtWeather.setText(weatherDetails?.get(0) + "" + AppGlobalProp.DEGREE)
                txtHumidity.setText(weatherDetails?.get(2))
                txtWind.setText(weatherDetails?.get(3))
                txtPressure.setText(weatherDetails?.get(1))
                when (i) {
                    1 -> {
                        txtDay1.setText(AppGlobalProp.WEEK_DAYS.get(day - 1))
                        txtWeatherDay1.setText(CustomApplication.dbHelper?.getUpcomingWeather(AppGlobalProp.WEATHER_CITIES.get(navigationCounter), reportDate + " 00:00:00") + "" + AppGlobalProp.DEGREE)
                    }
                    2 -> {
                        txtDay2.setText(AppGlobalProp.WEEK_DAYS.get(day - 1))
                        txtWeatherDay2.setText(CustomApplication.dbHelper?.getUpcomingWeather(AppGlobalProp.WEATHER_CITIES.get(navigationCounter), reportDate + " 00:00:00") + "" + AppGlobalProp.DEGREE)
                    }
                    3 -> {
                        txtDay3.setText(AppGlobalProp.WEEK_DAYS.get(day - 1))
                        txtWeatherDay3.setText(CustomApplication.dbHelper?.getUpcomingWeather(AppGlobalProp.WEATHER_CITIES.get(navigationCounter), reportDate + " 00:00:00") + "" + AppGlobalProp.DEGREE)
                    }
                    else -> println(":// no value")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getWeatherReport() {

        //creating volley string request
        val stringRequest = object : StringRequest(Request.Method.POST, "http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=24e580b160591da8b523b78ce7b82fad&units=metric&q=${AppGlobalProp.WEATHER_CITIES.get(navigationCounter)}",
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        //Toast.makeText(applicationContext, obj.toString()/*obj.getString("message")*/, Toast.LENGTH_LONG).show()
                        println(":// success " + obj.toString())
                        //val mainObj = obj.getJSONObject("list")
                        //println(":// success "+mainObj.toString())
                        val mainArray = obj.getJSONArray("list")
                        val cityName = obj.getJSONObject("city").getString("name")
                        println(":// name object $cityName")
                        println(":// success list " + mainArray.toString())
                        var weatherList: ArrayList<WeatherModel> = ArrayList()
                        var weather: WeatherModel

                        for (i in 0 until mainArray.length()) {

                            val mainObj: JSONObject = mainArray.getJSONObject(i).getJSONObject("main")
                            val date: String = mainArray.getJSONObject(i).getString("dt_txt")

                            val temprature = mainObj.getString("temp")
                            val tempMin = mainObj.getString("temp_min")
                            val tempMax = mainObj.getString("temp_max")
                            val pressure = mainObj.getString("pressure")
                            val humidity = mainObj.getString("humidity")

                            val windObj: JSONObject = mainArray.getJSONObject(i).getJSONObject("wind")
                            val windSpeed = windObj.getString("speed")
                            val windDeg = windObj.getString("deg")

                            weather = WeatherModel(cityName, date, temprature, tempMin, tempMax, pressure, humidity, windSpeed, windDeg)
                            weatherList.add(weather)

                            println(":// main json obj " + mainObj.toString())
                            println(":// windSpeed " + temprature)
                            println(":// windDeg " + windDeg)
                        }

                        CustomApplication.dbHelper?.persitWeatherReport(AppGlobalProp.WEATHER_CITIES.get(navigationCounter), weatherList)
                        CustomApplication.dbHelper?.getWeatherReport()
                        updateDays()
                        progressDialog?.dismiss()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        //Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                        println(":// failure " + volleyError.toString())
                        updateDays()
                        progressDialog?.dismiss()
                    }
                }) {}


        //adding request to queue
        CustomApplication.instance?.addToRequestQueue(stringRequest)
    }

    fun isNetworkConnected(): Boolean {
        try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
