package karma.converter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import karma.converter.R
import karma.converter.base.OnItemClickListener




class ParentViewAdapter(private val parentItemList: List<ParentItem>) :
    RecyclerView.Adapter<ParentViewAdapter.ParentRecyclerViewHolder>(),
    OnItemClickListener<ChildItem> {

    private var clickListener: OnItemClickListener<ChildItem>? = null

    fun setOnItemClickListener(listener: OnItemClickListener<ChildItem>) {
        clickListener = listener
    }

    inner class ParentRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentImageView: ImageView = itemView.findViewById(R.id.parentLogoIv)
        val parentTitle: TextView = itemView.findViewById(R.id.parentTitleTv)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.childRecyclerView)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)

        init {
            itemView.setOnClickListener(View.OnClickListener { this@ParentViewAdapter })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.parent_item, parent, false)

        //        parentViewHolder.itemView.setOnClickListener {
//            val position = parentViewHolder.adapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                clickListener?.onItemClick(position,parentItemList[position].childItemList)
//            }
//        }
        return ParentRecyclerViewHolder(view)

    }

    override fun onBindViewHolder(holder: ParentRecyclerViewHolder, position: Int) {
        val parentItem = parentItemList[position]

        holder.parentTitle.text = parentItem.title
        holder.parentImageView.setImageResource(parentItem.image)

        holder.childRecyclerView.setHasFixedSize(true)
        holder.childRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 3)

        val adapter = ChildViewAdapter(parentItem.childItemList)
        holder.childRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)

        //expandable functionality
        val isExpandable = parentItem.isExpandable
        holder.childRecyclerView.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.constraintLayout.setOnClickListener {

            isAnyItemExpanded(position)
            parentItem.isExpandable = !parentItem.isExpandable
            notifyItemChanged(position)

        }

    }

    private fun isAnyItemExpanded(position: Int) {

        val temp = parentItemList.indexOfFirst {
            it.isExpandable
        }

        if (temp >= 0 && temp != position) {
            parentItemList[temp].isExpandable = false
            notifyItemChanged(temp)
        }
    }

    override fun getItemCount(): Int {
        return parentItemList.size
    }

    override fun onItemClick( item: ChildItem) {
       clickListener?.onItemClick(item)
    }


}
