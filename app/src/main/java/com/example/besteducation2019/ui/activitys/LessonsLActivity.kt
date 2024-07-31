package com.example.besteducation2019.ui.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.besteducation2019.R
import com.example.besteducation2019.adapters.LessonsAdapter
import com.example.besteducation2019.databinding.ActivityLessonsLactivityBinding
import com.example.besteducation2019.model.Course2
import com.example.besteducation2019.model.Quiz
import com.example.besteducation2019.model.course_detailes_full
import com.example.besteducation2019.model.lesson_datailes
import com.example.besteducation2019.model.lesson_id_model
import com.example.besteducation2019.model.test_transfer_model
import com.example.besteducation2019.model.transfer_model
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import kotlinx.coroutines.launch

class LessonsLActivity : AppCompatActivity() {

    lateinit var binding: ActivityLessonsLactivityBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonsLactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("id_course")

        getDataFromNetwork(id.toString())


    }

    override fun onResume() {
        super.onResume()
        val id = intent.getStringExtra("id_course")

        getDataFromNetwork(id.toString())
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun getDataFromNetwork(id: String) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this@LessonsLActivity, "TOKEN", ""))
        lifecycleScope.launch {

            try {
                if (apiService.courDetailes(id).isSuccessful) {

                    val data = apiService.courDetailes(id).body() as course_detailes_full
                    setDisplay(data.data.course, id)
                    Log.e("ANLYZE2", data.toString())


                }
            } catch (p1: Exception) {
                println("Responssssssssssssssss ${p1.message}")
                Log.e("ANLYZE2", p1.message.toString())

            }

        }


    }

    fun setDisplay(model: Course2, id: String) {
//
        binding.anim.visibility = View.GONE

        Log.e("ANLYZE", model.toString())
        var list = arrayListOf<transfer_model>()
        binding.tvName.text = model.name
        model.modules.forEach {


            it.lessons.forEach { it2 ->
                if (it.isOpen) {
                    list.add(transfer_model(it2, it.id))
                } else {

                    it2.isOpen = it.isOpen
                    list.add(transfer_model(it2, it.id))


                }

            }
        }
//
        val controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_right_to_left)
        binding.rvAll.layoutAnimation = controller

        binding.rvAll.adapter =
            LessonsAdapter(list, object : LessonsAdapter.ItemSetOnClickListener {
                override fun onClick(lessonXX: transfer_model) {

                    if (lessonXX.lessone.type == "lesson") {
                        val mdl = lesson_id_model(
                            id,
                            lessonXX.modul_id.toString(),
                            lessonXX.lessone.id.toString()
                        )

                        val intent = Intent(this@LessonsLActivity, LessonsActivity::class.java)
                        intent.putExtra("LESSON", mdl)
                        startActivity(intent)
                        println("  $lessonXX")
                    } else {
                        Log.e("QUIZZ", lessonXX.lessone.quiz)

//                        val mdl: Quiz = lessonXX.quiz as Quiz
//                        val mdl: Quiz? = Gson().fromJson(lessonXX.quiz, Quiz::class.java)
//
                        val mdl = lesson_id_model(
                            id,
                            lessonXX.modul_id.toString(),
                            lessonXX.lessone.id.toString()
                        )
                        startTests(mdl)
//                        val intent = Intent(this@ShowCourseActivity, TestActivity::class.java)
//                        intent.putExtra("LESSON", mdl)
//                        startActivity(intent)

                    }
                }
            }, this)


    }

    fun showTests(model: Quiz, data: lesson_id_model) {

        when (model.questions.get(0).json.type) {

            "one_select" -> {
                val model = test_transfer_model(model, 0, 0, 0)
                val intent = Intent(this, TestActivity::class.java)
                intent.putExtra("transfer_test", model)
                intent.putExtra("lesson_id", data)

                startActivity(intent)

            }


            "many_select" -> {

                val model = test_transfer_model(model, 0, 0, 0)
                val intent = Intent(this, MultiselectTestActivity::class.java)
                intent.putExtra("transfer_test", model)
                intent.putExtra("lesson_id", data)

                startActivity(intent)

            }

            "matchable" -> {


                val model = test_transfer_model(model, 0, 0, 0)
                val intent = Intent(this, MatachableActivity::class.java)
                intent.putExtra("transfer_test", model)
                intent.putExtra("lesson_id", data)

                startActivity(intent)

            }

            "writable" -> {
                val model = test_transfer_model(model, 0, 0, 0)
                val intent = Intent(this, WriteableTestActivity::class.java)
                intent.putExtra("transfer_test", model)
                intent.putExtra("lesson_id", data)

                startActivity(intent)

            }


        }

    }

    fun startTests(data: lesson_id_model) {
        apiService = RetrofitBuilder.create(readFromSharedPreferences(this, "TOKEN", ""))

        lifecycleScope.launch {
            try {
                // Add debug logs to check the ids
                Log.d(
                    "ANLZYE4",
                    "data.id1: ${data.id1}, data.id2: ${data.id2}, data.id3: ${data.id3}"
                )

                val request = apiService.lessonDeatile(data.id1, data.id2, data.id3)

                if (request.isSuccessful) {
                    val body = request.body() as? lesson_datailes
                    if (body != null) {
                        Log.e("ANLZYE4", body.toString())

//                         Ensure body.data.lesson.quiz is not null and within bounds
                        body.data?.lesson?.quiz?.let { quiz ->
//                          display(quiz)
                            showTests(quiz, data)
                        } ?: run {
                            Log.e("ANLZYE4", "Quiz data is null or out of bounds")
                        }
                    } else {
                        Log.e("ANLZYE4", "Response body is null")
                    }
                } else {
                    Log.e("ANLZYE4", "Request not successful: ${request.code()}")
                }
            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())
            }
        }


    }


}