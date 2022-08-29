package dev.abrantes.monitor.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.abrantes.monitor.databinding.GridItemBinding
import dev.abrantes.monitor.infrastructure.RegisterUrl

class MainGridAdapter : ListAdapter<RegisterUrl, MainGridAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<RegisterUrl>() {
        override fun areItemsTheSame(oldItem: RegisterUrl, newItem: RegisterUrl): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RegisterUrl, newItem: RegisterUrl): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(private var binding: GridItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(registerUrl: RegisterUrl) {
            binding.registerUriText.text = registerUrl.uri
            binding.repeat.text = registerUrl.repeat.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GridItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}