package com.example.besteducation2019.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.besteducation2019.databinding.CoursesListItemBinding
import com.example.besteducation2019.model.Coursesa


class CoursesAdapter(
    val context: Context,
    var items: MutableList<Coursesa>,
    var listener: ItemSetOnClickListener,

    ) :
    RecyclerView.Adapter<CoursesAdapter.Holder>() {

    var nom = 0

    interface ItemSetOnClickListener {
        fun onClick(data: Coursesa)
    }


    inner class Holder(var view: CoursesListItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(data: Coursesa) {

            view.apply {
                this.sekk.setOnTouchListener { _, _ ->
                    true
                }
                this.tvPr.text = "${data.percentage!!.toInt().toString()} %"
                this.sekk.progress = data.percentage!!.toInt()
                this.tvAuthor.text = "${data.author!!.firstName} ${data.author!!.lastName}"
                Glide.with(context).load(data.author!!.image).into(this.tvImage)
                this.tvName.text = data.name

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            CoursesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(
            binding
        )


    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]

        holder.bind(item)



        holder.itemView.setOnClickListener {


            listener.onClick(item)


        }
    }

    fun check(a: Int): Boolean {

        val arr = arrayListOf<Int>(3, 8, 13, 18, 23, 28, 33, 38, 43, 48, 53, 58, 63, 64, 65)

        if (arr.contains(a)) {

            return false

        } else {
            return true
        }


    }


    override fun getItemCount(): Int = items.count()

}