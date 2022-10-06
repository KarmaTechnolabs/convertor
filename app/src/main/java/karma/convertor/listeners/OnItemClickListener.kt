package karma.convertor.listeners

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClick(view:View?, obj:T,position:Int)
}