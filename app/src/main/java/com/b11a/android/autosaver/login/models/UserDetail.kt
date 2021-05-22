package com.b11a.android.autosaver.login.models

import java.io.Serializable

data class UserDetail(
    var blood: String = "",
    var special: String = "",
    var emergencyName: String = "",
    var emergencyNumber: String = "",
    var disease: String = "",
    var medicine: String = ""
) : Serializable