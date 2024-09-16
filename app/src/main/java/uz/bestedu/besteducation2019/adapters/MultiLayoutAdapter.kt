package uz.bestedu.besteducation2019.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bestedu.besteducation2019.databinding.BronzeRateLayoutBinding
import uz.bestedu.besteducation2019.databinding.DefoultRateLayoutBinding
import uz.bestedu.besteducation2019.databinding.GoldRateLayoutBinding
import uz.bestedu.besteducation2019.databinding.SilverRateLayoutBinding
import uz.bestedu.besteducation2019.model.Ratings

class MultiLayoutAdapter(private val items: List<Ratings>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_LAYOUT_1 = 0
        private const val TYPE_LAYOUT_2 = 1
        private const val TYPE_LAYOUT_3 = 2
        private const val TYPE_LAYOUT_4 = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_LAYOUT_1
            1 -> TYPE_LAYOUT_2
            2 -> TYPE_LAYOUT_3
            else -> TYPE_LAYOUT_4
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LAYOUT_1 -> {
                val binding = GoldRateLayoutBinding.inflate(inflater, parent, false)
                Layout1ViewHolder(binding)
            }

            TYPE_LAYOUT_2 -> {
                val binding = SilverRateLayoutBinding.inflate(inflater, parent, false)
                Layout2ViewHolder(binding)
            }

            TYPE_LAYOUT_3 -> {
                val binding = BronzeRateLayoutBinding.inflate(inflater, parent, false)
                Layout3ViewHolder(binding)
            }

            else -> {

                val binding = DefoultRateLayoutBinding.inflate(inflater, parent, false)
                Layout4ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is Layout1ViewHolder -> holder.bind(items[position])
            is Layout2ViewHolder -> holder.bind(items[position])
            is Layout3ViewHolder -> holder.bind(items[position])
            is Layout4ViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Layout1ViewHolder(private val binding: GoldRateLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: Ratings) {
            binding.tvName.text = "${text.author.firstName} ${text.author.lastName}"
            binding.tvBall.text = text.score.toString()
        }
    }

    inner class Layout2ViewHolder(private val binding: SilverRateLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: Ratings) {
            binding.tvName.text = "${text.author.firstName} ${text.author.lastName}"
            binding.tvBall.text = text.score.toString()
        }
    }

    inner class Layout3ViewHolder(private val binding: BronzeRateLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: Ratings) {
            binding.tvName.text = "${text.author.firstName} ${text.author.lastName}"
            binding.tvBall.text = text.score.toString()
        }
    }

    inner class Layout4ViewHolder(private val binding: DefoultRateLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: Ratings) {
            binding.tvName.text = "${text.author.firstName} ${text.author.lastName}"
            binding.tvBall.text = text.score.toString()
            binding.tvId.text = (position + 1).toString()
        }
    }
}
