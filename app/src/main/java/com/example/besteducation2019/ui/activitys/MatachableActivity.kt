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
import com.example.besteducation2019.adapters.MatachableAdapter
import com.example.besteducation2019.adapters.MatachableAdapter2
import com.example.besteducation2019.databinding.ActivityMatachableBinding
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

class MatachableActivity : AppCompatActivity() {

    lateinit var adapter: MatachableAdapter
    lateinit var adapter2: MatachableAdapter2
    lateinit var binding: ActivityMatachableBinding
    var count = 0
    var response = Answer("", ",", false)
    var res_list = ArrayList<Answer>()
    private lateinit var dialog: AlertDialog
    private lateinit var apiService: ApiService
    var index = 0
    var foiz = 0
    var ball = 0
    var list = arrayListOf("", "")
    var jav1 = arrayListOf<String>()
    var jav2 = arrayListOf<String>()
    var size = 0
    var list1 = arrayListOf<String>()
    var list2 = arrayListOf<String>()
    lateinit var data2: lesson_id_model
    var list11 = arrayListOf<String>()
    var list22 = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatachableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sekk.max = 100

        val data = intent.getSerializableExtra("transfer_test") as test_transfer_model
        foiz = data.foiz
        ball = data.ball
        index = data.index
        Log.e("QUIZZ1", data.toString())
        binding.sekk.progress = foiz
        data2 = intent.getSerializableExtra("lesson_id") as lesson_id_model
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        display(data)

//        binding.btnSubmit.setOnClickListener {
//
//            Log.e("CHACK", "${jav1.toString()} ${jav2.toString()}")
//
//        }


    }

    fun giveQuestion(tests: List<Question>, index: Int) {

        var rv_bool_1 = false
        var rv_bool_2 = false
        binding.tvQuestion.text = tests.get(index).json.question
        size = tests.get(index).json.answers.size
        tests.get(index).json.answers.forEach {

            list1.add(it.value1)
            list2.add(it.value2)

            list11.add(it.value1)
            list22.add(it.value2)
        }

        list1.shuffle()
        list2.shuffle()

        adapter = MatachableAdapter(this, list1, object : MatachableAdapter.ItemSetOnClickListener {
            override fun onClick(data: String, previousSelectedPosition: Int) {
                count++
                list[0] = data
                Log.e("MATCH_TEST", "ADAPTER2- ${list[1]}")
                Log.e("MATCH_TEST", "ADAPTER1- ${list[0]}")
                rv_bool_1 = true
//                if (binding.rvMat2.isEnabled) {

                if (rv_bool_2) {
                    rv_bool_1 = false
                    rv_bool_2 =
                        false//                        Log.e("MATCH_TEST", "ADAPTER1- ${ binding.rvMat2.isEnabled }")
                    list1.remove(list.get(0))
                    list2.remove(list.get(1))
                    jav1.add(list.get(0))
                    jav2.add(list.get(1))

                    adapter.resetAllItemBackgrounds()
                    adapter.notifyDataSetChanged()
                    adapter2.notifyDataSetChanged()


//                    adapter.notifyItemRangeChanged(0, list1.size - 1)
//                    adapter2.notifyItemRangeChanged(0, list2.size - 1)
                    adapter.resetAllItemBackgrounds()
                    adapter2.resetAllItemBackgrounds()
                    count = 0
//                    adapter.notifyItemChanged(previousSelectedPosition)
//                    }
                }
            }

        })
        adapter2 =
            MatachableAdapter2(this, list2, object : MatachableAdapter2.ItemSetOnClickListener {
                override fun onClick(data: String, previousSelectedPosition: Int) {

                    list[1] = data
                    count++
//                    binding.rvMat1.setOnTouchListener { _, _ ->
//                        false
//                    }
//                    binding.rvMat2.setOnTouchListener { _, _ ->
//                        true
//                    }
                    Log.e("MATCH_TEST", "ADAPTER2- ${list[1]}")
                    Log.e("MATCH_TEST", "ADAPTER1- ${list[0]}")

                    rv_bool_2 = true
                    if (rv_bool_1) {
                        rv_bool_1 = false
                        rv_bool_2 = false
                        list1.remove(list.get(0))
                        list2.remove(list.get(1))
                        jav1.add(list.get(0))
                        jav2.add(list.get(1))
                        adapter.notifyDataSetChanged()
                        adapter2.notifyDataSetChanged()
//                        adapter.notifyItemRangeChanged(0, list1.size - 1)
//                        adapter2.notifyItemRangeChanged(0,list2.size-1)
                        adapter.resetAllItemBackgrounds()
                        adapter2.resetAllItemBackgrounds()
//                        adapter2.notifyItemChanged(previousSelectedPosition)
//                        adapter2.notifyItemRangeChanged(0, list2.size - 1)
                        count = 0
                    }
                }


            })
        binding.rvMat1.adapter = adapter
        binding.rvMat2.adapter = adapter2


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

//        binding.rvMat2.setOnTouchListener { _, _ ->
//            true
//        }

        binding.btnSubmit.setOnClickListener {
            if (jav1.size == tests.get(index).json.answers.size && jav2.size == tests.get(index).json.answers.size) {

//            if (response.get(0).value1 != "") {
                if (tests.size - 1 > index) {
                    index++
////                    res_list.add(response)

                    Log.e("ORIGIN_TEST_TEST", "${jav1} \n ${jav2} ")
                    Log.e("ORIGIN_TEST_TEST", "\n\n${list11} \n ${list22} ")


                    var chh = true
                    jav1.forEachIndexed { index, s ->

                        Log.e(
                            "ORIGIN_TEST_TEST",
                            "${jav2.get(index)} ${list22.get(list11.indexOf(s))}"
                        )

                        if (jav2.get(index) != list22.get(list11.indexOf(s))) {

                            chh = false
//                            Log.e("ORIGIN_TEST_TEST", jav2.get(index).toString())

                            Log.e("AANNMM", "$s ${jav2.get(index)}")

                        }


                    }
                    Log.e("AANNMM", chh.toString())

                    if (chh) {
                        ball++
                    }
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
                            giveQuestion(tests, index)


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
                    var chh = true
                    jav1.forEachIndexed { index, s ->

                        if (jav2.get(index) != list22.get(list11.indexOf(s))) {

                            chh = false
                            Log.e("ORIGIN_TEST_TEST", jav2.get(index).toString())

                            Log.e("AANNMM", "$s ${jav2.get(index)}")

                        }


                    }
                    Log.e("ORIGIN_TEST_TEST", jav2.toString())

                    Log.e("AANNMM", chh.toString())

                    if (chh) {
                        ball++
                    }
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

                Toast.makeText(this@MatachableActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    val intent = Intent(this@MatachableActivity, TestResultActivity::class.java)
                    intent.putExtra("TRA", test_result(title, ball, response.questions.size))
                    startActivity(intent)
                    finish()
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@MatachableActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }
}

