package uz.bestedu.besteducation2019.ui.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.bestedu.besteducation2019.R
import uz.bestedu.besteducation2019.databinding.ActivityTestBinding
import uz.bestedu.besteducation2019.model.Answer
import uz.bestedu.besteducation2019.model.Question
import uz.bestedu.besteducation2019.model.Quiz
import uz.bestedu.besteducation2019.model.lesson_datailes
import uz.bestedu.besteducation2019.model.lesson_id_model
import uz.bestedu.besteducation2019.model.rate_request
import uz.bestedu.besteducation2019.model.request_end
import uz.bestedu.besteducation2019.model.test_result
import uz.bestedu.besteducation2019.model.test_transfer_model
import uz.bestedu.besteducation2019.network.ApiService
import uz.bestedu.besteducation2019.network.RetrofitBuilder

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    var response = Answer("", ",", false)
    var res_list = ArrayList<Answer>()
    private lateinit var dialog: AlertDialog
    private lateinit var apiService: ApiService
    lateinit var data2: lesson_id_model
    var index = 0
    var foiz = 0
    var ball = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.sekk.max = 100
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        val data = intent.getSerializableExtra("transfer_test") as test_transfer_model

        data2 = intent.getSerializableExtra("lesson_id") as lesson_id_model

        foiz = data.foiz
        ball = data.ball
        index = data.index

//        network(data)
        Log.e("QUIZZ1", data.toString())

        display(data)
        binding.sekk.setOnTouchListener { _, _ ->
            true
        }
    }

    fun display(data: test_transfer_model) {

        val model = data.quiz


        val tittle = model.name
        val prs = 100 / model.questions.size

        val tests = model.questions
        Log.e("QUIZZ1", model.toString())
        binding.tvTittle.text = tittle
        giveQuestion(tests, index)
        binding.tvIndicate.text = "${index + 1}/${tests.size}"
        Log.e("AANNMM", tests.toString())
        binding.btnSubmit.setOnClickListener {

            if (response.value1 != "") {
                if (tests.size - 1 > index) {
                    index++
//                    res_list.add(response)
                    if (response.isCorrect) {
                        ball++
                    }

                    response = Answer("", "", false)
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
                            intent.putExtra("lesson_id", data2)

                            intent.putExtra("transfer_test", model)
                            startActivity(intent)
                            finish()

                        }

                        "writable" -> {
                            val model = test_transfer_model(model, ball, foiz, index)

                            val intent = Intent(this, WriteableTestActivity::class.java)

                            intent.putExtra("lesson_id", data2)
                            intent.putExtra("transfer_test", model)
                            startActivity(intent)
                            finish()

                        }


                    }

                    println("Javob : $response")
                } else {
                    if (response.isCorrect) {
                        ball++
                    }
                    foiz += prs * 1
                    binding.sekk.progress = foiz
                    res_list.add(response)
                    response = Answer("", "", false)
                    check(model, tittle)
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
//                            display(quiz)
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

                Toast.makeText(this@TestActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    val intent = Intent(this@TestActivity, TestResultActivity::class.java)
                    intent.putExtra("TRA", test_result(title, ball, response.questions.size))
                    startActivity(intent)
                    finish()
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@TestActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}