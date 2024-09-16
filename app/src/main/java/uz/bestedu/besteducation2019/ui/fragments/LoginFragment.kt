package uz.bestedu.besteducation2019.ui.fragments

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
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.bestedu.besteducation2019.databinding.FragmentLoginBinding
import uz.bestedu.besteducation2019.model.User
import uz.bestedu.besteducation2019.model.login_model
import uz.bestedu.besteducation2019.model.login_response
import uz.bestedu.besteducation2019.model.profil_detailes
import uz.bestedu.besteducation2019.network.ApiClient
import uz.bestedu.besteducation2019.network.ApiService
import uz.bestedu.besteducation2019.network.RetrofitBuilder
import uz.bestedu.besteducation2019.ui.activitys.HomeActivity
import uz.bestedu.besteducation2019.utilits.CustomLottieDialog
import uz.bestedu.besteducation2019.utilits.DatabaseHelper


class LoginFragment : Fragment() {
    private lateinit var request: ApiClient
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var dialog: CustomLottieDialog
    private lateinit var binding: FragmentLoginBinding
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        dialog = CustomLottieDialog(requireActivity())
        binding.tvRo.setOnClickListener {

            findNavController().navigateUp()

        }

        val token = readFromSharedPreferences(requireActivity(), "TOKEN", "")

        if (!token.isEmpty()) {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
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
        binding.anim.visibility = View.VISIBLE

        // Example of adding a user
        dialog.show()
        request.apiService.login(model).enqueue(object : Callback<login_response> {
            override fun onResponse(
                p0: Call<login_response>,
                respons: Response<login_response>
            ) {
                val respons_model = respons.body() as login_response
//                Toast.makeText(
//                    requireActivity(),
//                    "$respons_model",
//                    Toast.LENGTH_LONG
//                ).show()

                if (respons_model.status == "success") {


                    getUserData(respons_model.data.token, respons_model.data.id)

//                    if (respons_model.data.image.isNullOrEmpty()) {
//                        val newUser = Author(
//                            respons_model.data.id,
//                            binding.etNumber.text.toString(),
//                            respons_model.data.firstName,
//                            respons_model.data.lastName,
//                            "",
//                            ""
//                        )
//                        dbHelper.addUser(newUser)
//
//                    } else {
//                        val newUser = Author(
//                            respons_model.data.id,
//                            binding.etNumber.text.toString(),
//                            respons_model.data.firstName,
//                            respons_model.data.lastName,
//                            "",
//                            respons_model.data.image
//                        )
//                        dbHelper.addUser(newUser)
//
//                    }
//                    dialog.dismiss()
//
//                    Toast.makeText(
//                        requireActivity(),
//                        respons_model.data.token,
//                        Toast.LENGTH_LONG
//                    ).show()
                    Log.e("show_data", "  ${respons_model} $p0")

                } else {
                    dialog.dismiss()


                    Toast.makeText(
                        requireActivity(),
                        respons_model.errors.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("RESPONSSSSS", "  ${respons_model.errors} $p0")

                }

            }

            override fun onFailure(p0: Call<login_response>, respons: Throwable) {
                println("RESPONSSSSS  ${respons.message} $p0")
                dialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    respons.message,
                    Toast.LENGTH_LONG
                ).show()
                Log.e("RESPONSSSSS", "  ${respons.message} $p0")

            }
        })


    }

    private fun getUserData(token: String, id: Int) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e(
                    "show_data",
                    "  ${apiService.profilInformation(id.toString())} "
                )

                if (apiService.profilInformation(id.toString()).isSuccessful) {

                    val body = apiService.profilInformation(id.toString()).body() as profil_detailes
                    dialog.dismiss()
                    val user_author = body.data!!.user
                    val newUser = User(
                        user_author!!.id!!.toInt(),
                        user_author.username!!,
                        user_author.firstName!!,
                        user_author.lastName.toString(),
                        user_author.middleName.toString(),
                        user_author.bio.toString(),
                        user_author.isStudent.toString()
                    )
                    dbHelper.addUser(user_author)
                    saveToSharedPreferences(requireActivity(), "TOKEN", token)

                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()


                    Log.e("show_data", "  ${body} ")

                } else {
                    dialog.dismiss()
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                dialog.dismiss()

                Log.e("show_data", "  ${e.message} ")

            }


        }

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