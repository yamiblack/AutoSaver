package com.b11a.android.autosaver

import java.lang.Exception
import java.security.MessageDigest

val serverURL = "https://junction-auto.com/"

fun sha(str: String): String {
    var result: String
    try {
        val md = MessageDigest.getInstance("SHA")
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