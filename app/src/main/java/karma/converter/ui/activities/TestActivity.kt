package karma.converter.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import karma.converter.R
import karma.converter.adapter.ExpandableListAdapter
import karma.converter.api.requestmodel.ChildItem
import karma.converter.api.requestmodel.GroupItem
import karma.converter.custom.gotoActivity
import kotlinx.android.synthetic.main.activity_test.expandableListView

class TestActivity : AppCompatActivity() {

    val hed: MutableList<String> = ArrayList()
    val body: MutableList<MutableList<String>> = ArrayList()

    private lateinit var expandableListView: ExpandableListView
    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var groupItems: List<GroupItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

       /* val s1:MutableList<String> = ArrayList()
        s1.add("Hello")
        s1.add("hey")
        s1.add("hii")

        val s2:MutableList<String> = ArrayList()
        s2.add("aaa")
        s2.add("bbb")
        s2.add("ccc")

        val s3:MutableList<String> = ArrayList()
        s3.add("xxx")
        s3.add("yyy")
        s3.add("zzz")

        hed.add("S1")
        hed.add("S2")
        hed.add("S3")

        body.add(s1)
        body.add(s2)
        body.add(s3)

        expandableListView.setAdapter(ExpandableListAdapter(this,hed, body))
        expandableListView.setOnChildClickListener(object : ExpandableListView.OnChildClickListener{
            override fun onChildClick(
                parent: ExpandableListView?,
                v: View?,
                groupPosition: Int,
                childPosition: Int,
                id: Long
            ): Boolean {
                when(groupPosition){
                    0 -> {
                        when(childPosition){
                            0 -> gotoActivity(AreaActivity::class.java, needToFinish = false)
                        }
                    }
                }
                return true
            }

        })*/

        expandableListView = findViewById(R.id.expandableListView)
        groupItems = generateGroupItems()
        expandableListAdapter = ExpandableListAdapter(this, groupItems)
        expandableListView.setAdapter(expandableListAdapter)

    }

    private fun generateGroupItems(): List<GroupItem> {
        val groupItems = mutableListOf<GroupItem>()

        val childItems1 = listOf(
//            ChildItem("Child 1", R.drawable.area),
//            ChildItem("Child 2", R.drawable.angle),
            // Add more child items as needed
        ChildItem(R.drawable.noun_density,"Volume"),
        ChildItem(R.drawable.area,"Area"),
        ChildItem(R.drawable.energy,"Energy"),
        ChildItem(R.drawable.force_,"Force"),
        ChildItem(R.drawable.mobile_data,"Data"),
        ChildItem(R.drawable.temperature,"Temperature"),
        ChildItem(R.drawable.pressure_gauge,"Pressure"),
        ChildItem(R.drawable.electric_tower_power,"Power"),
        ChildItem(R.drawable.time,"Time"),
        ChildItem(R.drawable.angle,"Angle")
        )

        val childItems2 = listOf(
            ChildItem(R.drawable.noun_density,""),
            // Add more child items as needed
        )

        groupItems.add(GroupItem( R.drawable.ic_eng,"Engineering Converters", childItems1))
        groupItems.add(GroupItem( R.drawable.area,"Group 2", childItems2))
        // Add more group items as needed

        return groupItems
    }
}