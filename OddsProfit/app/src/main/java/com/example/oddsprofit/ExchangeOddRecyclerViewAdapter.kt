package com.example.oddsprofit

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ExchangeOddRecyclerViewAdapter(private val itemList: ArrayList<String>) :
    RecyclerView.Adapter<ExchangeOddRecyclerViewAdapter.ExchangeOddViewHolder>() {

    private val itemInputData = mutableMapOf<Int, String>()

    class ExchangeOddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textInputLayout: TextInputLayout = itemView.findViewById(R.id.exchangeOddItemLayout)
        val textInputeEditText: TextInputEditText = itemView.findViewById(R.id.exchangeOddItem)
        val deleteButton: FloatingActionButton = itemView.findViewById(R.id.fab_delete_item)
        val addButton: FloatingActionButton = itemView.findViewById(R.id.fab_add_another_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeOddViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.calculator_event_item, parent, false)
        return ExchangeOddViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExchangeOddViewHolder, position: Int) {
        if (position == itemList.size - 1) {
            holder.addButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE
        } else {
            holder.addButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }

        holder.textInputLayout.hint = "Item ${position + 1}"
        if (itemInputData.containsKey(position)) {
            holder.textInputLayout.editText?.setText(itemInputData[position])
        }

        holder.deleteButton.setOnClickListener {
            removeAt(position)
        }

        holder.addButton.setOnClickListener {
            holder.addButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
            addAt()


        }

        holder.textInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            val itemPosition = holder.adapterPosition
            if (itemPosition != RecyclerView.NO_POSITION) {
                Log.d("TESTI", "iteminputdata kohdassa $itemPosition on ${text.toString()}")
                itemInputData[itemPosition] = text.toString()
            }
        }
    }
    fun addAt(){
        itemList.add("Item ${itemList.size + 1}")
        itemInputData[itemList.size - 1] = ""
        notifyItemInserted(itemList.size - 1)

    }
    fun removeAt(position: Int) {

        Log.d("TESTI", "position on$position ja listan pituus on ${itemList.size}")
        itemInputData.remove(itemList.size - 1)
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position - 1)
    }


    override fun getItemCount(): Int {
        return itemList.size
    }
}
