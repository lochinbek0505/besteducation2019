package uz.bestedu.besteducation2019.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bestedu.besteducation2019.databinding.BillingReportLayoutBinding
import uz.bestedu.besteducation2019.model.Data100


class BillingAdapter(
    var items: MutableList<Data100>,
) :
    RecyclerView.Adapter<uz.bestedu.besteducation2019.adapters.BillingAdapter.Holder>() {


    inner class Holder(var view: BillingReportLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(data: Data100) {

            view.apply {

                if (data.status != "0") {
                    this.tvSumma.text = "${data.course?.price.toString()} so'm"
                    this.tvTime.text = data.created.toString()
                    this.tvCourseName.text = data.course?.name.toString()
                }
            }


        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingAdapter.Holder {

        val binding =
            BillingReportLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(
            binding
        )


    }

    override fun onBindViewHolder(holder: BillingAdapter.Holder, position: Int) {
        val item = items[position]

        holder.bind(item)

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

