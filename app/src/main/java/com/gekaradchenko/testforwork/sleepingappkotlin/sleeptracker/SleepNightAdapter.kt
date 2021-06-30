package com.gekaradchenko.testforwork.sleepingappkotlin.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gekaradchenko.testforwork.sleepingappkotlin.Unit
import com.gekaradchenko.testforwork.sleepingappkotlin.database.SleepNight
import com.gekaradchenko.testforwork.sleepingappkotlin.databinding.ListItemSleepNightBinding

class SleepNightAdapter(val clickListener: SleepNightListener) :
    ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position)!!, clickListener)
    }


    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ListItemSleepNightBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return ViewHolder(binding)
            }
        }
    }


    class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem == newItem
        }
    }

}

class SleepNightListener(val clickListener: (sleepId: Long) -> kotlin.Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

//sealed class DataItem() {
//    data class SleepNightItem(val sleepNight: SleepNight) : DataItem(){
//        override val id = sleepNight.nightId
//    }
//    object Header : DataItem() {
//        override val id = Long.MAX_VALUE
//    }
//
//    abstract val id: Long
//}