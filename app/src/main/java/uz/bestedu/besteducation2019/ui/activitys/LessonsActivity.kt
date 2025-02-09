package uz.bestedu.besteducation2019.ui.activitys

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import kotlinx.coroutines.launch
import uz.bestedu.besteducation2019.databinding.ActivityLessonsBinding
import uz.bestedu.besteducation2019.model.Lesson2
import uz.bestedu.besteducation2019.model.lesson_datailes
import uz.bestedu.besteducation2019.model.lesson_id_model
import uz.bestedu.besteducation2019.model.request_end
import uz.bestedu.besteducation2019.network.ApiService
import uz.bestedu.besteducation2019.network.RetrofitBuilder
import java.util.regex.Pattern


class LessonsActivity : AppCompatActivity() {


    companion object {
        const val REQUEST_WRITE_STORAGE = 112
    }
    private lateinit var binding: ActivityLessonsBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        val data = intent.getSerializableExtra("LESSON") as lesson_id_model

        network(data)



        binding.btnBuy.setOnClickListener {
            end(data)

        }
    }

    private fun downloadPdf(url: String,name:String) {
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment ?: "$name.pdf"

        val request = DownloadManager.Request(uri)
        request.setTitle("$name PDF")
        request.setDescription("Downloading PDF file...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
    }

    fun network(data: lesson_id_model) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this, "TOKEN", ""))

        lifecycleScope.launch {

            try {


                val request = apiService.lessonDeatile(data.id1, data.id2, data.id3)
                println(request.body())
                Log.e("ANLZYE4", request.toString())

                if (request.isSuccessful) {
                    var body = request.body() as lesson_datailes
                    display(body.data.lesson)
                    Log.e("ANLZYE4", body.toString())
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@LessonsActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun end(data: lesson_id_model) {


        apiService =
            RetrofitBuilder.create(readFromSharedPreferences(this, "TOKEN", ""))

        lifecycleScope.launch {

            try {


                val request = apiService.endLessons(request_end(data.id3))
                println(request.body())
                Log.e("ANLZYE4", request.toString())

                if (request.isSuccessful) {
//                    startActivity(Intent(this@LessonsActivity, ShowCourseActivity::class.java))
                    finish()
                }

            } catch (e: Exception) {
                Log.e("ANLZYE4", e.message.toString())

                Toast.makeText(this@LessonsActivity, e.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun display(model: Lesson2) {
        binding.anim.visibility = View.GONE

        val url = model.video
        binding.tvName.text = model.name
        binding.btnDownload.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Scoped storage (Android 10 and above)
                downloadPdf(model.resource,model.name)
            } else {
                // Legacy storage (Android 9 and below)
                checkAndRequestStoragePermission(model.resource,model.name)
            }

        }
//        https://youtu.be/X-WIG2k7Ruw
        val lastChar = url.substring(url.length - 1)
        if (lastChar == "/") {

            val last = url.substring(0, url.length - 1)
            val substringAfterLastSlash = last.substringAfterLast("/")

            val youTubePlayerView = binding.youtubePlayerView
//            val youtubeUrl = "https://www.youtube.com/watch?v=VIDEO_ID"

            // Extract video ID from the URL
            val videoId = extractVideoIdFromUrl(url)


            lifecycle.addObserver(youTubePlayerView)

            lifecycle.addObserver(youTubePlayerView)

            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {

//                    val videoUrl=model.video
                    youTubePlayer.loadVideo(videoId, 0f)


                }
            })
//            youTubePlayerView.
            youTubePlayerView.enableAutomaticInitialization = false
            val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val defaultPlayerUiController =
                        DefaultPlayerUiController(youTubePlayerView, youTubePlayer)
                    defaultPlayerUiController.showVideoTitle(false)
                    defaultPlayerUiController.showYouTubeButton(false)
                    defaultPlayerUiController.showFullscreenButton(true)

                    youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.rootView)

                }
            }

            val options = IFramePlayerOptions.Builder()
                .controls(0)
                .fullscreen(0)
                .build()

            youTubePlayerView.initialize(listener, options)


        } else {


            val substringAfterLastSlash = url.substringAfterLast("/")
            val youTubePlayerView = binding.youtubePlayerView


            lifecycle.addObserver(youTubePlayerView)

            lifecycle.addObserver(youTubePlayerView)
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {

                    val videoId = substringAfterLastSlash
                    youTubePlayer.loadVideo(videoId, 0f)


                }
            })

            youTubePlayerView.enableAutomaticInitialization = false
            val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val defaultPlayerUiController =
                        DefaultPlayerUiController(youTubePlayerView, youTubePlayer)
                    defaultPlayerUiController.showVideoTitle(false)
                    defaultPlayerUiController.showYouTubeButton(false)
                    defaultPlayerUiController.showFullscreenButton(true)

                    youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.rootView)

                }
            }

            val options = IFramePlayerOptions.Builder()
                .controls(0)
                .fullscreen(0)
                .build()
            youTubePlayerView.initialize(listener, options)


        }
        Log.e("ANLZYE", model.resource.toString())


        binding.prLessons.initWithUrl(
            url = model.resource,
            lifecycleCoroutineScope = lifecycleScope,
            lifecycle = lifecycle
        )


    }
    private fun checkAndRequestStoragePermission(url:String,name:String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE
            )
        } else {
            downloadPdf(url,name)
        }
    }
    private fun extractVideoIdFromUrl(youtubeUrl: String): String {
        val pattern =
            Pattern.compile("(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]{11})")
        val matcher = pattern.matcher(youtubeUrl)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            ""
        }
    }
}