package uz.bestedu.besteducation2019.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.bestedu.besteducation2019.R
import uz.bestedu.besteducation2019.adapters.CoursesAdapter
import uz.bestedu.besteducation2019.databinding.FragmentDashboardBinding
import uz.bestedu.besteducation2019.model.Coursesa
import uz.bestedu.besteducation2019.model.Mycourse
import uz.bestedu.besteducation2019.network.ApiService
import uz.bestedu.besteducation2019.network.RetrofitBuilder
import uz.bestedu.besteducation2019.ui.activitys.LessonsLActivity

class CoursesFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var apiService: ApiService

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getDataFromNetwork2(readFromSharedPreferences(requireContext(), "TOKEN", ""))

        return root
    }

    override fun onResume() {
        super.onResume()
        val id = requireActivity().intent.getStringExtra("id_course")

        getDataFromNetwork2(readFromSharedPreferences(requireContext(), "TOKEN", ""))
    }

    fun getDataFromNetwork2(token: String) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e("RESPONSSSSS", "  ${apiService.myCourses().isSuccessful} ")

                if (apiService.myCourses().isSuccessful) {

                    val body = apiService.myCourses().body() as Mycourse

                    adapter(body.data!!.courses)
//                    adapter2(body, token)

                    Log.e("RESPONSSSSS", "  ${body} ")

                }

            } catch (e: Exception) {
                Log.e("RESPONSSSSS", "  ${e.message} ")

            }


        }


//        apiService.courses().enqueue(object : Callback<courses_model>{
//            override fun onResponse(p0: Call<courses_model>, respons: Response<courses_model>) {
//                val data = respons.body()
//
//            }
//
//            override fun onFailure(p0: Call<courses_model>, p1: Throwable) {
//            }
//
//        })


    }

    fun adapter(data: ArrayList<Coursesa>) {
        binding.anim.visibility = View.GONE
        val controller =
            AnimationUtils.loadLayoutAnimation(requireActivity(), R.anim.layout_right_to_left)
        binding.rvFd.layoutAnimation = controller

        val adapter =
            CoursesAdapter(requireActivity(), data, object : CoursesAdapter.ItemSetOnClickListener {
                override fun onClick(data: Coursesa) {

                    val intent = Intent(requireActivity(), LessonsLActivity::class.java)
                    intent.putExtra("id_course", data.id.toString())
                    startActivity(intent)

                }

            })

        binding.rvFd.adapter = adapter
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}