package com.masaibar.roomflowablesample

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.view_item_time.view.*

class TimeAdapter(
        context: Context,
        private var times: List<Time>
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    fun update(
            updated: List<Time>
    ) {
        if (times == updated) {
            return
        }

        val result = DiffUtil.calculateDiff(
                TimeDiffCallback(
                        times,
                        updated
                )
        )
        times = updated
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val itemView = inflater.inflate(R.layout.view_item_time, parent, false)
        return TimeViewHolder(itemView)
    }

    override fun getItemCount(): Int =
            times.size

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.textTime.text = times[position].toString()
    }

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTime :TextView = itemView.text_time
    }
}