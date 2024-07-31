package com.example.besteducation2019.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.besteducation2019.R
import com.example.besteducation2019.databinding.FragmentNotificationsBinding

class ReytingFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)

        val radioGroup = root.findViewById<RadioGroup>(R.id.radio_group)

        val weeklyRadioButton = root.findViewById<View>(R.id.weekly_radio_button)
            .findViewById<RadioButton>(R.id.radio_button)
        val monthlyRadioButton = root.findViewById<View>(R.id.monthly_radio_button)
            .findViewById<RadioButton>(R.id.radio_button)
        val allTimeRadioButton = root.findViewById<View>(R.id.all_time_radio_button)
            .findViewById<RadioButton>(R.id.radio_button)

        weeklyRadioButton.text = "Haftalik"
        monthlyRadioButton.text = "Oylik"
        allTimeRadioButton.text = "Butun davr"

        weeklyRadioButton.setOnClickListener {
            weeklyRadioButton.isChecked = true
            monthlyRadioButton.isChecked = false
            allTimeRadioButton.isChecked = false
        }

        monthlyRadioButton.setOnClickListener {
            weeklyRadioButton.isChecked = false
            monthlyRadioButton.isChecked = true
            allTimeRadioButton.isChecked = false
        }

        allTimeRadioButton.setOnClickListener {
            weeklyRadioButton.isChecked = false
            monthlyRadioButton.isChecked = false
            allTimeRadioButton.isChecked = true
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}