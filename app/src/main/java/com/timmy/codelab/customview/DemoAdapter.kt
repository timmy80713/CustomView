package com.timmy.codelab.customview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timmy.codelab.customview.databinding.AdapterDemoBinding
import com.timmy.codelab.customview.widget.ClickableRecyclerViewAdapter

class DemoAdapter : ClickableRecyclerViewAdapter<Demo, DemoAdapter.ViewHolderDemo>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDemo {
        return ViewHolderDemo.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolderDemo, position: Int) {
        getItem(position).run {
            holder.setText(text)
            enableClickOnView(position, holder.itemView, this)
        }
    }

    class ViewHolderDemo(private val binding: AdapterDemoBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): ViewHolderDemo {
                val itemBinding = AdapterDemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolderDemo(itemBinding)
            }
        }

        fun setText(text: CharSequence?) {
            binding.demoText.text = text
        }
    }
}