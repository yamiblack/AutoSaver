package com.b11a.android.autosaver.ui.analysis

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b11a.android.autosaver.kServerURL
import com.github.mikephil.charting.data.Entry
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import okhttp3.*
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AnalysisViewModel : ViewModel() {
    private val habitURL = kServerURL("drives/habit")
    private val client = OkHttpClient()

    private val _loading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loading: LiveData<Boolean> = _loading

    private val _habitArray = MutableLiveData<Array<MutableList<Entry>>>()
    val habitArray: LiveData<Array<MutableList<Entry>>> = _habitArray

    var labelList = mutableListOf<String>()

    fun writeData(userToken: String) {
        val requestBody = FormBody.Builder()
            .add("accel_x", "1.4")
            .add("accel_y", "9.7")
            .add("accel_z", "-1.0")
            .build()
        val request = Request.Builder()
            .url(habitURL)
            .header("Authorization", userToken)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ANALYSIS", e.toString())
                _loading.postValue(false)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body!!.string()
                val responseCode = response.code

                Log.d("ANALYSIS", responseString)
                Log.d("ANALYSIS", responseCode.toString())

                _loading.postValue(false)
            }
        })
    }

    fun loadData(userToken: String) {
        val request = Request.Builder()
            .url("$habitURL&week_num=1")
            .header("Authorization", userToken)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ANALYSIS", e.toString())
                _loading.postValue(false)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body!!.string()

                Log.d("ANALYSIS", responseString)

                val xList = mutableListOf<Entry>()
                val yList = mutableListOf<Entry>()
                val zList = mutableListOf<Entry>()
                val jsonArray = JsonParser.parseString(responseString) as JsonArray
                jsonArray.forEachIndexed { index, json ->
                    val jsonObject = json.asJsonObject
                    //val localDate = LocalDateTime.parse(jsonObject["timestamp"].asString)

                    labelList.add(jsonObject["timestamp"].asString)
                    xList.add(Entry(index.toFloat(), jsonObject["accel_x"].asString.toFloat()))
                    yList.add(Entry(index.toFloat(), jsonObject["accel_y"].asString.toFloat()))
                    zList.add(Entry(index.toFloat(), jsonObject["accel_z"].asString.toFloat()))
                }

                _habitArray.postValue(arrayOf(xList, yList, zList))
                _loading.postValue(false)
            }
        })
    }
}