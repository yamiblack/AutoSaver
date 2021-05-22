package com.b11a.android.autosaver.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.b11a.android.autosaver.databinding.ActivityJoinBinding
import com.b11a.android.autosaver.kPrefs

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private lateinit var viewModel: JoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(JoinViewModel::class.java)
        setContentView(binding.root)

        viewModel.joining.observe(this) {
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
                startActivity(Intent(this, JoinDetailActivity::class.java))
                finish()
            }
        }

        viewModel.errorJoin.observe(this) {
            binding.textEmailErrorJoin.text = it.email
            binding.textPasswordErrorJoin.text = it.password1
            binding.textPasswordConfirmErrorJoin.text = it.password2
        }

        binding.editTextEmailJoin.addTextChangedListener {
            viewModel.setEmail(it.toString())
        }

        binding.editTextPasswordJoin.addTextChangedListener {
            viewModel.setPassword1(it.toString())
        }

        binding.editTextPasswordConfirmJoin.addTextChangedListener {
            viewModel.setPassword2(it.toString())
        }

        binding.buttonNextJoin.setOnClickListener {
            viewModel.join()
        }
    }
}