package uz.bestedu.besteducation2019.ui.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import uz.bestedu.besteducation2019.R
import uz.bestedu.besteducation2019.databinding.ActivitySignBinding

class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        setContentView(binding.root)
        val token = readFromSharedPreferences(this, "TOKEN", "")

        if (!token.isEmpty()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        val navController = findNavController(R.id.nav_sign_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_sign_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

}