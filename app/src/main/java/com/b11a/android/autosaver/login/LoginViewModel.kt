package com.b11a.android.autosaver.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b11a.android.autosaver.JoinStatus
import com.b11a.android.autosaver.hashSHA256
import com.b11a.android.autosaver.login.models.UserLogin
import com.b11a.android.autosaver.kServerURL
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginViewModel: ViewModel() {
    private val loginURL = kServerURL("rest-auth/login")
    private val userURL = kServerURL("users/userinfos")
    private val client = OkHttpClient()

    private val _login = MutableLiveData<UserLogin>().apply {
        value = UserLogin()
    }
    val login: LiveData<UserLogin> = _login

    private val _logining = MutableLiveData<Boolean>().apply {
        value = false
    }
    val logining: LiveData<Boolean> = _logining

    private val _userToken = MutableLiveData<String?>().apply {
        value = null
    }
    val userToken: LiveData<String?> = _userToken

    private val _errorLogin = MutableLiveData<UserLogin>().apply {
        value = UserLogin()
    }
    val errorLogin: LiveData<UserLogin> = _errorLogin

    private val _success = MutableLiveData<JoinStatus>().apply {
        value = JoinStatus.FAIL
    }
    val success: LiveData<JoinStatus> = _success

    fun setEmail(email: String) {
        _login.value?.email = email
    }

    fun setPassword(password: String) {
        _login.value?.password = password
    }

    fun login() {
        _logining.value = true

        val requestBody = FormBody.Builder()
            .add("email", _login.value!!.email)
            .add("password", hashSHA256(_login.value!!.password))
            .build()
        val request = Request.Builder()
            .url(loginURL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("LOGIN", e.toString())
                _logining.postValue(false)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body!!.string()
                Log.d("LOGIN", responseString)

                val jsonObject = JSONObject(responseString)

                if (jsonObject.has("key")) {
                    _userToken.postValue(jsonObject.getString("key"))
                    checkDetail()
                } else checkInvalid(jsonObject)
            }
        })
    }

    fun checkInvalid(result: JSONObject) {
        val error = UserLogin()

        if(result.has("email")) error.email = result.getJSONArray("email").getString(0)
        if(result.has("password")) error.password = result.getJSONArray("password").getString(0)
        if(result.has("non_field_errors")) error.email = result.getJSONArray("non_field_errors").getString(0)

        _errorLogin.postValue(error)
    }

    fun checkDetail() {
        val request = Request.Builder()
            .url(userURL)
            .header("Authorization", "Token ${_userToken.value!!}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("LOGIN", e.toString())
                _logining.postValue(false)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body!!.string()
                Log.d("LOGIN", responseString)

                val jsonObject = JSONObject(responseString)

                _success.postValue(
                    if(jsonObject.getString("blood").isEmpty()) JoinStatus.NEED
                    else JoinStatus.SUCCESS
                )
                _logining.postValue(false)
            }
        })
    }
}