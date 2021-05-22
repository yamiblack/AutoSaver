package com.b11a.android.autosaver.ui.mypage

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b11a.android.autosaver.kServerURL
import com.b11a.android.autosaver.login.models.UserDetail
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MyPageInfoViewModel: ViewModel() {
    private val userURL = kServerURL("users/userinfos")
    private val client = OkHttpClient()

    private val _detail = MutableLiveData<UserDetail>().apply {
        value = UserDetail()
    }
    val detail: LiveData<UserDetail> = _detail

    private val _loading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loading: LiveData<Boolean> = _loading

    private val _success = MutableLiveData<Boolean>().apply {
        value = false
    }
    val success: LiveData<Boolean> = _success

    fun loadData(userToken: String) {
        _loading.value = true

        val request = Request.Builder()
            .url(userURL)
            .header("Authorization", userToken)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MYINFO", e.toString())
                _loading.postValue(false)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body!!.string()
                Log.d("MYINFO", responseString)

                val jsonObject = JSONObject(responseString)

                _detail.postValue(UserDetail(
                    blood = jsonObject.getString("blood"),
                    special = jsonObject.getString("special_note"),
                    emergencyName = jsonObject.getString("emergency").split("/")[0],
                    emergencyNumber = jsonObject.getString("emergency").split("/")[1],
                    disease = jsonObject.getString("disease"),
                    medicine = jsonObject.getString("medicine")
                ))
                _loading.postValue(false)
            }
        })
    }

    fun setBloodType(bloodType: String) {
        _detail.value?.blood = bloodType
    }

    fun setSpecialNote(specialNote: String) {
        _detail.value?.special = specialNote
    }

    fun setMedicalHistory(disease: String) {
        _detail.value?.disease = disease
    }

    fun setMedicationTaken(medication: String) {
        _detail.value?.medicine = medication
    }

    fun setEmergencyName(name: String) {
        _detail.value?.emergencyName = name
    }

    fun setEmergencyNumber(number: String) {
        _detail.value?.emergencyNumber = number
    }

    fun itemSelectedListener(itemSelected: (Int)->Unit)  = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = itemSelected(position)
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }

    fun submit(userToken: String) {
        _loading.value = true

        val requestBody = FormBody.Builder()
            .add("blood", _detail.value!!.blood)
            .add("special_note", _detail.value!!.special)
            .add("emergency", "${_detail.value!!.emergencyName}/${_detail.value!!.emergencyNumber}")
            .add("disease", _detail.value!!.disease)
            .add("medicine", _detail.value!!.medicine)
            .build()
        val request = Request.Builder()
            .url(userURL)
            .header("Authorization", userToken)
            .put(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DETAIL", e.toString())
                _loading.postValue(false)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                val responseCode = response.code
                Log.d("DETAIL", responseCode.toString())

                _success.postValue(responseCode == 200)
                _loading.postValue(false)
            }
        })
    }
}