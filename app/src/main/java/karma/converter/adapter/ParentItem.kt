package karma.converter.adapter

data class ParentItem(
    val image : Int ,
    val title : String,
    val childItemList : ArrayList<ChildItem>,
    var isExpandable : Boolean = false
)