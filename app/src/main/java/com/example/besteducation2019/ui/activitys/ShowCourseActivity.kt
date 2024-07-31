package com.example.besteducation2019.ui.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.besteducation2019.adapters.OuterAdapter
import com.example.besteducation2019.databinding.ActivityShowCourseBinding
import com.example.besteducation2019.model.Course2
import com.example.besteducation2019.model.Lesson
import com.example.besteducation2019.model.Module
import com.example.besteducation2019.model.Quiz
import com.example.besteducation2019.model.buy_model
import com.example.besteducation2019.model.buy_response_model
import com.example.besteducation2019.model.course_detailes_full
import com.example.besteducation2019.model.lesson_datailes
import com.example.besteducation2019.model.lesson_id_model
import com.example.besteducation2019.model.order_model
import com.example.besteducation2019.model.order_response
import com.example.besteducation2019.model.test_transfer_model
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import kotlinx.coroutines.launch

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

        binding.btnBuy.setOnClickListener {

            pay(id.toString())

        }


    }

    override fun onResume() {
        super.onResume()
        val id = intent.getStringExtra("id_course")

        getDataFromNetwork(id.toString())
    }

    fun getDataFromNetwork(id: String) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this@ShowCourseActivity, "TOKEN", ""))
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
//4
        binding.anim.visibility = View.GONE

        Log.e("ANLYZE", model.toString())
        var outerData = model.modules
        var list = arrayListOf<Module>()
        for (outerDatum in outerData) {
            if (!outerDatum.lessons.isEmpty()) {
                list.add(outerDatum)
            }
        }
        if (model.isOpen) {

            binding.btnBuy.visibility = View.GONE

        }
        binding.outerRecyclerView.adapter =
            OuterAdapter(list, object : OuterAdapter.ItemSetOnClickListener {
                override fun onClick(modul: Module, lessonXX: Lesson) {

                    if (lessonXX.type == "lesson") {
                        val mdl = lesson_id_model(id, modul.id.toString(), lessonXX.id.toString())

                        val intent = Intent(this@ShowCourseActivity, LessonsActivity::class.java)
                        intent.putExtra("LESSON", mdl)
                        startActivity(intent)
//                        finish()
                        println("$modul  $lessonXX")
                    } else {
                        Log.e("QUIZZ", lessonXX.quiz)

//                        val mdl: Quiz = lessonXX.quiz as Quiz
//                        val mdl: Quiz? = Gson().fromJson(lessonXX.quiz, Quiz::class.java)
//
                        val mdl = lesson_id_model(id, modul.id.toString(), lessonXX.id.toString())
                        startTests(mdl)
//                        val intent = Intent(this@ShowCourseActivity, TestActivity::class.java)
//                        intent.putExtra("LESSON", mdl)
//                        startActivity(intent)

                    }
                }
            }, this)


        binding.tvCourseName.text = model.name
        binding.tvDescription.text = model.description
        binding.tvFeedback.text = model.feedback.toString()
        binding.tvPrice.text = "${model.price} so'm"
        binding.tvParticantCount.text = "${model.countStudents} ta "
//        binding.tvParticantCount.text=model.

        binding.tvTeacherName.text = "${model.author_.firstName} ${model.author_.lastName}"
        binding.tvLessonsLen.text = "${model.countModules} ta videodars"
        binding.tvTestsLen.text = "${model.quizzes_count} ta sinov testi"
        val len = model.length

        if (len / 60 != 0) {
            binding.tvVideoLen.text = "${len / 60} soat ${len % 60} min"

        } else {
            binding.tvVideoLen.text = "${len % 60} min"
        }
        Glide.with(this).load(model.image).into(binding.ivAscBanner)
        Glide.with(this).load("https://bestedu.uz${model.author_.image}")
            .into(binding.ivTeacher)

    }

//    "https://bestedu.uz${dbHelper.readData().get(0).image}"

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
//              Toast.makeText(this@TestActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun pay(id: String) {


        apiService = RetrofitBuilder.create(readFromSharedPreferences(this, "TOKEN", ""))

        lifecycleScope.launch {
            try {
                // Add debug logs to check the ids
//                Log.d(
//                    "ANLZYE4",
//                    "data.id1: ${data.id1}, data.id2: ${data.id2}, data.id3: ${data.id3}"
//                )

                val request = apiService.order(order_model(id))

                if (request.isSuccessful) {
                    val body = request.body() as? order_response
                    if (body!!.status == "success") {
                        Log.e("ANLZYE5", body.toString())

                        lifecycleScope.launch {

                            try {

                                val request2 =
                                    apiService.buy(buy_model(body.data.order_id.toString(), id))
                                if (request2.isSuccessful) {

                                    val body2 = request2.body() as? buy_response_model
                                    if (body2!!.status == "success") {
                                        Log.e("ANLZYE5", body2.toString())
                                        val intent = Intent(
                                            this@ShowCourseActivity,
                                            PayPageActivity::class.java
                                        )
                                        intent.putExtra("URL", body2.data.link)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Log.e("ANLZYE5", body2.errors.toString())
                                    }

                                }

                            } catch (e: Exception) {
                                Log.e("ANLZYE5", e.message.toString())

                            }

                        }

                    } else {
                        Toast.makeText(
                            this@ShowCourseActivity,
                            body.errors.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("ANLZYE4", "Response body is null")
                    }
                } else {
                    Log.e("ANLZYE4", "Request not successful: ${request.code()}")
                }
            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())
                Toast.makeText(this@ShowCourseActivity, e.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }


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

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

}