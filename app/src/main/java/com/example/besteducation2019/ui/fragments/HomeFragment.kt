package com.example.besteducation2019.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.besteducation2019.adapters.HomeCoursesAdapter
import com.example.besteducation2019.adapters.HomeSubjectAdapter
import com.example.besteducation2019.databinding.FragmentHomeBinding
import com.example.besteducation2019.model.Course
import com.example.besteducation2019.model.Subject
import com.example.besteducation2019.model.course_model
import com.example.besteducation2019.model.subject_model
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import com.example.besteducation2019.ui.activitys.ShowCourseActivity
import com.example.besteducation2019.utilits.DatabaseHelper
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var apiService: ApiService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dbHelper = DatabaseHelper(requireActivity())
        val users = dbHelper.readData()
        for (user in users) {
            binding.tvHomeName.text = "Salom , ${user.firstName}"
            Glide.with(requireActivity()).load(user.image).into(binding.ivHomeImage)
        }
        val token = readFromSharedPreferences(requireActivity(), "TOKEN", "")
        Log.e("RESPONSSSSS", "  $token ")

        getDataFromNetwork(token.toString())
        getDataFromNetwork2(token)
        return root
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun getDataFromNetwork(token: String) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e("RESPONSSSSS", "  ${apiService.courses().isSuccessful} ")

                if (apiService.courses().isSuccessful) {

                    val body = apiService.courses().body() as course_model

                    adapter(body.data.courses.toMutableList())

                    Log.e("RESPONSSSSS", "  ${body} ")

                }

            } catch (e: Exception) {
                Log.e("RESPONSSSSS", "  ${e.message} ")

            }


        }


//        apiService.courses().enqueue(object : Callback<courses_model>{
//            override fun onResponse(p0: Call<courses_model>, respons: Response<courses_model>) {
//                val data = respons.body()
//
//            }
//
//            override fun onFailure(p0: Call<courses_model>, p1: Throwable) {
//            }
//
//        })


    }

    fun getDataFromNetwork3(token: String, id: String) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e("RESPONSSSSS", "  ${apiService.getSubjectCourse(id).isSuccessful} ")

                if (apiService.getSubjectCourse(id).isSuccessful) {

                    val body = apiService.getSubjectCourse(id).body() as course_model

                    adapter(body.data.courses.toMutableList())

                    Log.e("RESPONSSSSS", "  ${body} ")

                }

            } catch (e: Exception) {
                Log.e("RESPONSSSSS", "  ${e.message} ")

            }


        }


//        apiService.courses().enqueue(object : Callback<courses_model>{
//            override fun onResponse(p0: Call<courses_model>, respons: Response<courses_model>) {
//                val data = respons.body()
//
//            }
//
//            override fun onFailure(p0: Call<courses_model>, p1: Throwable) {
//            }
//
//        })


    }

    fun getDataFromNetwork2(token: String) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e("RESPONSSSSS", "  ${apiService.getSubjects().isSuccessful} ")

                if (apiService.getSubjects().isSuccessful) {

                    val body = apiService.getSubjects().body() as subject_model

                    adapter2(body, token)

                    Log.e("RESPONSSSSS", "  ${body} ")

                }

            } catch (e: Exception) {
                Log.e("RESPONSSSSS", "  ${e.message} ")

            }


        }


//        apiService.courses().enqueue(object : Callback<courses_model>{
//            override fun onResponse(p0: Call<courses_model>, respons: Response<courses_model>) {
//                val data = respons.body()
//
//            }
//
//            override fun onFailure(p0: Call<courses_model>, p1: Throwable) {
//            }
//
//        })


    }

    fun adapter2(model: subject_model, token: String) {

        var listt = model.data.subjects.toMutableList()

        listt.add(0, Subject("Barchasi", -1))
        binding.rvCategory.adapter = HomeSubjectAdapter(
            requireActivity(),
            listt,
            object : HomeSubjectAdapter.ItemSetOnClickListener {
                override fun onClick(data: Subject) {
                    if (data.id == -1) {
                        getDataFromNetwork(token)
                    } else {
                        getDataFromNetwork3(token, data.id.toString())
                    }
                    println(data.id)
                }

            })

    }

    fun adapter(list: MutableList<Course>) {


        val adapter1 = HomeCoursesAdapter(
            requireActivity(),
            list,
            object : HomeCoursesAdapter.ItemSetOnClickListener {
                override fun onClick(data: Course) {
                    Log.e("ANLYZE", data.toString())

                    val intent = Intent(requireActivity(), ShowCourseActivity::class.java)
                    intent.putExtra("id_course", data.id.toString())
                    startActivity(intent)
            }
        })

        binding.rvCourses.adapter = adapter1

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}