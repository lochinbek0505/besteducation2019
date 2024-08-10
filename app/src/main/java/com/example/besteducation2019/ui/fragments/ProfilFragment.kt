package com.example.besteducation2019.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.besteducation2019.databinding.ChangeDialogBinding
import com.example.besteducation2019.databinding.EditDialogBinding
import com.example.besteducation2019.databinding.FragmentProfilBinding
import com.example.besteducation2019.model.User
import com.example.besteducation2019.model.change_password_model
import com.example.besteducation2019.model.upload_image_data
import com.example.besteducation2019.network.ApiService
import com.example.besteducation2019.network.RetrofitBuilder
import com.example.besteducation2019.ui.activitys.BIllingActivity
import com.example.besteducation2019.ui.activitys.SignActivity
import com.example.besteducation2019.utilits.CustomLottieDialog
import com.example.besteducation2019.utilits.DatabaseHelper
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class ProfilFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var dbHelper: DatabaseHelper
    private var _binding: FragmentProfilBinding? = null
    private val IMAGE_PICK_CODE = 1000
    private var pickedImageUri: Uri? = null
    lateinit var data: User
    private lateinit var alertDialog2: AlertDialog
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 101
    lateinit var dialog: CustomLottieDialog
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
        data = dbHelper.readData().get(0)
        Glide.with(requireActivity()).load(dbHelper.readData().get(0).image)
            .into(_binding!!.ivProfil)
        _binding!!.ivProfil.setOnClickListener {
//            pickImageFromGallery()

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                openGallery()
            }

        }

        _binding!!.btnPrivate.setOnClickListener {

            passwordDialog()

        }

        _binding!!.btnAdmin.setOnClickListener {

            val group_telegram = Intent (ACTION_VIEW, Uri.parse("https://t.me/JamshidOtaqulov"))
            startActivity(group_telegram)

        }

        dialog = CustomLottieDialog(requireActivity())

        _binding!!.btnFinancial.setOnClickListener {

            startActivity(Intent(requireActivity(), BIllingActivity::class.java))

        }

        data = dbHelper.readData().get(0)
        show(data)
        _binding!!.tvProfilName.text = "${data.firstName} ${data.lastName}"
        _binding!!.rlLogout.setOnClickListener {

            end()

        }
        _binding!!.btnEdit.setOnClickListener {

            showDialog(data)
            Log.w("ANLZYE_TIP", data.toString())

        }

        return root
    }

    fun show(data: User) {
        _binding!!.tvProfilName.text = "${data.firstName} ${data.lastName}"


        Glide.with(requireActivity()).load("https://bestedu.uz${data.image}")
            .into(_binding!!.ivProfil)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            uploadImage(requireContext(), selectedImageUri!!)
        }
    }

    private fun getFileFromUri(context: Context, fileUri: Uri): File {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(fileUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return File(filePath!!)
    }

    private fun prepareFilePart(context: Context, fileUri: Uri): MultipartBody.Part {
        val file = getFileFromUri(context, fileUri)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun uploadImage(context: Context, fileUri: Uri) {
        val service =
            RetrofitBuilder.create(readFromSharedPreferences(requireActivity(), "TOKEN", ""))

        val imagePart = prepareFilePart(context, fileUri)

        val call = service.uploadImage(imagePart)
        call.enqueue(object : Callback<upload_image_data> {
            override fun onResponse(
                call: Call<upload_image_data>,
                response: Response<upload_image_data>
            ) {
                if (response.isSuccessful) {
                    // Handle success
                    Log.e("ANLZYE_TIP45", response.body()?.data.toString())
                    val body = response.body()?.data?.image
                    dbHelper.deleteUser(data.id!!)
                    dbHelper.addUser(
                        User(
                            data.id,
                            body?.username,
                            body?.first_name,
                            body?.last_name,
                            body?.middle_name,
                            body?.bio,
                            body?.image,
                            body?.is_student.toString()
                        )
                    )
                    data = User(
                        data.id,
                        body?.username,
                        body?.first_name,
                        body?.last_name,
                        body?.middle_name,
                        body?.bio,
                        body?.image,
                        body?.is_student.toString()
                    )

                    show(data)

                } else {
                    // Handle failure
                    Log.e("ANLZYE_TIP45", response.body().toString())

                }
            }

            override fun onFailure(call: Call<upload_image_data>, t: Throwable) {
                // Handle error
            }
        })
    }


    fun passwordDialog() {

        val dialogBinding = ChangeDialogBinding.inflate(layoutInflater)

        alertDialog2 = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialogBinding.btnCancel.setOnClickListener {

            alertDialog2.dismiss()

        }
        dialogBinding.btnEdit.setOnClickListener {

            Log.e("ANLZYE_TIP", "${dialogBinding.etNewPw}")


            change_request(
                change_password_model(
                    dialogBinding.etOldPw.text.toString(),
                    dialogBinding.etNewPw.text.toString()
                )
            )

        }
        alertDialog2.show()


    }

    fun change_request(data: change_password_model) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(requireActivity(), "TOKEN", ""))

        lifecycleScope.launch {

            try {

                val request = apiService.change_password(data)
                println(request.body())
                Log.e("ANLZYE4", request.toString())

                if (request.isSuccessful) {
                    if (request.body()?.status == "success") {
                        Toast.makeText(
                            requireActivity(),
                            "Parol muvaffaqiyatli o'zgartirildi",
                            Toast.LENGTH_SHORT
                        ).show()

                        alertDialog2.dismiss()

                    } else {
                        Toast.makeText(
                            requireActivity(),
                            request.body()?.errors.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun showDialog(data: User) {

        val dialogBinding = EditDialogBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialogBinding.etUsername.setText(data.username)
        dialogBinding.etName.setText(data.firstName)
        dialogBinding.etMiddleName.setText(data.middleName)
        dialogBinding.etLastName.setText(data.lastName)
        dialogBinding.etBio.setText(data.bio)
        dialogBinding.isTeacher.isChecked = data.isStudent.toBoolean()
        Log.e("ANLZYE_TIP", "${data.isStudent.toBoolean()}")
        val is_student = dialogBinding.isTeacher.isChecked
        dialogBinding.btnCancel.setOnClickListener {

            alertDialog.dismiss()

        }
        dialogBinding.btnEdit.setOnClickListener {

            Log.e("ANLZYE_TIP", "${dialogBinding.isTeacher.isChecked}")


            uploadUser(
                dialogBinding.etUsername.text.toString(),
                dialogBinding.etName.text.toString(),
                dialogBinding.etLastName.text.toString(),
                dialogBinding.etMiddleName.text.toString(),
                dialogBinding.etBio.text.toString(),
                dialogBinding.isTeacher.isChecked
            )
            alertDialog.dismiss()

        }
        alertDialog.show()


    }

    private fun uploadUser(
        username: String,
        firstName: String,
        lastName: String,
        middleName: String,
        bio: String,
        isStudent: Boolean
    ) {

        dialog.show()
        lifecycleScope.launch {
            uploadUserData(
                username = username,
                first_name = firstName,
                last_name = lastName,
                middle_name = middleName,
                bio = bio,
                imageUri = pickedImageUri,
                is_student = isStudent
            )
        }
    }

    private suspend fun uploadUserData(
        username: String,
        first_name: String,
        last_name: String,
        middle_name: String?,
        bio: String,
        imageUri: Uri?,
        is_student: Boolean
    ) {

        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(requireActivity(), "TOKEN", ""))

        val usernamePart = createPartFromString(username)
        val firstNamePart = createPartFromString(first_name)
        val lastNamePart = createPartFromString(last_name)
        val middleNamePart = middle_name?.let { createPartFromString(it) }
        val bioPart = createPartFromString(bio)
        val isStudentPart = createPartFromString(is_student.toString())

        val imagePart = imageUri?.let {
            val byteArray = getBytesFromUri(it)
            val requestFile =
                byteArray.toRequestBody("application/octet-stream".toMediaTypeOrNull())
            requestFile
        }

        val response = apiService.createUser(
            data.id.toString(),
            usernamePart,
            firstNamePart,
            lastNamePart,
            middleNamePart,
            bioPart,
            isStudentPart
        )

        if (response.isSuccessful) {
            Log.e("ANLZYE_TIP", response.body().toString())
//            dbHelper.updateUser(
//                User(
//                    data.id, username.toString(), first_name, last_name,
//                    middle_name.toString(), data.image
//                )
//            )
//            _binding!!.tvProfilName.text = "${first_name} ${last_name}"

            dialog.dismiss()
            dbHelper.deleteUser(data.id!!)
            dbHelper.addUser(
                User(
                    data.id,
                    username,
                    first_name,
                    last_name,
                    middle_name,
                    bio,
                    data.image,
                    is_student.toString()
                )
            )
            data = User(
                data.id,
                username,
                first_name,
                last_name,
                middle_name,
                bio,
                data.image,
                is_student.toString()
            )
            show(data)
            // Handle successful response
        } else {
            Log.e("ANLZYE_TIP", response.body().toString())
            dialog.dismiss()
        }
    }

    private fun getBytesFromUri(uri: Uri): ByteArray {
        val inputStream: InputStream? = context?.contentResolver?.openInputStream(uri)
        return inputStream?.readBytes() ?: ByteArray(0)
    }

    fun createPartFromString(partString: String): RequestBody {
        return partString.toRequestBody("text/plain".toMediaTypeOrNull())
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

                        dbHelper.deleteUser(user.id!!)
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