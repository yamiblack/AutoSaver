package com.b11a.android.autosaver.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.b11a.android.autosaver.JoinStatus
import com.b11a.android.autosaver.MainActivity
import com.b11a.android.autosaver.databinding.ActivityLoginBinding
import com.b11a.android.autosaver.kPrefs

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)

        viewModel.logining.observe(this) {
            if (it) {
                binding.loadingLayout.root.visibility = View.VISIBLE
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.loadingLayout.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }

        viewModel.userToken.observe(this) {
            it?.let {
                kPrefs(binding.root.context).edit().putString("userToken", "Token $it").apply()
            }
        }

        viewModel.errorLogin.observe(this) {
            binding.textEmailErrorLogin.text = it.email
            binding.textPasswordErrorLogin.text = it.password
        }

        viewModel.success.observe(this) {
            if(it != JoinStatus.FAIL)
                startActivity(Intent(this, when(it) {
                        JoinStatus.SUCCESS -> MainActivity::class.java
                        JoinStatus.NEED -> JoinDetailActivity::class.java
                        else -> null
                    }
                ))
        }

        binding.editTextEmailLogin.addTextChangedListener {
            viewModel.setEmail(it.toString())
        }

        binding.editTextPasswordLogin.addTextChangedListener {
            viewModel.setPassword(it.toString())
        }

        binding.buttonLogin.setOnClickListener {
            viewModel.login()
        }

        binding.buttonSignup.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }
    }
}