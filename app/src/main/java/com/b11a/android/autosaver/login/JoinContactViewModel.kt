package com.b11a.android.autosaver.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b11a.android.autosaver.kServerURL
import com.b11a.android.autosaver.login.models.UserDetail
import okhttp3.*
import java.io.IOException

class JoinContactViewModel : ViewModel() {
    private val detailURL = kServerURL("users/userinfos")
    private val client = OkHttpClient()

    private val _detail = MutableLiveData<UserDetail>().apply {
        value = UserDetail()
    }
    val detail: LiveData<UserDetail> = _detail

    private val _joining = MutableLiveData<Boolean>().apply {
        value = false
    }
    val joining: LiveData<Boolean> = _joining

    private val _error = MutableLiveData<String>().apply {
        value = ""
    }
    val error: LiveData<String> = _error

    private val _success = MutableLiveData<Boolean>().apply {
        value = false
    }
    val success: LiveData<Boolean> = _success

    fun initializeDetail(detail: UserDetail) {
        _detail.value = detail
    }

    fun setEmergencyName(name: String) {
        _detail.value?.emergencyName = name
    }

    fun setEmergencyNumber(number: String) {
        _detail.value?.emergencyNumber = number
    }

    fun submit(userToken: String) {
        _joining.value = true

        val requestBody = FormBody.Builder()
            .add("blood", _detail.value!!.blood)
            .add("special_note", _detail.value!!.special)
            .add("emergency", "${_detail.value!!.emergencyName}/${_detail.value!!.emergencyNumber}")
            .add("disease", _detail.value!!.disease)
            .add("medicine", _detail.value!!.medicine)
            .build()
        val request = Request.Builder()
            .url(detailURL)
            .header("Authorization", userToken)
            .put(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DETAIL", e.toString())
                _joining.postValue(false)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                val responseCode = response.code
                Log.d("DETAIL", responseCode.toString())

                _success.postValue(responseCode == 200)
                _joining.postValue(false)
            }
        })
    }
}