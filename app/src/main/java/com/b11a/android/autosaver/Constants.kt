package com.b11a.android.autosaver

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.lang.Exception
import java.security.MessageDigest

enum class JoinStatus { FAIL, SUCCESS, NEED }

fun kServerURL(path: String): String = "https://junction-auto.com/$path/?format=json"
fun kPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

fun hashSHA256(str: String): String {
    var result: String
    try {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(str.toByteArray(charset("UTF-8")))

        val byteData = md.digest()
        val stringBuffer = StringBuffer()

        for (i in byteData.indices)
            stringBuffer.append(((byteData[i].toInt() and 0xff) + 0x100).toString(16).substring(1))
        result = stringBuffer.toString()
    } catch(e: Exception) {
        e.printStackTrace()
        result = ""
    }

    return result
}