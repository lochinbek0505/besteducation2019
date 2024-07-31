package com.example.besteducation2019.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.besteducation2019.R
import com.example.besteducation2019.model.transfer_model

class LessonsAdapter(
//    private  val isOpen:Boolean,
    private val data: ArrayList<transfer_model>,
    var listener: ItemSetOnClickListener,
    var context: Context
) :
    RecyclerView.Adapter<LessonsAdapter.InnerViewHolder>() {
    interface ItemSetOnClickListener {
        fun onClick(data: transfer_model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.lessons_list_item, parent, false)
        return InnerViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val test = holder.itemView.findViewById<TextView>(R.id.tv_name)
        val image = holder.itemView.findViewById<ImageView>(R.id.iv_state)
        test.text = data[position].lessone.name

        if (data[position].lessone.isOpen) {
        } else {
            image.setImageResource(R.drawable.lock)

        }

        holder.itemView.setOnClickListener {

            if (data[position].lessone.isOpen) {
                listener.onClick(data[position])
            } else {

                Toast.makeText(context, "Oldingi dars ko'rilmagan", Toast.LENGTH_LONG).show()

            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
