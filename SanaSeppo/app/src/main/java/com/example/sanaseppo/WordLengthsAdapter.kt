package com.example.sanaseppo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordLengthsAdapter(
    private val context: Context,
    private val listOfWordLengths: List<Pair<Int, Int>>
    ) : RecyclerView.Adapter<WordLengthsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val wordLengthTextView: TextView = itemView.findViewById(R.id.word_lengths_text_view)

        fun bind(position: Int){

            val length = listOfWordLengths[position].first
            val amount = listOfWordLengths[position].second
            wordLengthTextView.text = "$length-kirjain: $amount"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordLengthsAdapter.ViewHolder {
        for (item in listOfWordLengths){
            Log.d("PITUUS", "${item.first.toString()} on ${item.second} kpl")
        }
        val view = LayoutInflater.from(context).inflate(R.layout.word_lengths_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordLengthsAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listOfWordLengths.size
    }

}
