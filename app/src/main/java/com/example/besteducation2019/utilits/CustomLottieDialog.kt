package com.example.besteducation2019.utilits

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.airbnb.lottie.LottieAnimationView
import com.example.besteducation2019.R

class CustomLottieDialog(context: Context) {
    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_lottie, null)
        builder.setCancelable(false)
        builder.setView(view)
        dialog = builder.create()

        val animationView = view.findViewById<LottieAnimationView>(R.id.anim)

//        animationVie
    }


    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
