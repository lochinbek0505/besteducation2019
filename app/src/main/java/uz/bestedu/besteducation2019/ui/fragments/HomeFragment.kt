package uz.bestedu.besteducation2019.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.bestedu.besteducation2019.R
import uz.bestedu.besteducation2019.adapters.HomeCoursesAdapter
import uz.bestedu.besteducation2019.adapters.HomeSubjectAdapter
import uz.bestedu.besteducation2019.databinding.FragmentHomeBinding
import uz.bestedu.besteducation2019.model.Course
import uz.bestedu.besteducation2019.model.Subject
import uz.bestedu.besteducation2019.model.buy_model
import uz.bestedu.besteducation2019.model.buy_response_model
import uz.bestedu.besteducation2019.model.course_model
import uz.bestedu.besteducation2019.model.order_model
import uz.bestedu.besteducation2019.model.order_response
import uz.bestedu.besteducation2019.model.subject_model
import uz.bestedu.besteducation2019.network.ApiService
import uz.bestedu.besteducation2019.network.RetrofitBuilder
import uz.bestedu.besteducation2019.ui.activitys.PayPageActivity
import uz.bestedu.besteducation2019.ui.activitys.ShowCourseActivity
import uz.bestedu.besteducation2019.ui.activitys.SignActivity
import uz.bestedu.besteducation2019.utilits.DatabaseHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var array_list: MutableList<Course>
    private lateinit var apiService: ApiService
    private lateinit var re_adapter: HomeCoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        array_list = emptyList<Course>().toMutableList()
//        re_adapter=HomeCoursesAdapter(array_list,object :HomeCoursesAdapter.ItemSetOnClickListener)
        dbHelper = DatabaseHelper(requireActivity())
//        val customDialog = CustomLottieDialog(requireActivity())
//        customDialog.show()
        val users = dbHelper.readData()

        for (user in users) {
            binding.tvHomeName.text = "Salom , ${user.firstName}"
            Glide.with(requireActivity()).load("https://bestedu.uz${user.image}")
                .into(binding.ivHomeImage)
        }

//        getUserData(users.get(0).id)
        val token = readFromSharedPreferences(requireActivity(), "TOKEN", "")

        Log.e("RESPONSSSSS", "  $token ")
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!array_list.isEmpty()) {

                    filter(newText)
                }
                return true
            }
        })

//        val searchAutoComplete = binding.searchView.findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
//
//        // Set the hint text color
//        searchAutoComplete.setHintTextColor(Color.GRAY)
        binding.ssPullRefresh.setOnRefreshListener {

            CoroutineScope(Dispatchers.Main).launch {


//
//                mAdapter.randomizeData()

                getDataFromNetwork(token.toString())

                getDataFromNetwork2(token)

            }

        }

        getDataFromNetwork(token.toString())

        getDataFromNetwork2(token)
        return root
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun pay(id: String) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(requireActivity(), "TOKEN", ""))

        lifecycleScope.launch {
            try {
                // Add debug logs to check the ids
//                Log.d(
//                    "ANLZYE4",
//                    "data.id1: ${data.id1}, data.id2: ${data.id2}, data.id3: ${data.id3}"
//                )

                val request = apiService.order(order_model(id))

                if (request.isSuccessful) {
                    val body = request.body() as? order_response
                    if (body!!.status == "success") {
                        Log.e("ANLZYE5", body.toString())

                        lifecycleScope.launch {

                            try {

                                val request2 =
                                    apiService.buy(buy_model(body.data.order_id.toString(), id))
                                if (request2.isSuccessful) {

                                    val body2 = request2.body() as? buy_response_model
                                    if (body2!!.status == "success") {
                                        Log.e("ANLZYE5", body2.toString())
                                        val intent = Intent(
                                            requireActivity(),
                                            PayPageActivity::class.java
                                        )
                                        intent.putExtra("URL", body2.data.link)
                                        startActivity(intent)
                                        requireActivity().finish()
                                    } else {
                                        Log.e("ANLZYE5", body2.errors.toString())
                                    }

                                }

                            } catch (e: Exception) {
                                Log.e("ANLZYE5", e.message.toString())

                            }

                        }

                    } else {
                        Toast.makeText(
                            requireActivity(),
                            body.errors.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("ANLZYE4", "Response body is null")
                    }
                } else {
                    Log.e("ANLZYE4", "Request not successful: ${request.code()}")
                }
            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())
                Toast.makeText(requireActivity(), e.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }


    }

    private fun filter(text: String?) {
        val filteredList = array_list.filter {
            it.name.contains(text ?: "", ignoreCase = true)
        }
        re_adapter.updateList(filteredList)
    }

    fun getDataFromNetwork(token: String) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e("RESPONSSSSS1", "  ${apiService.courses().isSuccessful} ")

                if (apiService.courses().isSuccessful) {

                    val body = apiService.courses().body() as course_model

                    adapter(body.data.courses.toMutableList())

                    Log.e("RESPONSSSSS", "  ${body} ")

                } else {
                    logout()
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

            dbHelper.deleteUser(user.id!!)
        }
        startActivity(Intent(requireActivity(), SignActivity::class.java))
        requireActivity().finish()
    }


    fun getDataFromNetwork3(token: String, id: String) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e("RESPONSSSSS", "  ${apiService.getSubjectCourse(id).isSuccessful} ")

                if (apiService.getSubjectCourse(id).isSuccessful) {

                    val body = apiService.getSubjectCourse(id).body() as course_model

                    adapter(body.data.courses.toMutableList())

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

    fun getDataFromNetwork2(token: String) {

        apiService =
            RetrofitBuilder.create(token)

        lifecycleScope.launch {

            try {
                Log.e("RESPONSSSSS", "  ${apiService.getSubjects().isSuccessful} ")

                if (apiService.getSubjects().isSuccessful) {

                    val body = apiService.getSubjects().body() as subject_model

                    adapter2(body, token)

                    Log.e("RESPONSSSSS", "  ${body} ")

                } else {
                    logout()

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

    fun adapter2(model: subject_model, token: String) {
        binding.ssPullRefresh.setRefreshing(false) // This stops refreshing

        var listt = model.data.subjects.toMutableList()
        binding.anim.visibility = View.GONE

        listt.add(0, Subject("Barchasi", -1))
        binding.rvCategory.adapter = HomeSubjectAdapter(
            requireActivity(),
            listt,
            object : HomeSubjectAdapter.ItemSetOnClickListener {
                override fun onClick(data: Subject) {
                    if (data.id == -1) {
                        getDataFromNetwork(token)
                    } else {
                        getDataFromNetwork3(token, data.id.toString())
                    }
                    println(data.id)
                }

            })

    }

    fun adapter(list: MutableList<Course>) {
        binding.anim.visibility = View.GONE
        array_list = list
        val controller =
            AnimationUtils.loadLayoutAnimation(requireActivity(), R.anim.layout_right_to_left)
        binding.rvCourses.layoutAnimation = controller
        re_adapter = HomeCoursesAdapter(
            requireActivity(),
            array_list, object : HomeCoursesAdapter.ItemSetOnClickListener {
                override fun onClick(data: Course) {
                    Log.e("ANLYZE", data.is_open.toString())

                    val intent = Intent(requireActivity(), ShowCourseActivity::class.java)
                    intent.putExtra("id_course", data.id.toString())
                    startActivity(intent)
                }
            },
            object : HomeCoursesAdapter.ItemSetOnClickListener2 {
                override fun onClick(id: String) {
                    pay(id)
                }

            })

        binding.rvCourses.adapter = re_adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        _binding!!.anim.visibility = View.GONE
    }
}