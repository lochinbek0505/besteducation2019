package com.example.besteducation2019.ui.activitys

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.besteducation2019.adapters.MatachableAdapter
import com.example.besteducation2019.adapters.MatachableAdapter2
import com.example.besteducation2019.databinding.ActivityMatachableBinding

class MatachableActivity : AppCompatActivity() {

    lateinit var adapter: MatachableAdapter
    lateinit var adapter2: MatachableAdapter2
    lateinit var binding: ActivityMatachableBinding
    var count = 0
    var list = arrayListOf("", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatachableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list1 = mutableListOf(

            "Hello",
            "welcome",
            "one",

            )
        var list2 = mutableListOf(
            "Salom",
            "Xushkelibsiz",
            "bir"

        )
        adapter = MatachableAdapter(this, list1, object : MatachableAdapter.ItemSetOnClickListener {
            override fun onClick(data: String, previousSelectedPosition: Int) {
                count++
                list[0] = data

                if (count == 2) {
                    list1.remove(list.get(0))
                    list2.remove(list.get(1))
                    adapter.resetAllItemBackgrounds()
                    adapter.notifyDataSetChanged()
                    adapter2.notifyDataSetChanged()
//                    adapter.notifyItemRangeChanged(0, list1.size - 1)
//                    adapter2.notifyItemRangeChanged(0, list2.size - 1)
                    adapter.resetAllItemBackgrounds()
                    adapter2.resetAllItemBackgrounds()
                    count = 0
//                    adapter.notifyItemChanged(previousSelectedPosition)
                }
                Toast.makeText(this@MatachableActivity, "Click", Toast.LENGTH_SHORT).show()
            }

        })
        adapter2 =
            MatachableAdapter2(this, list2, object : MatachableAdapter2.ItemSetOnClickListener {
                override fun onClick(data: String, previousSelectedPosition: Int) {
                    Toast.makeText(this@MatachableActivity, "Click", Toast.LENGTH_SHORT).show()

                    list[1] = data
                    count++
                    if (count == 2) {
                        list1.remove(list.get(0))
                        list2.remove(list.get(1))

                        adapter.notifyDataSetChanged()
                        adapter2.notifyDataSetChanged()
//                        adapter.notifyItemRangeChanged(0, list1.size - 1)
//                        adapter2.notifyItemRangeChanged(0,list2.size-1)
                        adapter.resetAllItemBackgrounds()
                        adapter2.resetAllItemBackgrounds()
//                        adapter2.notifyItemChanged(previousSelectedPosition)
//                        adapter2.notifyItemRangeChanged(0, list2.size - 1)
                        count = 0
                    }
                }


            })
        binding.rvMat1.adapter = adapter
        binding.rvMat2.adapter = adapter2
    }
}