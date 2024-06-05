package com.example.besteducation2019.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.besteducation2019.databinding.FragmentProfilBinding
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import com.example.besteducation2019.ui.activitys.SignActivity
import com.example.besteducation2019.utilits.DatabaseHelper
import kotlinx.coroutines.launch

class ProfilFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var dbHelper: DatabaseHelper
    private var _binding: FragmentProfilBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root
        dbHelper = DatabaseHelper(requireActivity())

        _binding!!.rlDelete.setOnClickListener {

            end()
        }
        _binding!!.rlLogout.setOnClickListener {

            logout()

        }
        return root
    }

    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun logout() {
        saveToSharedPreferences(requireActivity(), "TOKEN", "")
        val users = dbHelper.readData()
        for (user in users) {

            dbHelper.deleteUser(user.id)
        }
        startActivity(Intent(requireActivity(), SignActivity::class.java))
        requireActivity().finish()
    }

    fun end() {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(requireActivity(), "TOKEN", ""))

        lifecycleScope.launch {

            try {

                val request = apiService.logout()
                println(request.body())
                Log.e("ANLZYE4", request.toString())

                if (request.isSuccessful) {
                    saveToSharedPreferences(requireActivity(), "TOKEN", "")
                    val users = dbHelper.readData()
                    for (user in users) {

                        dbHelper.deleteUser(user.id)
                    }
                    startActivity(Intent(requireActivity(), SignActivity::class.java))
                    requireActivity().finish()
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilFragment().apply {

            }
    }
}