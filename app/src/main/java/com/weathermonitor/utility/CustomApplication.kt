package com.weathermonitor.utility

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.weathermonitor.dbhelper.DbHelper

class CustomApplication : Application() {
    companion object {
        private val TAG = CustomApplication::class.java.simpleName
        @get:Synchronized
        var instance: CustomApplication? = null
            private set

        var dbHelper: DbHelper? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        dbHelper = DbHelper(this)
        dbHelper?.writableDatabase
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }


}