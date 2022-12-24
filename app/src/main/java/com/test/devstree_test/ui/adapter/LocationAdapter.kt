package com.test.devstree_test.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.devstree_test.database.entity.LocationEntity
import com.test.devstree_test.databinding.RowLocationItemBinding
import com.test.devstree_test.ui.util.Constant.DELETE_VIEW
import com.test.devstree_test.ui.util.Constant.EDIT_VIEW
import com.test.devstree_test.ui.util.ExtensionFunction.autoNotify
import kotlin.properties.Delegates

class LocationAdapter(var listener: (message: String, location: LocationEntity) -> Unit) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    private var locations: ArrayList<LocationEntity> by Delegates.observable(arrayListOf()) { p, old, new ->
        autoNotify(old, new) { o, n -> true/*o.productId == n.productId*/ }
    }

    inner class ViewHolder(val binding: RowLocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val model = locations[position]
            binding.model = model
            binding.ivEdit.setOnClickListener {
                listener.invoke(EDIT_VIEW, model)
            }
            binding.ivDelete.setOnClickListener {
                listener.invoke(DELETE_VIEW, model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RowLocationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    fun setData(locations: ArrayList<LocationEntity>) {
        this.locations = locations
    }
}