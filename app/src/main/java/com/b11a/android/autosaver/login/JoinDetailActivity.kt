package com.b11a.android.autosaver.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.b11a.android.autosaver.R
import com.b11a.android.autosaver.databinding.ActivityJoinDetailBinding
import com.b11a.android.autosaver.kPrefs

class JoinDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinDetailBinding
    private lateinit var viewModel: JoinDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinDetailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(JoinDetailViewModel::class.java)
        setContentView(binding.root)

        binding.spinnerBloodDetail.onItemSelectedListener = viewModel.itemSelectedListener { position ->
            val selected = binding.spinnerBloodDetail.getItemAtPosition(position).toString()
            viewModel.setBloodType(selected)
        }

        binding.spinnerSpecialDetail.onItemSelectedListener = viewModel.itemSelectedListener { position ->
            val selected = binding.spinnerSpecialDetail.getItemAtPosition(position).toString()
            viewModel.setSpecialNote(selected)
        }

        binding.editTextMedicalDetail.addTextChangedListener {
            viewModel.setMedicalHistory(it.toString())
        }

        binding.editTextMedicationDetail.addTextChangedListener {
            viewModel.setMedicationTaken(it.toString())
        }

        binding.buttonCompletionDetail.setOnClickListener {
            startActivity(Intent(this, JoinContactActivity::class.java)
                .putExtra("detail", viewModel.detail.value))
        }
    }
}