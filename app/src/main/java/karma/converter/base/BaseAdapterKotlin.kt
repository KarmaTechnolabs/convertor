package karma.converter.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterKotlin<T, S : RecyclerView.ViewHolder?>(protected var context: Context?) :
    RecyclerView.Adapter<S>() {
    private var onItemClickListener: OnItemClickListener<T>? = null
    var list = ArrayList<T>()
        private set

    private var isLoading = false
    fun setDataArrayList(mArrayList: ArrayList<T>) {
        list = mArrayList
    }

    fun addItem(`object`: T) {
        list.add(`object`)
        notifyDataSetChanged()
    }

    fun setItems(arrayList: ArrayList<T>) {
        list.clear()
        list.addAll(arrayList)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addItems(arrayList: ArrayList<T>) {
        list.addAll(arrayList)
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        if (list.remove(item)) {
            notifyDataSetChanged()
        }
    }

    fun removeItem(position: Int) {
        if (list.size > position) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    open fun getListItem(position: Int): T? {
        return if (position >= list.size) {
            null
        } else list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateItem(pos: Int, memberModel: T) {
        list[pos] = memberModel
        notifyItemChanged(pos)
    }


}
