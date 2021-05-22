package com.b11a.android.autosaver.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.b11a.android.autosaver.databinding.ActivityJoinBinding

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
                //binding.loadingLayout.root.visibility = View.VISIBLE
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                //binding.loadingLayout.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }
}