package com.b11a.android.autosaver.ui.analysis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.b11a.android.autosaver.R
import com.b11a.android.autosaver.databinding.FragmentAnalysisBinding
import com.b11a.android.autosaver.kPrefs
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AnalysisFragment : Fragment() {
    private lateinit var binding: FragmentAnalysisBinding
    private lateinit var viewModel: AnalysisViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAnalysisBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AnalysisViewModel::class.java)

        val userToken = kPrefs(requireContext()).getString("userToken", "")!!

        viewModel.loadData(userToken)

        viewModel.habitArray.observe(viewLifecycleOwner) {
            val xLineDataSet = LineDataSet(it[0], "X axis").apply {
                color = ContextCompat.getColor(requireContext(), R.color.chart1)
                circleColors = listOf(ContextCompat.getColor(requireContext(), R.color.chart1))
            }
            val yLineDataSet = LineDataSet(it[1], "Y axis").apply {
                color = ContextCompat.getColor(requireContext(), R.color.chart2)
                circleColors = listOf(ContextCompat.getColor(requireContext(), R.color.chart2))
            }
            val zLineDataSet = LineDataSet(it[2], "Z axis").apply {
                color = ContextCompat.getColor(requireContext(), R.color.chart3)
                circleColors = listOf(ContextCompat.getColor(requireContext(), R.color.chart3))
            }

            val lineData = LineData().apply {
                addDataSet(xLineDataSet)
                addDataSet(yLineDataSet)
                addDataSet(zLineDataSet)
            }

            binding.lineChart.data = lineData
            binding.lineChart.invalidate()
        }

        binding.lineChart.apply {
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false

            xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if(value % 1 == 0f) viewModel.labelList[value.toInt()] else ""
                }
            }

            description.isEnabled = false
        }

        return binding.root
    }
}