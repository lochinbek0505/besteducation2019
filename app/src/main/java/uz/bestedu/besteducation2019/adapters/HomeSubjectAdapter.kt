package uz.bestedu.besteducation2019.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bestedu.besteducation2019.databinding.ItemCatHomeLayoutBinding
import uz.bestedu.besteducation2019.model.Subject


class HomeSubjectAdapter(
    val context: Context,
    var items: List<Subject>,
    var listener: ItemSetOnClickListener,
) :
    RecyclerView.Adapter<HomeSubjectAdapter.Holder>() {

    var nom = 0

    interface ItemSetOnClickListener {
        fun onClick(data: Subject)
    }

    inner class Holder(var view: ItemCatHomeLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(data: Subject) {

            view.apply {
                this.tvSubject.text = data.name

//                this.tvCoursesName.text=data.name
//                this.tvDescription.text=data.description
//                this.tvTeacher.text = "${data.author_.firstName} ${data.author_.lastName}"
//                this.tvFeedback.text = data.feedback.toDouble().toString()
//                this.tvPrice.text="${data.price} so'm"
//                this.tvParticantCount.text = "${ data.count_students.toString() } ta o'quvchi"
//                Glide.with(context).load(data.image).into(this.ivImage)

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val binding =
            ItemCatHomeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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