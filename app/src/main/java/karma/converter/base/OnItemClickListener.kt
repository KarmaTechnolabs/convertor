package karma.converter.base

import androidx.annotation.IdRes

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
    //fun onItemClick(@IdRes viewIdRes: Int, model: T, position: Int)
}
