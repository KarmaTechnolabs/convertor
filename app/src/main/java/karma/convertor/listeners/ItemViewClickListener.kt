package karma.convertor.listeners

import android.view.View

interface ItemViewClickListener<T> {
    fun onItemClick(view: View, model: T, position: Int)
}