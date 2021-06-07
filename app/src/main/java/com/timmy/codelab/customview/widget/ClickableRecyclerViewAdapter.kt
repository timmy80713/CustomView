package com.timmy.codelab.customview.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.timmy.codelab.customview.R

/**
 * 可被點擊的 RecycleView Adapter，只能指定一個元件被點，通常會是 itemView，如果有其他元件需要點擊的話，要自行實作
 */
abstract class ClickableRecyclerViewAdapter<T, VH> : RecyclerView.Adapter<VH>() where  VH : RecyclerView.ViewHolder {

    // 定義在 ids.xml 內，避免被人手殘弄到重複。
    private val DATA_INDEX = R.id.tag_row_data
    private val POSITION_INDEX = R.id.tag_row_position

    // 預先準備好空的 List，讓 getItemCount 不會 NullPointer
    var data: List<T> = emptyList()
        private set

    var itemClickBlock: ((position: Int, view: View, data: T) -> Unit)? = null

    private val clickListener: View.OnClickListener = View.OnClickListener { v ->
        // 利用泛型進行 cast，方便在接的時候直接取用不用再轉一次。
        itemClickBlock?.invoke(v.getTag(POSITION_INDEX) as Int, v, v.getTag(DATA_INDEX) as T)
    }

    open fun setData(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }

    open fun setDataWithoutNotifyDataSetChanged(data: List<T>) {
        this.data = data
    }

    // 定義類似 ListView 的 Adapter 使用的 getItem，方便在 onBindViewHolder 使用。
    fun getItem(position: Int): T {
        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun bindDataOnView(position: Int, view: View, data: T) {
        view.setTag(POSITION_INDEX, position)
        view.setTag(DATA_INDEX, data)
    }

    // 在 Bind 時只要呼叫這個功能就可以在某個 View 加上 Click 的接收。
    protected fun enableClickOnView(position: Int, view: View, data: T) {
        bindDataOnView(position, view, data)
        view.setOnClickListener(clickListener)
    }
}