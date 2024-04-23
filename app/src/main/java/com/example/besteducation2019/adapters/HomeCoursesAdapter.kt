package com.example.besteducation2019.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.besteducation2019.databinding.HomeItemLayoutBinding
import com.example.besteducation2019.model.courses_modelItem


class HomeCoursesAdapter(
    var items: MutableList<courses_modelItem>,
    var listener: ItemSetOnClickListener,
) :
    RecyclerView.Adapter<HomeCoursesAdapter.Holder>() {

    var nom = 0

    interface ItemSetOnClickListener {
        fun onClick(data: courses_modelItem)
    }

    inner class Holder(var view:HomeItemLayoutBinding ) : RecyclerView.ViewHolder(view.root) {

        fun bind(data: courses_modelItem) {

            view.apply {


                this.tvCoursesName.text=data.name
                this.tvDescription.text=data.description
                this.tvTeacher.text= "${ data.author.first_name } ${data.author.last_name}"
                this.tvFeedback.text=data.feedback.toString()
                this.tvPrice.text="${data.price} so'm"
//                this.tvRegion.text = data
            }


        }


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