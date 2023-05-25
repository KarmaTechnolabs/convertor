package karma.converter.api.requestmodel

data class GroupItem(val imageResId: Int,val title: String, val childItems: List<ChildItem>)

data class ChildItem( val imageResId: Int,val title: String)
