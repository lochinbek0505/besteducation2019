package com.example.besteducation2019.ui.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.besteducation2019.databinding.ActivityTestResultBinding
import com.example.besteducation2019.model.test_result

class TestResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getSerializableExtra("TRA") as test_result
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        binding.tvTestName.text = data.title
        binding.tvCorrect.text = "${data.correct} ta"
        binding.tvIncorrect.text = "${data.lenght - data.correct} ta"
        binding.tvProcent.text = "${(data.correct / data.lenght * 100).toInt()} %"

        binding.btnEnd.setOnClickListener {

            finish()

        }
    }
}