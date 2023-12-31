package com.example.oddsprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class EventsAdapter(private val onClick: (ResponseDataClass) -> Unit) :
    ListAdapter<ResponseDataClass, EventsAdapter.EventViewHolder>(EventDiffCallback) {

    class EventViewHolder(itemview: View, val onClick: (ResponseDataClass) -> Unit) :
        RecyclerView.ViewHolder(itemview) {
        private val eventNameTextView: TextView = itemview.findViewById(R.id.event_name)
        private var currentEvent: ResponseDataClass? = null

        init {
            itemview.setOnClickListener {
                currentEvent?.let {
                    onClick(it)
                }
            }
        }


        fun bind(event: ResponseDataClass){
            currentEvent = event
            val ottelun_nimi = currentEvent?.homeTeam + " vastaan " + currentEvent?.awayTeam
            eventNameTextView.text = ottelun_nimi ?: "data on null"

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kohteet_recyclerview_item, parent, false)
        return EventViewHolder(view, onClick)
    }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int){
        val event = getItem(position)
        holder.bind(event)
    }

}
object EventDiffCallback: DiffUtil.ItemCallback<ResponseDataClass>(){
    override fun areItemsTheSame(oldItem: ResponseDataClass, newItem: ResponseDataClass): Boolean{
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ResponseDataClass,
        newItem: ResponseDataClass
    ): Boolean {
        return oldItem.id == newItem.id
    }
}

