package com.example.ribs_demo_android.ribs.root.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.databinding.ItemCategoryBinding
import com.example.ribs_demo_android.models.Catalogue

class CategoryAdapter(
    private val items: List<Catalogue>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Catalogue) {
            binding.nameCategory.text = item.name
            binding.root.setOnClickListener {
                onClick(item.name)
            }

            Glide.with(binding.root)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.iconCategory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}