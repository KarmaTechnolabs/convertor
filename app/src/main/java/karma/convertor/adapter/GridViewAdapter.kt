package karma.convertor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import karma.convertor.R
import karma.convertor.api.requestmodel.UnitActivityModelResponse
import karma.convertor.api.requestmodel.UnititemModel
import karma.convertor.databinding.ListUnitItemBinding
import karma.convertor.databinding.UnitItemBinding
import karma.convertor.listeners.ItemClickListener
import java.util.*

class GridViewAdapter(context: Context?) :
    BaseAdapter<UnititemModel,GridViewAdapter.ViewHolder?>(context),
    Filterable {
    var filteredTicketList = ArrayList<UnititemModel>()
    var originalData = ArrayList<UnititemModel>()
    private var clickListener: ItemClickListener<UnititemModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: UnitItemBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.unit_item, parent, false
        )

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getListItem(position)
        if (model != null) {
            holder.setBinding(model)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val results = FilterResults()
                if (charSequence == null || charSequence.isEmpty()) {
                    results.values = originalData
                    results.count = originalData.size
                } else {
                    val filterData: MutableList<UnititemModel> = ArrayList()
                    for (model in list) {
                        if (model?.appname?.lowercase(Locale.getDefault())
                                ?.contains(charSequence)!!
                        ) {
                            filterData.add(model)
                        }
                    }
                    results.values = filterData
                    results.count = filterData.size
                }
                return results
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredTicketList = filterResults.values as ArrayList<UnititemModel>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredTicketList.size
    }

    override fun getListItem(position: Int):UnititemModel{
        return filteredTicketList[position]!!
    }

    inner class ViewHolder(binding: UnitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding:UnitItemBinding= binding
        fun setBinding(model: UnititemModel) {
            binding.model = model
            binding.clickListener = clickListener
            binding.position = absoluteAdapterPosition
            binding.executePendingBindings()
        }

        init {
            binding.root.setOnClickListener(View.OnClickListener { view ->
                if (onItemClickListener != null) {
                    val model: UnititemModel = getListItem(bindingAdapterPosition)
                    val selectedPos = bindingAdapterPosition
                    notifyDataSetChanged()
                    onItemClickListener?.onItemClick(view, model, selectedPos)
                }
            })
        }
    }

    init {
        filteredTicketList = list
        originalData = list
    }

    fun setClickListener(clickListener: ItemClickListener<UnititemModel>?) {
        this.clickListener = clickListener
    }

}
