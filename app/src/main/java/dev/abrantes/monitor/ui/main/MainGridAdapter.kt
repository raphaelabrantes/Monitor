package dev.abrantes.monitor.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.abrantes.monitor.databinding.GridItemBinding
import dev.abrantes.monitor.infrastructure.RegisterUrl

class MainGridAdapter(private val handler: (register: RegisterUrl) -> Unit) : ListAdapter<RegisterUrl, MainGridAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<RegisterUrl>() {
        override fun areItemsTheSame(oldItem: RegisterUrl, newItem: RegisterUrl): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RegisterUrl, newItem: RegisterUrl): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(private var binding: GridItemBinding, private var handler: (register: RegisterUrl) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(registerUrl: RegisterUrl) {
            var text = registerUrl.uri
            if(text.length > 29) text = text.replaceRange(29 until text.length, "...")
            binding.registerUriText.text = text
            binding.repeat.text = registerUrl.repeat.toString().lowercase().replace("_", " ")
            binding.deleteButton.setOnClickListener {
                handler(registerUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GridItemBinding.inflate(LayoutInflater.from(parent.context)), handler)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}