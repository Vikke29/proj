package com.example.oddsprofit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class DetailsAdapter(private val listOfLeagues: List<LeaguesResponseDataClass>) :
    ListAdapter<LeaguesResponseDataClass, DetailsAdapter.DetailsViewHolder>(SportDetailsDiffCallBack) {

    class DetailsViewHolder(itemview: View) :
        RecyclerView.ViewHolder(itemview) {
        private val leagueNameTextView: TextView = itemview.findViewById(R.id.liiga_name)
        private var currentLeague: LeaguesResponseDataClass? = null

        init {
            itemview.setOnClickListener {
                Log.d("TESTI", "klikattu!")
            }
        }


        fun bind(league: LeaguesResponseDataClass){
            currentLeague = league
            val league_name = currentLeague!!.title
            leagueNameTextView.text = league_name

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sport_details_recyclerview_item, parent, false)
        return DetailsViewHolder(view)
    }
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int){
        val league = listOfLeagues[position]
        holder.bind(league)
    }

}
object SportDetailsDiffCallBack: DiffUtil.ItemCallback<LeaguesResponseDataClass>(){
    override fun areItemsTheSame(oldItem: LeaguesResponseDataClass, newItem: LeaguesResponseDataClass): Boolean{
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: LeaguesResponseDataClass,
        newItem: LeaguesResponseDataClass
    ): Boolean {
        return oldItem.key == newItem.key
    }
}