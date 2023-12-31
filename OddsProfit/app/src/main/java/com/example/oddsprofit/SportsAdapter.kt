package com.example.oddsprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*

class SportsAdapter(private val onClick: (SportModel) -> Unit) :
    ListAdapter<SportModel, SportsAdapter.SportsViewHolder>(SportDiffCallBack) {

    class SportsViewHolder(itemview: View, val onClick: (SportModel) -> Unit) :
        RecyclerView.ViewHolder(itemview) {
        private val sportNameTextView: TextView = itemview.findViewById(R.id.sport_name)
        private var currentSport: SportModel? = null
        private var sportDetailsRecyclerView: RecyclerView = itemview.findViewById(R.id.sport_details_recyclerview)

        init {
            itemview.setOnClickListener {
                if (sportDetailsRecyclerView.visibility == View.VISIBLE){
                    sportDetailsRecyclerView.visibility = View.GONE
                }
                currentSport?.let {
                    onClick(it)
                    sportDetailsRecyclerView.visibility = View.VISIBLE
                }
            }
        }


        fun bind(sport: SportModel){
            currentSport = sport
            val sport_name = currentSport!!.sportName
            sportNameTextView.text = sport_name
            sportNameTextView.setOnClickListener {
                if (sportDetailsRecyclerView.visibility == View.VISIBLE){
                    sportDetailsRecyclerView.visibility = View.GONE
                }else{

                }
            }

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sports_recyclerview_item, parent, false)
        return SportsViewHolder(view, onClick)
    }
    override fun onBindViewHolder(holder: SportsViewHolder, position: Int){
        val sport = getItem(position)
        holder.bind(sport)
    }

}
object SportDiffCallBack: DiffUtil.ItemCallback<SportModel>(){
    override fun areItemsTheSame(oldItem: SportModel, newItem: SportModel): Boolean{
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: SportModel,
        newItem: SportModel
    ): Boolean {
        return oldItem.sportName == newItem.sportName
    }
}


//class EventDetailsAdapter(private val eventDetailsList: List<String>) :
//    RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder>() {
//
//    class EventDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val eventDetailsTextView: TextView = itemView.findViewById(R.id.liiga_name)
//
//        fun bind(eventDetails: String){
//            Log.d("KOKEILU", "bind callattiin, arvo on $eventDetails")
//            eventDetailsTextView.text = eventDetails
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailsViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_details_recyclerview_item, parent, false)
//        return EventDetailsViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: EventDetailsViewHolder, position: Int) {
//        val eventDetails = eventDetailsList[position]
//        holder.bind(eventDetails)
//    }
//
//    override fun getItemCount(): Int {
//        return eventDetailsList.size
//    }
//}
//

