package com.example.besteducation2019.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.besteducation2019.R
import com.example.besteducation2019.databinding.FragmentLoginBinding
import com.example.besteducation2019.model.login_model
import com.example.besteducation2019.model.login_respons
import com.example.besteducation2019.model.register_model
import com.example.besteducation2019.model.register_respons
import com.example.besteducation2019.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var request: ApiClient

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
        request = ApiClient

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {


            if (binding.etNumber.text.toString().isEmpty()  && binding.etPas.text.toString().isEmpty()) {

                Toast.makeText(
                    requireActivity(),
                    "Iltimos hamma maydonlarni to'ldiring",
                    Toast.LENGTH_LONG
                ).show()


            } else {



                    getRegister(
                        login_model(
                            binding.etNumber.text.toString(),
                            binding.etPas.text.toString()
                        )
                    )

            }

        }


    }

    fun getRegister(model: login_model) {

        request.apiService.login(model).enqueue(object : Callback<login_respons> {
            override fun onResponse(
                p0: Call<login_respons>,
                respons: Response<login_respons>
            ) {
                val respons_model = respons.body() as login_respons
                if(respons_model.status.isNullOrEmpty()){

                    Toast.makeText(
                        requireActivity(),
                        respons_model.token,
                        Toast.LENGTH_LONG
                    ).show()


                }
                else{


                    Toast.makeText(
                        requireActivity(),
                        respons_model.message,
                        Toast.LENGTH_LONG
                    ).show()

                }

            }

            override fun onFailure(p0: Call<login_respons>, respons: Throwable) {
                println("RESPONSSSSS  ${respons.message} $p0")
            }
        })


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {

            }
    }
}