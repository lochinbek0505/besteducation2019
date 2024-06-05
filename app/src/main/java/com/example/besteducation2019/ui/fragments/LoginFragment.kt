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
import androidx.navigation.fragment.findNavController
import com.example.besteducation2019.databinding.FragmentLoginBinding
import com.example.besteducation2019.model.Author
import com.example.besteducation2019.model.login_model
import com.example.besteducation2019.model.login_response
import com.example.besteducation2019.network.ApiClient
import com.example.besteducation2019.ui.activitys.HomeActivity
import com.example.besteducation2019.utilits.DatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var request: ApiClient
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginBinding.inflate(inflater,container,false)

        binding.tvRo.setOnClickListener {

            findNavController().navigateUp()

        }

        val token = readFromSharedPreferences(requireActivity(), "TOKEN", "")

        if (!token.isEmpty()){
            startActivity(Intent(requireActivity(),HomeActivity::class.java))
            requireActivity().finish()
        }

        request = ApiClient

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {


            val check = binding.etNumber.text!!.length > 6 && binding.etPas.text!!.length > 1
            if (check) {
//                Toast.makeText(
//                    requireActivity(),
//                    "ISHLADI",
//                    Toast.LENGTH_LONG
//                ).show()
                getRegister(
                    login_model(
                        binding.etNumber.text.toString(),
                        binding.etPas.text.toString()
                    )
                )
            } else {

                Toast.makeText(
                    requireActivity(),
                    "Iltimos hamma maydonlarni tog'ri to'ldiring",
                    Toast.LENGTH_LONG
                ).show()


            }

        }


    }

    fun getRegister(model: login_model) {
        dbHelper = DatabaseHelper(requireActivity())

        // Example of adding a user

        request.apiService.login(model).enqueue(object : Callback<login_response> {
            override fun onResponse(
                p0: Call<login_response>,
                respons: Response<login_response>
            ) {
                val respons_model = respons.body() as login_response
                Toast.makeText(
                    requireActivity(),
                    "$respons_model",
                    Toast.LENGTH_LONG
                ).show()

                if (respons_model.status == "success") {

                    saveToSharedPreferences(requireActivity(),"TOKEN",respons_model.data.token)
                    if (respons_model.data.image.isNullOrEmpty()){
                        val newUser = Author(respons_model.data.id, "", respons_model.data.firstName, respons_model.data.lastName, "", "")
                        dbHelper.addUser(newUser)

                    }else{
                        val newUser = Author(respons_model.data.id, "", respons_model.data.firstName, respons_model.data.lastName, "", respons_model.data.image)
                        dbHelper.addUser(newUser)

                    }
                    startActivity(Intent(requireActivity(),HomeActivity::class.java))
                    requireActivity().finish()
                    Toast.makeText(
                        requireActivity(),
                        respons_model.data.token,
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("RESPONSSSSS","  ${respons_model.errors} $p0")

                }
                else{


                    Toast.makeText(
                        requireActivity(),
                        respons_model.errors.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("RESPONSSSSS","  ${respons_model.errors} $p0")

                }

            }

            override fun onFailure(p0: Call<login_response>, respons: Throwable) {
                println("RESPONSSSSS  ${respons.message} $p0")
                Toast.makeText(
                    requireActivity(),
                    respons.message,
                    Toast.LENGTH_LONG
                ).show()
                Log.e("RESPONSSSSS","  ${respons.message} $p0")

            }
        })


    }
    // Save data to Shared Preferences
    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }
    // Read data from Shared Preferences
    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {

            }
    }
}