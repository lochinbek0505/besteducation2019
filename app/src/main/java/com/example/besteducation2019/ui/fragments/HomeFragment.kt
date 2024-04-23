package com.example.besteducation2019.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.besteducation2019.adapters.HomeCoursesAdapter
import com.example.besteducation2019.databinding.FragmentHomeBinding
import com.example.besteducation2019.model.courses_model
import com.example.besteducation2019.model.courses_modelItem
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        getDataFromNetwork()

        return root
    }

    fun getDataFromNetwork(){

        apiService=RetrofitBuilder.create("7f99b2a34b123c891107311354de53b48f16e4aa")

        apiService.courses().enqueue(object : Callback<courses_model>{
            override fun onResponse(p0: Call<courses_model>, respons: Response<courses_model>) {
                val data=respons.body() as courses_model

                adapter(data.toMutableList())

            }

            override fun onFailure(p0: Call<courses_model>, p1: Throwable) {
                println("Responssssssssssssssss ${p1.message}")
            }

        })


    }

    fun adapter(list:MutableList<courses_modelItem>){


        val adapter1=HomeCoursesAdapter(list,object : HomeCoursesAdapter.ItemSetOnClickListener{
            override fun onClick(data: courses_modelItem) {
                println(data)
            }
        })

        binding.rvCourses.adapter = adapter1

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}