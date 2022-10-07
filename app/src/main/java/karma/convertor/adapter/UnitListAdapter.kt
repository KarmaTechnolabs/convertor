package karma.convertor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import karma.convertor.api.requestmodel.UnititemModel
import karma.convertor.databinding.UnitItemBinding
import karma.convertor.listeners.ItemClickListener

class UnitListAdapter internal constructor(
    context: Context,
    private val resource: Int,
    private val itemList: ArrayList<UnititemModel>?
) : ArrayAdapter<UnitListAdapter.ItemViewHolder>(context, resource) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var itemBinding: UnitItemBinding
    private var clickListener: ItemClickListener<UnititemModel>? = null

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        val holder: ItemViewHolder
        if (convertView == null) {
            itemBinding =UnitItemBinding.inflate(inflater)
            convertView = itemBinding.root
            holder = ItemViewHolder(itemBinding)

            val model = getListItem(position)
            if (model != null) {
                holder.setBinding(model)
            }

            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }

        return convertView
    }

    fun getListItem(position: Int): UnititemModel {
        return itemList?.get(position)!!
    }


    inner class ItemViewHolder(binding: UnitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: UnitItemBinding = binding
        fun setBinding(model: UnititemModel) {
            binding.model = model
            binding.clickListener = clickListener
            binding.position = absoluteAdapterPosition
            binding.executePendingBindings()
        }

    }



    fun setClickListener(clickListener: ItemClickListener<UnititemModel>?) {
        this.clickListener = clickListener
    }


}
