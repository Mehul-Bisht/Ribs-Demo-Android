package com.example.ribs_demo_android.ribs.root.home.catalogue.adapter.CatalogueAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.databinding.ItemCatalogueBinding
import com.example.ribs_demo_android.models.Catalogue

class CatalogueAdapter(
    private val items: List<Catalogue>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CatalogueAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCatalogueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Catalogue?) {
            item?.let {
                binding.name.text = it.name
                binding.rarity.text = it.rarity
                binding.root.setOnClickListener { v ->
                    onClick(it.name)
                }

                Glide.with(binding.root)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.ic_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCatalogueBinding.inflate(
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