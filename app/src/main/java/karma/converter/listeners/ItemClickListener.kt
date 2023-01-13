package karma.converter.listeners

import androidx.annotation.IdRes

interface ItemClickListener<T> {
    fun onItemClick(@IdRes viewIdRes: Int, model: T, position: Int)
}