package com.example.besteducation2019.ui.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.besteducation2019.databinding.ActivityWriteableTestBinding
import com.example.besteducation2019.model.Answer
import com.example.besteducation2019.model.Quiz
import com.example.besteducation2019.model.lesson_id_model
import com.example.besteducation2019.model.rate_request
import com.example.besteducation2019.model.request_end
import com.example.besteducation2019.model.test_result
import com.example.besteducation2019.model.test_transfer_model
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import kotlinx.coroutines.launch

class WriteableTestActivity : AppCompatActivity() {
    var response = Answer("", ",", false)
    var res_list = ArrayList<Answer>()
    private lateinit var dialog: AlertDialog
    private lateinit var apiService: ApiService
    var index = 0
    var foiz = 0
    var ball = 0
    lateinit var data2: lesson_id_model
    private lateinit var binding: ActivityWriteableTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteableTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sekk.max = 100
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        val data = intent.getSerializableExtra("transfer_test") as test_transfer_model
        data2 = intent.getSerializableExtra("lesson_id") as lesson_id_model

        foiz = data.foiz
        ball = data.ball
        index = data.index
//        network(data)
        binding.sekk.progress = foiz
        display(data)
        Log.e("QUIZZ1", data.toString())


    }

    fun givequection(quection: String) {
        binding.tvQuestion.text = quection

    }

    fun display(data: test_transfer_model) {

        val model = data.quiz


        val tittle = model.name
        val prs = 100 / model.questions.size
        binding.tvIndicate.text = "${index + 1}/${model.questions.size}"

        val tests = model.questions
        Log.e("QUIZZ12", tests.get(index).json.answers.get(0).value1.toLowerCase().toString())
        binding.tvTittle.text = tittle
        givequection(data.quiz.questions.get(index).json.question)
        binding.btnSubmit.setOnClickListener {
            if (!binding.textInputEditText.text.isNullOrBlank()) {

//            if (response.get(0).value1 != "") {
                if (tests.size - 1 > index) {
                    index++
////                    res_list.add(response)

                    Log.e(
                        "ASDFG", "${
                            binding.textInputEditText.text.toString()
                                .toLowerCase().equals(tests.get(index).json.answers.get(0).value1.toLowerCase().toString()
                                    ,false)}")

                    if (binding.textInputEditText.text.toString()
                            .toLowerCase().trim().equals(tests.get(index).json.answers.get(0).value1.toLowerCase().toString().trim()
                                ,false)                    ) {
                        ball++
                        Log.e("AANNMM", ball.toString())
                        Log.e(
                            "ORIGIN_TEST_TEST",
                            "w1 - ${binding.textInputEditText.text.toString().toLowerCase()} w2 - ${
                                tests.get(index).json.answers.get(
                                    0
                                ).value1.toString().toLowerCase()
                            }"
                        )

                    }
                    Log.e(
                        "ORIGIN_TEST_TEST",
                        "w1 - ${binding.textInputEditText.text.toString().toLowerCase()} w2 - ${
                            tests.get(index).json.answers.get(
                                0
                            ).value1.toString().toLowerCase()
                        }"
                    )
//                    response = Answer("", "", false)
                    foiz += prs * 1
                    binding.sekk.progress = foiz
                    when (tests.get(index).json.type) {

                        "one_select" -> {
                            val model = test_transfer_model(model, ball, foiz, index)
                            val intent = Intent(this, TestActivity::class.java)
                            intent.putExtra("transfer_test", model)
                            intent.putExtra("lesson_id", data2)

                            startActivity(intent)
                            finish()

                        }


                        "many_select" -> {

                            val model = test_transfer_model(model, ball, foiz, index)
                            val intent = Intent(this, MultiselectTestActivity::class.java)
                            intent.putExtra("transfer_test", model)
                            intent.putExtra("lesson_id", data2)

                            startActivity(intent)
                            finish()

                        }

                        "matchable" -> {


                            val model = test_transfer_model(model, ball, foiz, index)
                            val intent = Intent(this, MatachableActivity::class.java)
                            intent.putExtra("transfer_test", model)
                            intent.putExtra("lesson_id", data2)

                            startActivity(intent)
                            finish()

                        }

                        "writable" -> {

                            givequection(data.quiz.questions.get(index).json.question)

                        }


                    }

                    println("Javob : $response")
                } else {
                    foiz += prs * 1
                    binding.sekk.progress = foiz

                    Log.e(
                        "ASDFG", "${
                            binding.textInputEditText.text.toString()
                                .toLowerCase().equals(tests.get(index).json.answers.get(0).value1.toLowerCase().toString()
                                    ,false)
                        }"
                    )


                    if (binding.textInputEditText.text.toString().trim().equals(tests.get(index).json.answers.get(0).value1.toLowerCase().toString().trim()
                        ,false) ) {
                        ball++
                        Log.e("AANNMM", ball.toString())

                    }
                    Log.e(
                        "ORIGIN_TEST_TEST",
                        "w1 - ${binding.textInputEditText.text.toString().toLowerCase()} w2 - ${
                            tests.get(index).json.answers.get(
                                0
                            ).value1.toString().toLowerCase()
                        }"
                    )
//            res_list.add(response)
//            response = Answer("", "", false)

                    check(model, tittle)
                    println("Finished")

                }
            } else {


                Toast.makeText(this, "Iltimos birorta javobni tanlang", Toast.LENGTH_LONG).show()

            }
        }

    }

    fun check(response: Quiz, title: String) {


//        var index = 0
//        for (question in response) {
//            if (question.isCorrect) {
//
//                ball++
//
//            }
//            index++
//        }

        end(data2, response, title)

        endQuiz(data2)
    }

    fun endQuiz(data: lesson_id_model) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this, "TOKEN", ""))

        lifecycleScope.launch {

            try {


                val request =
                    apiService.saveRating(rate_request(data.id1, data.id3, data.id2, foiz, ball))

                println(request.body())
                Log.e("ANLZYE455", request.body().toString())

                if (request.isSuccessful) {

//                    val intent = Intent(this@TestActivity, TestResultActivity::class.java)
//                    intent.putExtra("TRA", test_result(title, ball, response.questions.size))
//                    startActivity(intent)
//                    finish()
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@WriteableTestActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }


    fun end(data: lesson_id_model, response: Quiz, title: String) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this, "TOKEN", ""))

        lifecycleScope.launch {

            try {


                val request = apiService.endLessons(request_end(data.id3))
                println(request.body())
                Log.e("ANLZYE4", request.toString())

                if (request.isSuccessful) {
                    val intent = Intent(this@WriteableTestActivity, TestResultActivity::class.java)
                    intent.putExtra("TRA", test_result(title, ball, response.questions.size))
                    startActivity(intent)
                    finish()
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@WriteableTestActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}