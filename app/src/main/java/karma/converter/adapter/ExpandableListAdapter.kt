package karma.converter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import karma.converter.R
import karma.converter.api.requestmodel.ChildItem
import karma.converter.api.requestmodel.GroupItem

/*class ExpandableListAdapter(var context: Context,var hed:MutableList<String>,var body:MutableList<MutableList<String>>):BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return hed.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return body[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): String {
        return hed[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return body[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertViewGroup = convertView
        if (convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewGroup = inflater.inflate(R.layout.item_grup,null)
        }
        val title = convertViewGroup?.findViewById<TextView>(R.id.tvGrp)
        title?.text = getGroup(groupPosition)
        return convertViewGroup
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertViewChild = convertView
        if (convertViewChild == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewChild = inflater.inflate(R.layout.item_grup,null)
        }
        val title = convertViewChild?.findViewById<TextView>(R.id.tvGrp)
        title?.text = getGroup(groupPosition)
        return convertViewChild
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}*/

class ExpandableListAdapter(private val context: Context, private val groupItems: List<GroupItem>) :
    BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return groupItems.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return groupItems[groupPosition].childItems.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return groupItems[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return groupItems[groupPosition].childItems[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var view = convertView
        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_grup, null)
        }

        val groupItem = getGroup(groupPosition) as GroupItem
        val groupTitle = view?.findViewById<TextView>(R.id.tvGrp)
        val groupImage = view?.findViewById<ImageView>(R.id.groupImage)

        groupTitle?.text = groupItem.title
        groupImage?.setImageResource(groupItem.imageResId)

        return view!!
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var view = convertView
        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_child, null)
        }

        val childItem = getChild(groupPosition, childPosition) as ChildItem
        val childTitle = view?.findViewById<TextView>(R.id.tvChild)
        val childImage = view?.findViewById<ImageView>(R.id.childImage)


        childTitle?.text = childItem.title
        childImage?.setImageResource(childItem.imageResId)

        return view!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
