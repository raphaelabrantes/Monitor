package dev.abrantes.monitor.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.abrantes.monitor.databinding.GridInfoItemBinding
import dev.abrantes.monitor.infrastructure.Response

class MoreInfoAdapter :
    ListAdapter<Response, MoreInfoAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Response>() {
        override fun areItemsTheSame(oldItem: Response, newItem: Response): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Response, newItem: Response): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(
        private var binding: GridInfoItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(response: Response) {
            binding.statusInfo.text = response.status.toString()
            binding.datRequest.text = response.dat
            binding.timeRequest.text = response.requestTime.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GridInfoItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}