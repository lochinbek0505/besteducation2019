package uz.bestedu.besteducation2019.ui.activitys

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.bestedu.besteducation2019.adapters.BillingAdapter
import uz.bestedu.besteducation2019.databinding.ActivityBillingBinding
import uz.bestedu.besteducation2019.model.Data100
import uz.bestedu.besteducation2019.model.billing_model
import uz.bestedu.besteducation2019.network.ApiService
import uz.bestedu.besteducation2019.network.RetrofitBuilder

class BIllingActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    private lateinit var binding: ActivityBillingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        report()

    }

    fun report() {

        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this, "TOKEN", ""))

        lifecycleScope.launch {

            try {

                val request = apiService.getBillingReport()
                println(request.body())
                Log.e("ANLZYE4", request.toString())

                if (request.isSuccessful) {

                    val body = request.body() as billing_model

                    val list = arrayListOf<Data100>()

                    body.data.forEach {
                        if (it.status != "0") {
                            list.add(it)
                        }
                    }

                    val adapter = BillingAdapter(list)
                    binding.rvBilling.adapter = adapter
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@BIllingActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

//                        val adapter = BillingAdapter(body.data)
//                        binding.rvBilling.adapter = adapter
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }


}