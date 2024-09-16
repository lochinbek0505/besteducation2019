package uz.bestedu.besteducation2019.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import uz.bestedu.besteducation2019.R
import uz.bestedu.besteducation2019.model.Lesson

class InnerAdapter(
    private val data: List<Lesson>,
    var isOpen: Boolean,
    var listener: ItemSetOnClickListener,
    var context: Context
) :
    RecyclerView.Adapter<InnerAdapter.InnerViewHolder>() {
    interface ItemSetOnClickListener {
        fun onClick(data: Lesson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.inner_item_layout, parent, false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val test = holder.itemView.findViewById<TextView>(R.id.tv_lesson_name)
        val image = holder.itemView.findViewById<ImageView>(R.id.is_pos)
        test.text = data[position].name
        if (isOpen) {
            if (data[position].isOpen) {
                image.setImageResource(R.drawable.play_button)
            } else {
                image.setImageResource(R.drawable.lock)

            }
        } else {
            image.setImageResource(R.drawable.lock)

        }
        holder.itemView.setOnClickListener {
            if (isOpen) {

                if (data[position].isOpen) {
                    listener.onClick(data[position])
                } else {

                    Toast.makeText(context, "Oldingi dars ko'rilmagan", Toast.LENGTH_LONG).show()

                }
            } else {
                Toast.makeText(context, "Kurs sotib olinmagan", Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
