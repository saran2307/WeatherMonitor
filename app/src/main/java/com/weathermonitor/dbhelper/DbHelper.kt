package com.weathermonitor.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.weathermonitor.models.WeatherModel


class DbHelper(context: Context) : SQLiteOpenHelper(context, DBNAME, null, DBVERSION) {
    companion object {
        val DBNAME = "weather"
        val DBVERSION = 1

        val TABLE_REPORT = "report"
        val COL_CITY = "city"
        val COL_DATE = "date"
        val COL_TEMP = "temp"
        val COL_MIN_TEMP = "min_temp"
        val COL_MAX_TMEP = "max_temp"
        val COL_PRESSURE = "pressure"
        val COL_HUMIDITY = "humidity"
        val COL_WIND_SPEED = "wind_speed"
        val COL_WIND_DEG = "wind_deg"

        val CREATE_TABLE = ("create table " + TABLE_REPORT + "(" + COL_CITY
                + " TEXT, " + COL_DATE + " TEXT, " + COL_TEMP + " TEXT, " + COL_MIN_TEMP + " TEXT, " + COL_MAX_TMEP + " TEXT, " + COL_PRESSURE + " TEXT, " + COL_HUMIDITY + " TEXT, " + COL_WIND_SPEED + " TEXT, " + COL_WIND_DEG + " TEXT);");

    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CREATE_TABLE)
            println(":// table created")
        } catch (e: Exception) {

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT)
    }

    // Persit weather report details
    fun persitWeatherReport(city: String, weatherList: ArrayList<WeatherModel>) {
        try {
            println(":// city name " + city)
            var db: SQLiteDatabase = this.writableDatabase
            db.delete(TABLE_REPORT, COL_CITY + "='" + city + "'", null)
            weatherList.forEach() {
                val contentValues = ContentValues()
                contentValues.put(COL_CITY, it.getCity())
                contentValues.put(COL_DATE, it.getDate())
                contentValues.put(COL_TEMP, it.getTemprature())
                contentValues.put(COL_MIN_TEMP, it.getMinTemp())
                contentValues.put(COL_MAX_TMEP, it.getMaxTemp())
                contentValues.put(COL_PRESSURE, it.getPressure())
                contentValues.put(COL_HUMIDITY, it.getHumidity())
                contentValues.put(COL_WIND_SPEED, it.getWindSpeed())
                contentValues.put(COL_WIND_DEG, it.getWindDeg())

                db.insert(TABLE_REPORT, null, contentValues)
                println(":// inserted successfully ")
            }

            if (db != null)
                db.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrentWeather(city: String, date: String): ArrayList<String> {
        var weatherList: ArrayList<String> = ArrayList<String>()
        try {


            println(":// get method called ")
            val query = "SELECT  DISTINCT * FROM " + TABLE_REPORT + " where " + COL_CITY + "='" + city + "' and " + COL_DATE + "='" + date + "'"
            println(":// getCurrentWeather $query ")
            var db: SQLiteDatabase = this.readableDatabase
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            do {
                weatherList.add(cursor.getString(cursor.getColumnIndex(COL_TEMP)))
                weatherList.add(cursor.getString(cursor.getColumnIndex(COL_PRESSURE)))
                weatherList.add(cursor.getString(cursor.getColumnIndex(COL_HUMIDITY)))
                weatherList.add(cursor.getString(cursor.getColumnIndex(COL_WIND_SPEED)))


            } while (cursor.moveToNext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return weatherList
    }


    fun getUpcomingWeather(city: String, date: String): String? {
        try {
            println(":// get method called ")
            val query = "SELECT  * FROM " + TABLE_REPORT + " where " + COL_CITY + "='" + city + "' and " + COL_DATE + "='" + date + "'"
            println(":// query " + query)
            var db: SQLiteDatabase = this.readableDatabase
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            do {
                println(":// city " + cursor.getString(2))
                return cursor.getString(2)

            } while (cursor.moveToNext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    // fetch weather report
    fun getWeatherReport() {
        try {
            println(":// get method called ")
            val query = "SELECT  * FROM " + TABLE_REPORT
            var db: SQLiteDatabase = this.readableDatabase
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            do {
                println(":// city " + cursor.getString(0))

            } while (cursor.moveToNext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}