package com.example.besteducation2019.ui.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.besteducation2019.adapters.OuterAdapter
import com.example.besteducation2019.databinding.ActivityShowCourseBinding
import com.example.besteducation2019.model.Course2
import com.example.besteducation2019.model.Lesson
import com.example.besteducation2019.model.Module
import com.example.besteducation2019.model.course_detailes_full
import com.example.besteducation2019.model.lesson_id_model
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowCourseActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityShowCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id_course")

        getDataFromNetwork(id.toString())
        Log.e("ANLYZE2", id.toString())


    }

    fun getDataFromNetwork(id: String) {

        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this@ShowCourseActivity, "TOKEN", ""))

        apiService.courDetailes(id).enqueue(object : Callback<course_detailes_full> {
            override fun onResponse(
                p0: Call<course_detailes_full>,
                respons: Response<course_detailes_full>
            ) {
                val data = respons.body() as course_detailes_full

                setDisplay(data.data.course, id)
                Log.e("ANLYZE2", respons.body().toString())

            }

            override fun onFailure(p0: Call<course_detailes_full>, p1: Throwable) {
                println("Responssssssssssssssss ${p1.message}")
                Log.e("ANLYZE2", p1.message.toString())

            }

        })


    }


    fun setDisplay(model: Course2, id: String) {
//
        Log.e("ANLYZE", model.toString())
        var outerData = model.modules
        var list = arrayListOf<Module>()
        for (outerDatum in outerData) {
            if (!outerDatum.lessons.isEmpty()) {
                list.add(outerDatum)
            }
        }
//
        binding.outerRecyclerView.adapter =
            OuterAdapter(list, object : OuterAdapter.ItemSetOnClickListener {
                override fun onClick(modul: Module, lessonXX: Lesson) {

                    if (lessonXX.type == "lesson") {
                        val mdl = lesson_id_model(id, modul.id.toString(), lessonXX.id.toString())

                        val intent = Intent(this@ShowCourseActivity, LessonsActivity::class.java)
                        intent.putExtra("LESSON", mdl)
                        startActivity(intent)
                        println("$modul  $lessonXX")
                    } else {
                        Log.e("QUIZZ", lessonXX.quiz)

//                        val mdl: Quiz = lessonXX.quiz as Quiz
//                        val mdl: Quiz? = Gson().fromJson(lessonXX.quiz, Quiz::class.java)
//
                        val mdl = lesson_id_model(id, modul.id.toString(), lessonXX.id.toString())

                        val intent = Intent(this@ShowCourseActivity, TestActivity::class.java)
                        intent.putExtra("LESSON", mdl)
                        startActivity(intent)
                    }
                }
            }, this)


        binding.tvCourseName.text = model.name
        binding.tvDescription.text = model.description
        binding.tvFeedback.text = model.feedback.toString()
        binding.tvPrice.text = "${model.price} so'm"
//        binding.tvParticantCount.text="${model.}"
//        binding.tvParticantCount.text=model.

        binding.tvTeacherName.text = "${model.author_.firstName} ${model.author_.lastName}"
        binding.tvLessonsLen.text = "${model.countModules} ta videodars"
//        binding.tvTestsLen.text = "${model.c} ta sinov testi"
        val len = model.length

        if (len / 60 != 0) {
            binding.tvVideoLen.text = "${len / 60} soat ${len % 60} min"

        } else {
            binding.tvVideoLen.text = "${len % 60} min"
        }
        Glide.with(this).load(model.image).into(binding.ivAscBanner)
        Glide.with(this).load("http://147.45.158.162:9060/${model.author_.image}")
            .into(binding.ivTeacher)

    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

}