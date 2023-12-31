package com.example.oddsprofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class EvCalculatorActivity : AppCompatActivity() {

    private lateinit var itemList: ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExchangeOddRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ev_calculator)

        title = "Ev-laskuri"

        val calculateButton: Button = findViewById(R.id.button_calculate)

        calculateButton.setOnClickListener {
            //laskeEstimatedValue()
        }
        setUpRecyclerView()
    }

    fun setUpRecyclerView() {

        itemList = ArrayList()
        itemList.add("Item 1")
        itemList.add("Item 2")
        itemList.add("Item 3")

        recyclerView = findViewById(R.id.exchangeOddItemRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ExchangeOddRecyclerViewAdapter(itemList)
        recyclerView.adapter = adapter

        // addButton.setOnClickListener {
        //     itemList.add("Item ${itemList.size + 1}")
        //     adapter.notifyItemInserted(itemList.size - 1)
//
        // }
    }

    //fun laskeEstimatedValue() {
    //    val exchangeOdds1TextInput: TextInputEditText = findViewById(R.id.exhangeOdd1)
    //    val exchangeOdds2TextInput: TextInputEditText = findViewById(R.id.exhangeOdd2)
    //    val eventOdds1: TextInputEditText = findViewById(R.id.event_odd)
    //    val stakeTextInput: TextInputEditText = findViewById(R.id.bet_amount)
    //    val cashBackPercentageTextInput: TextInputEditText =
    //        findViewById(R.id.cashBackPercentage)
    //    val cashBackTurnOverTextInput: TextInputEditText = findViewById(R.id.cashBackTurnOver)
    //    val evTextView: TextView = findViewById(R.id.textview_profit)
    //    val percentageProfitTextView: TextView = findViewById(R.id.textView_profit_percentage)
    //    val cashBackTurnOverReturnRateTextInput: TextInputEditText =
    //        findViewById(R.id.cashBackTurnOverReturnRate)
    //    val cashBackOnlyIfQualifyingBetLossesCheckBox: CheckBox =
    //        findViewById(R.id.cashBackOnlyIfQualifyingBetLossesCheckBox)
    //    val exchangeOddsList: MutableList<Float> =
    //        mutableListOf(exchangeOdds1TextInput.text.toString().toFloat())
    //    exchangeOddsList.add(exchangeOdds2TextInput.text.toString().toFloat())
    //    val eventProbability = getEventProbability(exchangeOddsList)
    //    val eventOdd = eventOdds1.text.toString().toFloat()
    //    val qualifyingBetReturnRate = eventOdd * eventProbability
    //    val stake = stakeTextInput.text.toString().toFloat()
    //    val qualifyingBetFinalProfit = stake - stake * qualifyingBetReturnRate
    //    var cashbackProfit = 0.0F
    //    val cashBackPercentage = cashBackPercentageTextInput.text.toString().toFloat()
    //    val cashBackTurnOver = cashBackTurnOverTextInput.text.toString().toFloat()
    //    val cashBackTurnOverReturnRate =
    //        cashBackTurnOverReturnRateTextInput.text.toString().toFloat()
    //    if (cashBackOnlyIfQualifyingBetLossesCheckBox.isChecked) {
    //        cashbackProfit =
    //            stake * (cashBackPercentage / 100) - (stake * (cashBackPercentage / 100) * cashBackTurnOver * (1 - (cashBackTurnOverReturnRate / 100)))
    //        cashbackProfit =
    //            eventProbability * (eventOdd - 1) * stake + ((1 - eventProbability) * (-stake + cashbackProfit))
//
    //        evTextView.text = cashbackProfit.toString() + "€"
//
    //        val roundedFloat = String.format("%.2f", ((cashbackProfit / stake) * 100))
    //        percentageProfitTextView.text = roundedFloat
//
    //    } else {
    //        cashbackProfit =
    //            stake * (cashBackPercentage / 100) - (stake * (cashBackPercentage / 100) * cashBackTurnOver * (1 - (cashBackTurnOverReturnRate / 100)))
//
    //        evTextView.text = (cashbackProfit - qualifyingBetFinalProfit).toString() + "€"
//
    //        val roundedFloat = String.format(
    //            "%.2f",
    //            ((cashbackProfit - qualifyingBetFinalProfit) / stake) * 100
    //        )
    //        percentageProfitTextView.text = roundedFloat
//
    //    }
//
//
    //}
//
    fun getEventProbability(listOfExchangeOdds: List<Float>): Float {

        var sum: Float = 0.0F
        for (exchangeOdd in listOfExchangeOdds) {
            sum += 1 / exchangeOdd
        }
        val eventProbability = 1 / listOfExchangeOdds[0] / sum
        return eventProbability
    }
}