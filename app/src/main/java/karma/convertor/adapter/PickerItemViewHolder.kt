package karma.convertor.adapter
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import karma.convertor.R

class PickerItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    val tvItem: TextView? = itemView?.findViewById(R.id.tv_item)
}