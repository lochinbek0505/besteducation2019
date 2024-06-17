package com.example.besteducation2019.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.besteducation2019.R
import com.example.besteducation2019.databinding.MatchableItemBinding

class MatachableAdapter(
    val context: Context,
    var items: List<String>,
    var listener: ItemSetOnClickListener,
) : RecyclerView.Adapter<MatachableAdapter.Holder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    interface ItemSetOnClickListener {
        fun onClick(data: String, previousSelectedPosition: Int)
    }

    inner class Holder(var view: MatchableItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(data: String, isSelected: Boolean) {
            view.apply {
                this.tvA.text = data

                // Set the background based on whether this item is selected or not
                val bg = itemView.findViewById<RelativeLayout>(R.id.cv_a)
                if (isSelected) {
                    bg.setBackgroundResource(R.drawable.selected_bg)
                } else {
                    bg.setBackgroundResource(R.drawable.defould_bg)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            MatchableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }
    fun resetAllItemBackgrounds() {
        selectedPosition = RecyclerView.NO_POSITION
        for (i in items.indices) {
            notifyItemChanged(i)
        }
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.bind(item, position == selectedPosition)

        holder.itemView.setOnClickListener {
            // Update the background of all items
            val previousSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            // Notify the previous and new selected items to change their background
            if (previousSelectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousSelectedPosition)
            }
            notifyItemChanged(selectedPosition)

            // Trigger the listener
            listener.onClick(item,selectedPosition)
        }
    }


    override fun getItemCount(): Int = items.count()
}
