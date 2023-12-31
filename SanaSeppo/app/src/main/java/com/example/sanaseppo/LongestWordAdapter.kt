package com.example.sanaseppo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class LongestWordAdapter(
    private val context: Context,
    private val longestWord: String
) : RecyclerView.Adapter<LongestWordAdapter.ViewHolder>() {
    private val revealedLetters = BooleanArray(longestWord.length)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val longestWordLetterButton: Button = itemView.findViewById(R.id.longest_word_letter_button)

        fun bind(position: Int) {
            val letter = longestWord[position]
            if (revealedLetters[position]) {
                longestWordLetterButton.text = letter.toString()
            } else {
                longestWordLetterButton.text = "-"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.longest_word_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return longestWord.length
    }

    fun showHint() {
        // Paljasta yksi satunnainen kirjain
        val hiddenIndices = revealedLetters.indices.filter { !revealedLetters[it] }
        if (hiddenIndices.isNotEmpty()) {
            val indexToReveal = hiddenIndices.random()
            revealedLetters[indexToReveal] = true
            notifyItemChanged(indexToReveal)
        }
    }
}
