package com.example.sanaseppo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class FoundWordsAdapter(
    private val context: Context,
    private val listOfFoundWords: MutableList<String>) : RecyclerView.Adapter<FoundWordsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewWord: TextView = itemView.findViewById(R.id.found_words_item_word)
        val textViewPoints: TextView = itemView.findViewById(R.id.found_words_item_points)
        fun bind(word: String) {
            textViewPoints.text = getPoints(word.length).toString()
            textViewWord.text = word.uppercase()
        }

        fun getPoints(wordLength: Int): Int {
            return when (wordLength) {
                3 -> 1
                4 -> 3
                5 -> 6
                6 -> 10
                7 -> 15
                8 -> 21
                9 -> 29
                10 -> 40
                11 -> 50
                12 -> 63
                13 -> 77
                14 -> 94
                15 -> 116
                16 -> 129
                17 -> 141
                18 -> 168
                19 -> 190
                else -> if (wordLength >=20) {200} else {0}
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.found_word_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = listOfFoundWords[position]
        holder.bind(word)
    }

    override fun getItemCount(): Int {
        return listOfFoundWords.size
    }
}
