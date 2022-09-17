package com.solacestudios.ribs_demo_android.ribs.catalogue.adapter.CatalogueAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.solacestudios.ribs_demo_android.R
import com.solacestudios.ribs_demo_android.databinding.ItemCatalogueBinding
import com.solacestudios.ribs_demo_android.models.Catalogue

class CatalogueAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CatalogueAdapter.ViewHolder>() {
    private var items: List<Catalogue> = emptyList()
    fun setItems(items: List<Catalogue>){
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCatalogueBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v ->
                onClick(items[adapterPosition].name)
            }
        }

        fun bind(item: Catalogue?) {
            item?.let {
                binding.name.text = it.name
                binding.rarity.text = it.rarity


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