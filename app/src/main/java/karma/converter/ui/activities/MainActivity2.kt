package karma.converter.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import karma.converter.R
import karma.converter.adapter.ChildItem
import karma.converter.adapter.ParentItem
import karma.converter.adapter.ParentViewAdapter
import karma.converter.base.OnItemClickListener

class MainActivity2 : AppCompatActivity(), OnItemClickListener<ChildItem> {

    private lateinit var parentRecyclerView: RecyclerView
    private var parentList: ArrayList<ParentItem> = ArrayList()
    private var parentViewAdapter: ParentViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        parentRecyclerView = findViewById(R.id.parentRecyclerView)
        parentRecyclerView.setHasFixedSize(true)
        parentRecyclerView.layoutManager = LinearLayoutManager(this)
        //parentList = ArrayList()

        prepareData()
        parentViewAdapter = ParentViewAdapter(parentList)
        parentRecyclerView.adapter = parentViewAdapter
        parentViewAdapter?.setOnItemClickListener(this)

    }

    private fun prepareData() {
        val items1 = ArrayList<ChildItem>()
        items1.add(ChildItem(0, R.drawable.area, "Area"))
        items1.add(ChildItem(1, R.drawable.angle, "angle"))
        items1.add(ChildItem(2, R.drawable.circle, "circle"))
        parentList.add(ParentItem(R.drawable.ic_eng, "Engineering Converter", items1))

        val items2 = ArrayList<ChildItem>()
        items2.add(ChildItem(11, R.drawable.angle, "angle"))
        items2.add(ChildItem(12, R.drawable.circle, "circle"))
        items2.add(ChildItem(13, R.drawable.area, "Area"))
        parentList.add(ParentItem(R.drawable.ic_eng, "Enger", items2))

        val items3 = ArrayList<ChildItem>()
        items3.add(ChildItem(21, R.drawable.circle, "circle"))
        items3.add(ChildItem(22, R.drawable.area, "Area"))
        items3.add(ChildItem(23, R.drawable.angle, "angle"))
        parentList.add(ParentItem(R.drawable.ic_eng, "Enger", items3))
    }

    override fun onItemClick(item: ChildItem) {

        when (item.id) {
            0 -> {
                Log.d("TAG", "onItemClick() called with: item = $item")
            }

            11 -> {
                Log.d("TAG", "onItemClick() called with: item = $item")
            }
        }


    }


}