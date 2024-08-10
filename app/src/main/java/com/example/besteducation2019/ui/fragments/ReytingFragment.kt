package com.example.besteducation2019.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.besteducation2019.R
import com.example.besteducation2019.adapters.CustomSpinnerAdapter
import com.example.besteducation2019.adapters.MultiLayoutAdapter
import com.example.besteducation2019.databinding.FragmentNotificationsBinding
import com.example.besteducation2019.model.Courses77
import com.example.besteducation2019.model.Ratings
import com.example.besteducation2019.model.courses_rating_model
import com.example.besteducation2019.model.get_rating_model
import com.example.besteducation2019.model.get_rating_request
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import com.example.besteducation2019.utilits.CustomLottieDialog
import kotlinx.coroutines.launch

class ReytingFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var apiService: ApiService

    private lateinit var courses: MutableList<Courses77>

    private lateinit var ratings:MutableList<Ratings>
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var weeklyRadioButton: RadioButton
    lateinit var monthlyRadioButton: RadioButton
    lateinit var allTimeRadioButton: RadioButton

    var mode = "monthly"
    var inde=0

//    {
//        "course": id,
//        "type": "monthly" or "daily" or "weekly"
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)

        val radioGroup = root.findViewById<RadioGroup>(R.id.radio_group)

        weeklyRadioButton = root.findViewById<View>(R.id.weekly_radio_button)
            .findViewById<RadioButton>(R.id.radio_button)
        monthlyRadioButton = root.findViewById<View>(R.id.monthly_radio_button)
            .findViewById<RadioButton>(R.id.radio_button)
        allTimeRadioButton = root.findViewById<View>(R.id.all_time_radio_button)
            .findViewById<RadioButton>(R.id.radio_button)

        weeklyRadioButton.text = "Kunlik"
        monthlyRadioButton.text = "Haftalik"
        allTimeRadioButton.text = "Oylik"

        courses()

        weeklyRadioButton.isChecked = false
        monthlyRadioButton.isChecked = false
        allTimeRadioButton.isChecked = false

        Log.e(
            "WERTYU",
            "${weeklyRadioButton.isChecked} ${monthlyRadioButton.isChecked} ${allTimeRadioButton.isChecked}"
        )

        if (weeklyRadioButton.isChecked) {

            mode = "daily"

            rate(get_rating_request(inde, mode))

        }

        if (monthlyRadioButton.isChecked) {

            mode = "weekly"
            rate(get_rating_request(inde, mode))


        }
        if (allTimeRadioButton.isChecked) {

            mode = "monthly"
            rate(get_rating_request(inde, mode))


        }


        weeklyRadioButton.setOnClickListener {
            mode = "daily"
            rate(get_rating_request(inde, mode))

            weeklyRadioButton.isChecked = true
            monthlyRadioButton.isChecked = false
            allTimeRadioButton.isChecked = false
        }

        monthlyRadioButton.setOnClickListener {
            mode = "weekly"
            rate(get_rating_request(inde, mode))

            weeklyRadioButton.isChecked = false
            monthlyRadioButton.isChecked = true
            allTimeRadioButton.isChecked = false
        }

        allTimeRadioButton.setOnClickListener {

            mode = "monthly"

            rate(get_rating_request(inde, mode))

            weeklyRadioButton.isChecked = false
            monthlyRadioButton.isChecked = false
            allTimeRadioButton.isChecked = true
        }




        return root
    }

    override fun onResume() {
        super.onResume()

    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }


    fun spinner(list: MutableList<Courses77>) {


        var spinner = binding.spinnerCountry

        val adapter = CustomSpinnerAdapter(requireActivity(), R.layout.custom_spinner_item, list)
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as Courses77
                inde= selectedItem.id!!
                rate(get_rating_request(inde, mode))

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case where no item is selected if necessary
            }
        }

    }

    fun courses() {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(requireActivity(), "TOKEN", ""))

        lifecycleScope.launch {

            try {

                val request = apiService.getCoursesRating()
                println(request.body())
                Log.e("ANLZYE477", request.toString())

                if (request.isSuccessful) {

                    val body = request.body() as courses_rating_model
                    Log.e("ANLZYE477", body.toString())

                    courses = body.data.courses.toMutableList()

                    spinner(courses)

                }

            } catch (e: Exception) {
                Log.e("ANLZYE477", e.message.toString())

                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun rate(data: get_rating_request) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(requireActivity(), "TOKEN", ""))

        lifecycleScope.launch {

            try {

                val request = apiService.getRating(data)
                println(request.body())
                Log.e("ANLZYE477", request.toString())

                if (request.isSuccessful) {

                    val body = request.body() as get_rating_model
//                    Log.e("ANLZYE477", body.toString())

                    ratings= body.data!!.ratings
                    Log.e("ANLZYE477", ratings.toString())

                    showRating(ratings)
//                    courses = body.data.courses.toMutableList()

//                    spinner(courses)

                }

            } catch (e: Exception) {
                Log.e("ANLZYE477", e.message.toString())

                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showRating(list:MutableList<Ratings>){

        val adapter=MultiLayoutAdapter(list)
        binding.rvRating.adapter=adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        weeklyRadioButton.isChecked = true
        monthlyRadioButton.isChecked = false
        allTimeRadioButton.isChecked = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        weeklyRadioButton.isChecked = false
        monthlyRadioButton.isChecked = false
        allTimeRadioButton.isChecked = false
    }
}