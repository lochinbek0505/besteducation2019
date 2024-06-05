package com.example.besteducation2019.ui.activitys

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.besteducation2019.R
import com.example.besteducation2019.databinding.ActivityTestBinding
import com.example.besteducation2019.model.Answer
import com.example.besteducation2019.model.Question
import com.example.besteducation2019.model.Quiz
import com.example.besteducation2019.model.lesson_datailes
import com.example.besteducation2019.model.lesson_id_model
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import kotlinx.coroutines.launch

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    var response = Answer("", ",", false)
    var res_list = ArrayList<Answer>()
    private lateinit var dialog: AlertDialog
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.sekk.max = 100

        val data = intent.getSerializableExtra("LESSON") as lesson_id_model

        network(data)
        Log.e("QUIZZ1", data.toString())


        binding.sekk.setOnTouchListener { _, _ ->
            true
        }
    }

    fun display(model: Quiz) {
        var index = 0
        var foiz = 0
        val tittle = model.name
        val prs = 100 / model.questions.size

        val tests = model.questions
        Log.e("QUIZZ1", model.toString())
        binding.tvTittle.text = tittle
        giveQuestion(tests, 0)
        Log.e("AANNMM", tests.toString())
        binding.btnSubmit.setOnClickListener {

            if (response.value1 != "") {
                if (tests.size - 1 > index) {
                    index++
                    res_list.add(response)
                    response = Answer("", "", false)
                    giveQuestion(tests, index)
                    foiz += prs * 1
                    binding.sekk.progress = foiz
                    println("Javob : $response")
                } else {
                    foiz += prs * 1
                    binding.sekk.progress = foiz
                    res_list.add(response)
                    response = Answer("", "", false)

                    check(res_list, tittle)
                    println("Finished")

                }
            } else {


                Toast.makeText(this, "Iltimos birorta javobni tanlang", Toast.LENGTH_LONG).show()

            }
        }
        binding.cvA.setOnClickListener {
//            response = "a"
            var aaa = model.questions.get(index).json.answers
            binding.cvA.setBackgroundResource(R.drawable.selected_bg)
            binding.cvB.setBackgroundResource(R.drawable.defould_bg)
            binding.cvC.setBackgroundResource(R.drawable.defould_bg)
            binding.cvD.setBackgroundResource(R.drawable.defould_bg)
            response = Answer(aaa.get(0).value1, "", aaa.get(0).isCorrect)
//            Toast.makeText(this, "Iltimos birorta javobni tanlang", Toast.LENGTH_LONG).show()

        }
        binding.cvB.setOnClickListener {
//            response = "b"
            var aaa = model.questions.get(index).json.answers

            binding.cvA.setBackgroundResource(R.drawable.defould_bg)
            binding.cvB.setBackgroundResource(R.drawable.selected_bg)
            binding.cvC.setBackgroundResource(R.drawable.defould_bg)
            binding.cvD.setBackgroundResource(R.drawable.defould_bg)
            response = Answer(aaa.get(1).value1, "", aaa.get(1).isCorrect)

        }
        binding.cvC.setOnClickListener {
//            response = "c"
            var aaa = model.questions.get(index).json.answers

            binding.cvA.setBackgroundResource(R.drawable.defould_bg)
            binding.cvB.setBackgroundResource(R.drawable.defould_bg)
            binding.cvC.setBackgroundResource(R.drawable.selected_bg)
            binding.cvD.setBackgroundResource(R.drawable.defould_bg)
            response = Answer(aaa.get(2).value1, "", aaa.get(2).isCorrect)

        }
        binding.cvD.setOnClickListener {
//            response = "d"
            var aaa = model.questions.get(index).json.answers

            binding.cvA.setBackgroundResource(R.drawable.defould_bg)
            binding.cvB.setBackgroundResource(R.drawable.defould_bg)
            binding.cvC.setBackgroundResource(R.drawable.defould_bg)
            binding.cvD.setBackgroundResource(R.drawable.selected_bg)
            response = Answer(aaa.get(3).value1, "", aaa.get(3).isCorrect)

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

    fun network(data: lesson_id_model) {


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
                            display(quiz)
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
                Toast.makeText(this@TestActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun check(response: ArrayList<Answer>, title: String) {

        var ball = 0

        var index = 0
        for (question in response) {
            if (question.isCorrect) {

                ball++

            }
            index++
        }


        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.custom_dialog_layout, null)

        builder.setView(dialogView)
        dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()

        var buttonClose = dialog.findViewById<Button>(R.id.buttonClose)
        dialog.findViewById<TextView>(R.id.textView2)?.text = "To'g'ri javoblar : $ball"
        dialog.findViewById<TextView>(R.id.textView3)?.text = "Noto'g'ri javoblar : ${index - ball}"
        dialog.findViewById<TextView>(R.id.textView1)?.text = title

        buttonClose!!.setOnClickListener {
            dialog.dismiss()
            finish()
        }


    }
}