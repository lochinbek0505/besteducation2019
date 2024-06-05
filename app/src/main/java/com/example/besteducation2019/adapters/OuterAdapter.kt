package com.example.besteducation2019.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.besteducation2019.R
import com.example.besteducation2019.model.Lesson
import com.example.besteducation2019.model.Module

class OuterAdapter(
    private val outerData: MutableList<Module>,
    var listener: ItemSetOnClickListener,
    var context: Context
) :
    RecyclerView.Adapter<OuterAdapter.OuterViewHolder>() {

    interface ItemSetOnClickListener {

        fun onClick(modul: Module, lessonXX: Lesson)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.outer_item_layout, parent, false)
        return OuterViewHolder(view)
    }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {

        if (!outerData[position].lessons.isEmpty()) {

            val innerRecyclerView: RecyclerView =
                holder.itemView.findViewById(R.id.innerRecyclerView)

            val image: ImageView = holder.itemView.findViewById(R.id.iv_check)

            val test = holder.itemView.findViewById<TextView>(R.id.tv_name)

            test.text = "${position + 1} . ${outerData[position].name}"
            val modul = outerData[position]
            val adapter = InnerAdapter(outerData[position].lessons,modul.isOpen,
                object : InnerAdapter.ItemSetOnClickListener {

                    override fun onClick(data: Lesson) {

                        listener.onClick(modul, data)

                    }

                }, context
            )
            val adapter2 = InnerAdapter(emptyList(), modul.isOpen,object : InnerAdapter.ItemSetOnClickListener {
                override fun onClick(data: Lesson) {

                }
            }, context)
            var check = true

            holder.itemView.setOnClickListener {

                if (check) {
                    println("ISHLADI ISHLADI ISHLADI")

                    innerRecyclerView.adapter = adapter
                    check = !check
                    image.setImageResource(R.drawable.remove)
                    println("ISHLADI ISHLADI $check")

                } else {

                    println("ISHLADI ISHLADI ISHLADI")
                    innerRecyclerView.adapter = adapter2
                    check = !check
                    image.setImageResource(R.drawable.add)


                }


            }
        }
    }

    override fun getItemCount(): Int {
        return outerData.size
    }

    inner class OuterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
