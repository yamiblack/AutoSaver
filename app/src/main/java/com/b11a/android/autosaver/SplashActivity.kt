package com.b11a.android.autosaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.b11a.android.autosaver.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this,
                if(kPrefs(this).getBoolean("autoLogin", false)
                    && kPrefs(this).getString("userToken", "")?.isNotEmpty() == true) {
                    MainActivity::class.java
                } else {
                    LoginActivity::class.java
                }
            )
            )
            finish()
        }, 1500)
    }
}