package com.b11a.android.autosaver.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.b11a.android.autosaver.R
import com.b11a.android.autosaver.databinding.FragmentMypageBinding
import com.b11a.android.autosaver.databinding.LayoutMypageEmergencyBinding
import com.b11a.android.autosaver.databinding.LayoutMypageInfoBinding
import com.b11a.android.autosaver.kPrefs

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private lateinit var infoBinding: LayoutMypageInfoBinding
    private lateinit var emergencyBinding: LayoutMypageEmergencyBinding
    private lateinit var viewModel: MyPageInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMypageBinding.inflate(inflater)
        infoBinding = LayoutMypageInfoBinding.inflate(inflater)
        emergencyBinding = LayoutMypageEmergencyBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(MyPageInfoViewModel::class.java)

        val userToken = kPrefs(requireContext()).getString("userToken", "")!!

        binding.frameMyPage.addView(infoBinding.root)
        viewModel.loadData(userToken)

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingLayout.root.visibility = View.VISIBLE
                requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.loadingLayout.root.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }

        viewModel.detail.observe(viewLifecycleOwner) {
            infoBinding.spinnerBloodMyPage.setSelection(resources.getStringArray(R.array.bloodType).indexOf(it.blood))
            infoBinding.spinnerSpecialMyPage.setSelection(resources.getStringArray(R.array.specialNote).indexOf(it.special))
            infoBinding.editTextMedicalMyPage.setText(it.disease)
            infoBinding.editTextMedicationMyPage.setText(it.medicine)
        }

        infoBinding.spinnerBloodMyPage.onItemSelectedListener = viewModel.itemSelectedListener { position ->
            val selected = infoBinding.spinnerBloodMyPage.getItemAtPosition(position).toString()
            viewModel.setBloodType(selected)
        }

        infoBinding.spinnerSpecialMyPage.onItemSelectedListener = viewModel.itemSelectedListener { position ->
            val selected = infoBinding.spinnerSpecialMyPage.getItemAtPosition(position).toString()
            viewModel.setSpecialNote(selected)
        }

        infoBinding.editTextMedicalMyPage.addTextChangedListener {
            viewModel.setMedicalHistory(it.toString())
        }

        infoBinding.editTextMedicationMyPage.addTextChangedListener {
            viewModel.setMedicationTaken(it.toString())
        }

        emergencyBinding.editTextNameMyPage.addTextChangedListener {
            viewModel.setEmergencyName(it.toString())
        }

        emergencyBinding.editTextNumberMyPage.addTextChangedListener {
            viewModel.setEmergencyNumber(it.toString())
        }

        binding.buttonMyInfoMyPage.setOnClickListener {
            binding.buttonMyInfoMyPage.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius_button_blue_activated)
            binding.buttonMyInfoMyPage.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

            binding.buttonContactsMyPage.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius_button_blue)
            binding.buttonContactsMyPage.setTextColor(ContextCompat.getColor(requireContext(), R.color.buttonTextColor))

            binding.frameMyPage.removeAllViews()
            binding.frameMyPage.addView(infoBinding.root)

            infoBinding.spinnerBloodMyPage.setSelection(resources.getStringArray(R.array.bloodType).indexOf(viewModel.detail.value!!.blood))
            infoBinding.spinnerSpecialMyPage.setSelection(resources.getStringArray(R.array.specialNote).indexOf(viewModel.detail.value!!.special))
            infoBinding.editTextMedicalMyPage.setText(viewModel.detail.value!!.disease)
            infoBinding.editTextMedicationMyPage.setText(viewModel.detail.value!!.medicine)
        }

        binding.buttonContactsMyPage.setOnClickListener {
            binding.buttonContactsMyPage.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius_button_blue_activated)
            binding.buttonContactsMyPage.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

            binding.buttonMyInfoMyPage.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius_button_blue)
            binding.buttonMyInfoMyPage.setTextColor(ContextCompat.getColor(requireContext(), R.color.buttonTextColor))

            binding.frameMyPage.removeAllViews()
            binding.frameMyPage.addView(emergencyBinding.root)

            emergencyBinding.editTextNameMyPage.setText(viewModel.detail.value!!.emergencyName)
            emergencyBinding.editTextNumberMyPage.setText(viewModel.detail.value!!.emergencyNumber)
        }

        infoBinding.buttonCompletionMyPage.setOnClickListener {
            viewModel.submit(userToken)
        }
        
        emergencyBinding.buttonCompletionMyPageContact.setOnClickListener {
            viewModel.submit(userToken)
        }

        return binding.root
    }
}