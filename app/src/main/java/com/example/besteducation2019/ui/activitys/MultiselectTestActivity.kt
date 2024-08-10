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
import com.example.besteducation2019.R
import com.example.besteducation2019.databinding.ActivityMultiselectTestBinding
import com.example.besteducation2019.model.Answer
import com.example.besteducation2019.model.Question
import com.example.besteducation2019.model.Quiz
import com.example.besteducation2019.model.lesson_id_model
import com.example.besteducation2019.model.rate_request
import com.example.besteducation2019.model.request_end
import com.example.besteducation2019.model.test_result
import com.example.besteducation2019.model.test_transfer_model
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import kotlinx.coroutines.launch

class MultiselectTestActivity : AppCompatActivity() {
    var response = arrayListOf<Answer>()
    var res_list = ArrayList<Answer>()
    private lateinit var dialog: AlertDialog
    private lateinit var apiService: ApiService
    lateinit var data2: lesson_id_model
    var index = 0
    var foiz = 0
    var ball = 0
    private lateinit var binding: ActivityMultiselectTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiselectTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sekk.max = 100

        val data = intent.getSerializableExtra("transfer_test") as test_transfer_model
        foiz = data.foiz
        ball = data.ball
        index = data.index
//        network(data)
        display(data)
        binding.sekk.progress = foiz
        data2 = intent.getSerializableExtra("lesson_id") as lesson_id_model
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        Log.e("QUIZZ1", data.toString())

    }

    fun display(data: test_transfer_model) {

        val model = data.quiz


        val tittle = model.name
        val prs = 100 / model.questions.size
        binding.tvIndicate.text = "${index + 1}/${model.questions.size}"

        val tests = model.questions
        Log.e("QUIZZ1", model.toString())
        binding.tvTittle.text = tittle
        giveQuestion(tests, index)
        var count = 0
        tests.get(index).json.answers.forEach {

            if (it.isCorrect) {
                count++
            }

        }
        Log.e("AANNMM", tests.toString())
        binding.btnSubmit.setOnClickListener {
            if (!response.isNullOrEmpty()) {

//            if (response.get(0).value1 != "") {
                if (tests.size - 1 > index) {
                    index++
                    var count2 = 0
                    response.forEach {

                        if (it.isCorrect) {
                            count2++
                        }

                    }
                    Log.e("ORIGIN_TEST_TEST", "c2 - $count2 c1- $count")
////                    res_list.add(response)
                    if (count == count2) {
                        ball++
                    }

//                    response = Answer("", "", false)
                    foiz += prs * 1
                    binding.sekk.progress = foiz
                    when (tests.get(index).json.type) {

                        "one_select" -> {
                            giveQuestion(tests, index)

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
                            val model = test_transfer_model(model, ball, foiz, index)
                            val intent = Intent(this, WriteableTestActivity::class.java)
                            intent.putExtra("transfer_test", model)
                            intent.putExtra("lesson_id", data2)

                            startActivity(intent)
                            finish()
                        }


                    }

                    println("Javob : $response")
                } else {
                    foiz += prs * 1
                    binding.sekk.progress = foiz
//            res_list.add(response)
//            response = Answer("", "", false)
                    var count2 = 0
                    response.forEach {

                        if (it.isCorrect) {
                            count2++
                        }

                    }
                    Log.e("ORIGIN_TEST_TEST", "c2 - $count2 c1- $count")

////                    res_list.add(response)
                    if (count == count2) {
                        ball++
                    }

                    check(model, tittle)
                    println("Finished")

                }
            } else {


                Toast.makeText(this, "Iltimos birorta javobni tanlang", Toast.LENGTH_LONG).show()

            }
        }
        var a1 = true
        binding.cvA.setOnClickListener {
//            response = "a"

            var aaa = model.questions.get(index).json.answers
            if (a1) {
                binding.cvA.setBackgroundResource(R.drawable.selected_bg)
                a1 = false
                response.add(Answer(aaa.get(0).value1, "", aaa.get(0).isCorrect))

            } else {
                binding.cvA.setBackgroundResource(R.drawable.defould_bg)
                a1 = true
                response.remove(Answer(aaa.get(0).value1, "", aaa.get(0).isCorrect))

            }
//            binding.cvB.setBackgroundResource(R.drawable.defould_bg)
//            binding.cvC.setBackgroundResource(R.drawable.defould_bg)
//            binding.cvD.setBackgroundResource(R.drawable.defould_bg)
//            Toast.makeText(this, "Iltimos birorta javobni tanlang", Toast.LENGTH_LONG).show()

        }
        var c1 = true
        binding.cvB.setOnClickListener {
//            response = "b"
            var aaa = model.questions.get(index).json.answers

            if (c1) {
                binding.cvB.setBackgroundResource(R.drawable.selected_bg)
                c1 = false
                response.add(Answer(aaa.get(1).value1, "", aaa.get(1).isCorrect))

            } else {
                binding.cvB.setBackgroundResource(R.drawable.defould_bg)
                c1 = true
                response.remove(Answer(aaa.get(1).value1, "", aaa.get(1).isCorrect))

            }
//            binding.cvA.setBackgroundResource(R.drawable.defould_bg)
//            binding.cvB.setBackgroundResource(R.drawable.selected_bg)
//            binding.cvC.setBackgroundResource(R.drawable.defould_bg)
//            binding.cvD.setBackgroundResource(R.drawable.defould_bg)
//            response = Answer(aaa.get(1).value1, "", aaa.get(1).isCorrect)

        }
        var b1 = true
        binding.cvC.setOnClickListener {
//            response = "c"
            var aaa = model.questions.get(index).json.answers


            if (b1) {
                binding.cvC.setBackgroundResource(R.drawable.selected_bg)
                b1 = false
                response.add(Answer(aaa.get(2).value1, "", aaa.get(2).isCorrect))

            } else {
                binding.cvC.setBackgroundResource(R.drawable.defould_bg)
                b1 = true
                response.remove(Answer(aaa.get(2).value1, "", aaa.get(2).isCorrect))

            }
//            binding.cvA.setBackgroundResource(R.drawable.defould_bg)
//            binding.cvB.setBackgroundResource(R.drawable.defould_bg)
//            binding.cvC.setBackgroundResource(R.drawable.selected_bg)
//            binding.cvD.setBackgroundResource(R.drawable.defould_bg)
//            response = Answer(aaa.get(2).value1, "", aaa.get(2).isCorrect)

        }
        var d1 = true
        binding.cvD.setOnClickListener {
//            response = "d"
            var aaa = model.questions.get(index).json.answers

            if (d1) {
                binding.cvD.setBackgroundResource(R.drawable.selected_bg)
                d1 = false
                response.add(Answer(aaa.get(3).value1, "", aaa.get(3).isCorrect))

            } else {
                binding.cvD.setBackgroundResource(R.drawable.defould_bg)
                d1 = true
                response.remove(Answer(aaa.get(3).value1, "", aaa.get(3).isCorrect))

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


                val request = apiService.saveRating(rate_request(data.id1,data.id3,data.id2,foiz,ball))

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

                Toast.makeText(this@MultiselectTestActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
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
                    val intent =
                        Intent(this@MultiselectTestActivity, TestResultActivity::class.java)
                    intent.putExtra("TRA", test_result(title, ball, response.questions.size))
                    startActivity(intent)
                    finish()
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@MultiselectTestActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun giveQuestion(list: List<Question>, intex: Int) {

        binding.cvA.setBackgroundResource(R.drawable.defould_bg)
        binding.cvB.setBackgroundResource(R.drawable.defould_bg)
        binding.cvC.setBackgroundResource(R.drawable.defould_bg)
        binding.cvD.setBackgroundResource(R.drawable.defould_bg)
        val data = list.get(intex)
        binding.tvQuestion.text = data.json.question
        binding.tvA.text = data.json.answers.get(0).value1
        binding.tvB.text = data.json.answers.get(1).value1
        binding.tvC.text = data.json.answers.get(2).value1
        binding.tvD.text = data.json.answers.get(3).value1

    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }
}