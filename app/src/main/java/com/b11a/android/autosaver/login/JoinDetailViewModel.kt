package com.b11a.android.autosaver.login

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b11a.android.autosaver.kServerURL
import com.b11a.android.autosaver.login.models.UserDetail
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class JoinDetailViewModel: ViewModel() {
    private val _detail = MutableLiveData<UserDetail>().apply {
        value = UserDetail("A", "None")
    }
    val detail: LiveData<UserDetail> = _detail

    fun setBloodType(bloodType: String) {
        _detail.value?.blood = bloodType
    }

    fun setSpecialNote(specialNote: String) {
        _detail.value?.special = specialNote
    }

    fun setMedicalHistory(disease: String) {
        _detail.value?.disease = disease
    }

    fun setMedicationTaken(medication: String) {
        _detail.value?.medicine = medication
    }

    fun itemSelectedListener(itemSelected: (Int)->Unit)  = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = itemSelected(position)
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }
}