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
import karma.convertor.databinding.ListUnitItemBinding
import karma.convertor.databinding.UnitItemBinding
import karma.convertor.listeners.ItemClickListener
import java.util.*

class UnitActivityAdpater(context: Context?) :
    BaseAdapter<UnitActivityModelResponse, UnitActivityAdpater.ViewHolder?>(context),
    Filterable {
    var filteredTicketList = ArrayList<UnitActivityModelResponse>()
    var originalData = ArrayList<UnitActivityModelResponse>()
    private var clickListener: ItemClickListener<UnitActivityModelResponse>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListUnitItemBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_unit_item, parent, false
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
                    val filterData: MutableList<UnitActivityModelResponse> = ArrayList()
                    for (model in list) {
                        if (model?.unit?.lowercase(Locale.getDefault())
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
                filteredTicketList = filterResults.values as ArrayList<UnitActivityModelResponse>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredTicketList.size
    }

    override fun getListItem(position: Int):UnitActivityModelResponse{
        return filteredTicketList[position]!!
    }

    inner class ViewHolder(binding: ListUnitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ListUnitItemBinding = binding
        fun setBinding(model: UnitActivityModelResponse) {
            binding.model = model
            binding.clickListener = clickListener
            binding.position = absoluteAdapterPosition
            binding.executePendingBindings()
        }

        init {
            binding.root.setOnClickListener(View.OnClickListener { view ->
                if (onItemClickListener != null) {
                    val model: UnitActivityModelResponse = getListItem(bindingAdapterPosition)
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

    fun setClickListener(clickListener: ItemClickListener<UnitActivityModelResponse>?) {
        this.clickListener = clickListener
    }

}
