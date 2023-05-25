package karma.converter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import karma.converter.R
import karma.converter.base.OnItemClickListener

/*class ChildViewAdapter(context: Context?) :
    BaseAdapterKotlin<ChildItem, ChildViewAdapter.ChildViewHolder?>(context) {

    private var clickListener: OnItemClickListener<ChildItem>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding: ChildItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.child_item, parent, false
        )
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val model = getListItem(position)
        holder.setBinding(model)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getListItem(position: Int): ChildItem? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun setClickListener(clickListener: OnItemClickListener<ChildItem>){
        this.clickListener = clickListener
    }

    inner class ChildViewHolder(binding: ChildItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var bind: ChildItemBinding = binding
        fun setBinding(model: ChildItem?) {
            bind.data = model
            bind.clickListener = clickListener
            bind.position = layoutPosition
            bind.executePendingBindings()
        }
    }


}*/

class ChildViewAdapter(private val childList: List<ChildItem>) :
    RecyclerView.Adapter<ChildViewAdapter.ChildViewHolder>() {

    private var clickListener: OnItemClickListener<ChildItem>? = null

    fun setOnItemClickListener(listener: ParentViewAdapter) {
        clickListener = listener
    }


    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.childLogoIv)
        val title: TextView = itemView.findViewById(R.id.childTitleTv)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener?.onItemClick(childList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_item, parent, false)
        val viewHolder = ChildViewHolder(view)

        // Set the click listener
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener?.onItemClick(childList[position])
            }
        }

        return viewHolder
    }


    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.imageView.setImageResource(childList[position].image)
        holder.title.text = childList[position].title
    }

    override fun getItemCount(): Int {
        return childList.size
    }
}
