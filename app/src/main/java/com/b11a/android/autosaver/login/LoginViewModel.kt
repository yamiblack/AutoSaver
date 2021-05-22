package com.b11a.android.autosaver.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b11a.android.autosaver.login.models.UserJoin
import com.b11a.android.autosaver.login.models.UserLogin
import com.b11a.android.autosaver.serverURL
import com.b11a.android.autosaver.sha
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Pattern

class LoginViewModel: ViewModel() {
    private val loginURL = serverURL + "rest-auth/login"
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

    fun setEmail(email: String) {
        login.value?.email = email
    }

    fun setPassword(password: String) {
        login.value?.password = sha(password)
    }

    fun login() {
        _logining.value = true

        val requestBody = FormBody.Builder()
            .add("email", login.value!!.email)
            .add("password", login.value!!.password)
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
                val jsonObject = JSONObject(response.body.toString())

                (jsonObject["key"] as String?)?.let {
                    _userToken.postValue(it)
                } ?: run {
                    checkInvalid(jsonObject)
                }
                _logining.postValue(false)
            }
        })
    }

    fun checkInvalid(result: JSONObject) {
        val error = UserLogin()

        error.email = result.getString("email")
        error.password = result.getString("password")

        _errorLogin.postValue(error)
    }
}