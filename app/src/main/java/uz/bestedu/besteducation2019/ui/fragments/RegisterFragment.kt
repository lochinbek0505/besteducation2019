package uz.bestedu.besteducation2019.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.bestedu.besteducation2019.R
import uz.bestedu.besteducation2019.databinding.FragmentRegisterBinding
import uz.bestedu.besteducation2019.model.register_model
import uz.bestedu.besteducation2019.model.register_respons
import uz.bestedu.besteducation2019.network.ApiClient
import uz.bestedu.besteducation2019.utilits.CustomLottieDialog

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var request: ApiClient
    private lateinit var dialog: CustomLottieDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        dialog = CustomLottieDialog(requireActivity())

        binding.tvRo.setOnClickListener {

            findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)


        }

        request = ApiClient

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {


            if (binding.etName.text.toString().isEmpty() && binding.etSurname.text.toString()
                    .isEmpty() && binding.etSurname.text.toString()
                    .isEmpty() && binding.etSurname.text.toString()
                    .isEmpty() && binding.etPas2.text.toString()
                    .isEmpty() && binding.etMiddleName.text.toString().isEmpty()
            ) {

                Toast.makeText(
                    requireActivity(),
                    "Iltimos hamma maydonlarni to'ldiring",
                    Toast.LENGTH_LONG
                ).show()


            } else {

                if (binding.etPas.text.toString() == binding.etPas2.text.toString()) {

                    getRegister(
                        register_model(
                            binding.etNumber.text.toString(),
                            binding.etName.text.toString(),
                            binding.etSurname.text.toString(),
                            binding.etMiddleName.text.toString(),
                            binding.etPas.text.toString(),
                        )
                    )

                } else {

                    Toast.makeText(
                        requireActivity(),
                        "Maxviy so'z mos emas iltimos qayta urinib ko'ring.",
                        Toast.LENGTH_LONG
                    ).show()


                }

            }

        }


    }

    fun getRegister(model: register_model) {
        binding.anim.visibility = View.VISIBLE
        dialog.show()
        request.apiService.register(model).enqueue(object : Callback<register_respons> {
            override fun onResponse(
                p0: Call<register_respons>,
                respons: Response<register_respons>
            ) {
                val respons_model = respons.body() as register_respons
                if (respons_model.status == "success") {
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
                    Toast.makeText(
                        requireActivity(),
                        respons_model.errors.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e("RESPONSSSSS", "  ${respons_model.errors} $p0")


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

            override fun onFailure(p0: Call<register_respons>, respons: Throwable) {
                println("RESPONSSSSS  ${respons.message} $p0")
                dialog.dismiss()
            }

        })


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {

            }
    }
}