package com.example.oddsprofit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class AllBookmakersAdapter(
    private val desiredMarket: String,
    private val desiredSubMarket: String,
    private val onClick: (Bookmakers) -> Unit
) :
    ListAdapter<Bookmakers, AllBookmakersAdapter.AllBookmakersViewHolder>(EventDiffCallback2) {

    class AllBookmakersViewHolder(
        itemview: View,
        val onClick: (Bookmakers) -> Unit,
        val desiredMarket: String,
        val desiredSubMarket: String
    ) :
        RecyclerView.ViewHolder(itemview) {
        private val odds1TextView: TextView = itemview.findViewById(R.id.odds1)
        private val odds2TextView: TextView = itemview.findViewById(R.id.odds2)
        private val odds3TextView: TextView = itemview.findViewById(R.id.odds3)
        private val bookmakerNameTextView: TextView = itemview.findViewById(R.id.bookmaker_name)
        private var currentBookmaker: Bookmakers? = null

        init {
            itemview.setOnClickListener {
                currentBookmaker?.let {
                    onClick(it)
                }
            }
        }

        fun bind(bookmaker: Bookmakers, markkina: String, subMarket: String) {
            var desiredMarket = markkina
            currentBookmaker = bookmaker
            val bookmakerin_nimi = currentBookmaker!!.title
            bookmakerNameTextView.text = bookmakerin_nimi

            val marketIndex = bookmaker.markets.indexOfFirst { it.key == desiredMarket }
            if (marketIndex != -1) {
                val market = bookmaker.markets[marketIndex]
                for (outcome in market.outcomes) {
                    Log.d("TESTI", "valittu Submarket on $subMarket")
                    if (desiredMarket == ""){
                        when (market.outcomes.indexOf(outcome)) {
                            0 -> {
                                odds1TextView.text = outcome.price.toString()
                                odds1TextView.visibility = View.VISIBLE
                            }
                            1 -> {
                                odds3TextView.text = outcome.price.toString()
                                odds3TextView.visibility = View.VISIBLE
                            }
                            2 -> {
                                odds2TextView.text = outcome.price.toString()
                                odds2TextView.visibility = View.VISIBLE
                            }
                        }
                    }else{
                        Log.d("TESTI", "${market.outcomes[0].point} katsotaan sisältääkö se $subMarket kun bookkerina on ${bookmaker.title}")
                        if (market.outcomes[0].point.toString().contains(subMarket)){
                            Log.d("TESTI", "Sisältää")
                            Log.d("TESTI", "${market.outcomes.indexOf(outcome)} on saatu numero")
                            when (market.outcomes.indexOf(outcome)) {
                                0 -> {
                                    odds1TextView.text = outcome.price.toString()
                                    odds1TextView.visibility = View.VISIBLE
                                }
                                1 -> {
                                    odds3TextView.text = outcome.price.toString()
                                    odds3TextView.visibility = View.VISIBLE
                                }
                                2 -> {
                                    odds2TextView.text = outcome.price.toString()
                                    odds2TextView.visibility = View.VISIBLE
                                }
                            }
                        }else{
                            Log.d("TESTI", "Ei sisällä")
                            itemView.visibility = View.GONE
                            itemView.layoutParams = RecyclerView.LayoutParams(0, 0)

                        }
                    }
                }
            } else {
                itemView.visibility = View.GONE
                itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }

            //for (market in bookmaker.markets){
            //    if (market.key == desiredMarket){
            //        for (outcome in market.outcomes){
            //            when (market.outcomes.indexOf(outcome)){
            //                0 ->{
            //                    odds1TextView.text = outcome.price.toString()
            //                    odds1TextView.visibility = View.VISIBLE
            //                }
            //                1 ->{
            //                    odds3TextView.text = outcome.price.toString()
            //                    odds2TextView.visibility = View.VISIBLE
            //                }
            //                2 ->{
            //                    odds2TextView.text = outcome.price.toString()
            //                    odds3TextView.visibility = View.VISIBLE
            //                }
            //            }
            //        }
            //    }
            //}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllBookmakersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_bookmakers_recyclerview_item, parent, false)
        return AllBookmakersViewHolder(view, onClick, "h2h", "")
    }

    override fun onBindViewHolder(holder: AllBookmakersViewHolder, position: Int) {
        val bookmaker = getItem(position)
        holder.bind(bookmaker, desiredMarket, desiredSubMarket)
    }

}

object EventDiffCallback2 : DiffUtil.ItemCallback<Bookmakers>() {
    override fun areItemsTheSame(oldItem: Bookmakers, newItem: Bookmakers): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Bookmakers,
        newItem: Bookmakers
    ): Boolean {
        return oldItem.key == newItem.key
    }
}

