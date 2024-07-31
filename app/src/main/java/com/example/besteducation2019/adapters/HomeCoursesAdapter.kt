package com.example.besteducation2019.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.besteducation2019.R
import com.example.besteducation2019.databinding.HomeItemLayoutBinding
import com.example.besteducation2019.model.Course
import com.google.android.material.button.MaterialButton


class HomeCoursesAdapter(
    val context: Context,
    var items: MutableList<Course>,
    var listener: ItemSetOnClickListener,
    var listener2: ItemSetOnClickListener2,

    ) :
    RecyclerView.Adapter<HomeCoursesAdapter.Holder>() {

    var nom = 0

    interface ItemSetOnClickListener {
        fun onClick(data: Course)
    }

    interface ItemSetOnClickListener2 {
        fun onClick(id: String)
    }

    inner class Holder(var view: HomeItemLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(data: Course) {

            view.apply {

                if (data.is_open) {
                    this.checkRl.visibility = View.GONE
                }

                this.tvCoursesName.text = data.name
                this.tvDescription.text = data.description
                this.tvTeacher.text = "${data.author_.firstName} ${data.author_.lastName}"
                this.tvFeedback.text = data.feedback.toDouble().toString()
                this.tvPrice.text = "${data.price} so'm"
                this.tvParticantCount.text = "${data.count_students.toString()} ta o'quvchi"
                Glide.with(context).load(data.image).into(this.ivImage)

            }


        }


    }

    fun updateList(newList: List<Course>) {
        items = newList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            HomeItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(
            binding
        )


    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]

        holder.bind(item)

        val button = holder.itemView.findViewById<MaterialButton>(R.id.btn_buy2)

        button.setOnClickListener {

            listener2.onClick(item.id.toString())

        }

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