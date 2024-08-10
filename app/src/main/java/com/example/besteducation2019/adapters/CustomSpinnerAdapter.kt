package com.example.besteducation2019.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.besteducation2019.R
import com.example.besteducation2019.model.Courses77

class CustomSpinnerAdapter(
        context: Context,
        private val resource: Int,
        private val items: List<Courses77>
    ) : ArrayAdapter<Courses77>(context, resource, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createItemView(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createItemView(position, convertView, parent)
        }

        private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
            val item = items[position]
            val textView: TextView = view.findViewById(R.id.textViewSpinnerItem)
            textView.text = item.name
            return view
        }
    }
