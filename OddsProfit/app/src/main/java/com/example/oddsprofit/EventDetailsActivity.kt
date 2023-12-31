package com.example.oddsprofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventDetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventData = intent.getParcelableExtra<ResponseDataClass>("EVENT_DATA")
        if (eventData != null) {
            setValues(eventData)
        }

    }


    fun setValues(eventData: ResponseDataClass) {
        var selectedMarket = "h2h"
        var selectedSubMarket = ""

        val textViewMainTitle = findViewById<TextView>(R.id.titleTextView)
        val textViewSubtitle1 = findViewById<TextView>(R.id.subtitle1TextView)
        val textViewSubtitle2 = findViewById<TextView>(R.id.subtitle2TextView)
        val textViewSubtitle3 = findViewById<TextView>(R.id.subtitle3TextView)
        val radioButtonOption1 = findViewById<RadioButton>(R.id.option1)
        val radioButtonOption2 = findViewById<RadioButton>(R.id.option2)
        val radioButtonOption3 = findViewById<RadioButton>(R.id.option3)
        val radioButtonOption4 = findViewById<RadioButton>(R.id.option4)
        val radioButtonOption5 = findViewById<RadioButton>(R.id.option5)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        val radioButtonOption21 = findViewById<RadioButton>(R.id.option21)
        val radioButtonOption22 = findViewById<RadioButton>(R.id.option22)
        val radioButtonOption23 = findViewById<RadioButton>(R.id.option23)
        val radioButtonOption24 = findViewById<RadioButton>(R.id.option24)
        val radioButtonOption25 = findViewById<RadioButton>(R.id.option25)

        val radioGroup2 = findViewById<RadioGroup>(R.id.radioGroup2)
        val scrollViewSubMarkets = findViewById<HorizontalScrollView>(R.id.scrollViewSubMarket)

        val otsikko = eventData.homeTeam + " vastaan " + eventData.awayTeam
        textViewMainTitle.text = otsikko
        textViewSubtitle1.text = eventData.sportTitle
        val timeString = timeFormatter(eventData.commenceTime!!, "dd.MM. HH:mm")
        textViewSubtitle2.text = "Ottelun aloitusaika: $timeString"
        textViewSubtitle3.text = "Parhaat kertoimet 1x2 markkinaan: "
        val listaH2Hkertoimista =
            getMapOfBookmakersAndPricesForAnEventType(eventData.bookmakers, "h2h")
        val parhaatkertoimet = getParhaatKertoimet(listaH2Hkertoimista)
        setMainEventKertoimetTextViews(
            parhaatkertoimet,
            mutableListOf(eventData.homeTeam!!, "Draw", eventData.awayTeam!!)
        )
        val allMarkets = getAllMarkets(eventData.bookmakers)
        for (market in allMarkets) {
            val index = allMarkets.entries.indexOfFirst { it.key == market.key }
            when (index) {
                0 -> {
                    radioButtonOption1.visibility = View.VISIBLE
                    radioButtonOption1.text = market.key
                }
                1 -> {
                    radioButtonOption2.visibility = View.VISIBLE
                    radioButtonOption2.text = market.key
                }
                2 -> {
                    radioButtonOption3.visibility = View.VISIBLE
                    radioButtonOption3.text = market.key
                }
                3 -> {
                    radioButtonOption4.visibility = View.VISIBLE
                    radioButtonOption4.text = market.key
                }
                4 -> {
                    radioButtonOption5.visibility = View.VISIBLE
                    radioButtonOption5.text = market.key
                }
            }

        }
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            hideAllSubMarketRadioButtons(listOf(
                findViewById<RadioButton>(R.id.option21),
                findViewById<RadioButton>(R.id.option22),
                findViewById<RadioButton>(R.id.option23),
                findViewById<RadioButton>(R.id.option24),
                findViewById<RadioButton>(R.id.option25)
            )
            )
            when (i) {
                R.id.option1 -> {
                    selectedMarket = radioButtonOption1.text.toString()

                }
                R.id.option2 -> {
                    selectedMarket = radioButtonOption2.text.toString()

                }
                R.id.option3 -> {
                    selectedMarket = radioButtonOption3.text.toString()

                }
                R.id.option4 -> {
                    selectedMarket = radioButtonOption4.text.toString()

                }
                R.id.option5 -> {
                    selectedMarket = radioButtonOption5.text.toString()

                }
            }
            if (allMarkets[selectedMarket]?.size!! > 0) {
                selectedSubMarket = allMarkets[selectedMarket]?.first().toString()
                scrollViewSubMarkets.visibility = View.VISIBLE
                for (subMarket in allMarkets[selectedMarket]!!) {
                    when (allMarkets[selectedMarket]!!.indexOf(subMarket)) {
                        0 -> {
                            radioButtonOption21.visibility = View.VISIBLE
                            radioButtonOption21.text = subMarket
                        }
                        1 -> {
                            radioButtonOption22.visibility = View.VISIBLE
                            radioButtonOption22.text = subMarket
                        }
                        2 -> {
                            radioButtonOption23.visibility = View.VISIBLE
                            radioButtonOption23.text = subMarket
                        }
                        3 -> {
                            radioButtonOption24.visibility = View.VISIBLE
                            radioButtonOption24.text = subMarket
                        }
                        4 -> {
                            radioButtonOption25.visibility = View.VISIBLE
                            radioButtonOption25.text = subMarket
                        }
                    }
                }
            }
            else{
                selectedSubMarket = ""
                scrollViewSubMarkets.visibility = View.GONE
            }

            setUpRecyclerView(eventData.bookmakers, selectedMarket, selectedSubMarket)
        }
        setUpRecyclerView(eventData.bookmakers, "h2h", "")

        radioGroup2.setOnCheckedChangeListener { radioGroup2, j ->
            when (j) {
                R.id.option21 -> {
                    selectedSubMarket = radioButtonOption21.text.toString()

                }
                R.id.option22 -> {
                    selectedSubMarket = radioButtonOption22.text.toString()

                }
                R.id.option23 -> {
                    selectedSubMarket = radioButtonOption23.text.toString()

                }
                R.id.option24 -> {
                    selectedSubMarket = radioButtonOption24.text.toString()

                }
                R.id.option25 -> {
                    selectedSubMarket = radioButtonOption25.text.toString()

                }
            }
            setUpRecyclerView(eventData.bookmakers, selectedMarket, selectedSubMarket)

        }
    }
    fun hideAllSubMarketRadioButtons(listOfRadioButtons: List<RadioButton>) {
        for (radioButton in listOfRadioButtons){
            radioButton.visibility = View.GONE
        }

    }

    fun getAllMarkets(listOfBookmakers: List<Bookmakers>): MutableMap<String, MutableList<String>> {
        val allMarkets: MutableMap<String, MutableList<String>> = mutableMapOf()
        for (bookmaker in listOfBookmakers) {
            for (market in bookmaker.markets) {
                when (market.key!!) {
                    "totals", "spreads" -> {
                        Log.d("TESTI", "Tässä tulee kaikki ")
                        val point = market.outcomes.first().point
                        if (market.key !in allMarkets.keys) {
                            allMarkets[market.key!!] = mutableListOf()
                            allMarkets[market.key!!]?.add(point.toString())
                        } else {
                            allMarkets[market.key!!]?.add(point.toString())
                        }
                    }
                    else -> {
                        if (market.key !in allMarkets.keys) {
                            allMarkets[market.key!!] = mutableListOf()
                        }
                    }
                }
            }
        }
        return allMarkets
    }

    fun setUpRecyclerView(listOfBookmakers: List<Bookmakers>, desiredMarket: String, desiredSubMarket: String) {

        val allBookmakersAdapter =
            AllBookmakersAdapter(desiredMarket, desiredSubMarket) { bookmaker -> adapterOnClick(bookmaker) }
        val concatAdapter = ConcatAdapter(allBookmakersAdapter)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerview_all_bookmakers)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        allBookmakersAdapter.submitList(listOfBookmakers)
        concatAdapter.notifyDataSetChanged()
    }

    private fun adapterOnClick(bookmaker: Bookmakers) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun timeFormatter(timeString: String, desiredFormat: String): String {
        val inputFormat = DateTimeFormatter.ISO_DATE_TIME
        val outputFormat = DateTimeFormatter.ofPattern(desiredFormat)
        val dateTime = LocalDateTime.parse(timeString, inputFormat)
        return dateTime.format(outputFormat)
    }

    private fun setMainEventKertoimetTextViews(
        parhaatkertoimet: MutableMap<String, MutableMap<Double, String>>,
        eventOutComesList: List<String>
    ) {
        val textViewHorizontal1 = findViewById<TextView>(R.id.textView1)
        val textViewHorizontal2 = findViewById<TextView>(R.id.textView2)
        val textViewHorizontal3 = findViewById<TextView>(R.id.textView3)
        val textViewHorizontalBottom1 = findViewById<TextView>(R.id.textViewBottom1)
        val textViewHorizontalBottom2 = findViewById<TextView>(R.id.textViewBottom2)
        val textViewHorizontalBottom3 = findViewById<TextView>(R.id.textViewBottom3)


        for ((event, priceAndBookmakerMap) in parhaatkertoimet.entries) {
            for (outcome in eventOutComesList) {
                if (outcome == event) {
                    when (eventOutComesList.indexOf(outcome)) {
                        0 -> {
                            textViewHorizontal1.text =
                                priceAndBookmakerMap.keys.first().toString()
                            textViewHorizontalBottom1.text =
                                priceAndBookmakerMap[priceAndBookmakerMap.keys.first()]
                        }
                        1 -> {
                            textViewHorizontal2.text =
                                priceAndBookmakerMap.keys.first().toString()
                            textViewHorizontalBottom2.text =
                                priceAndBookmakerMap[priceAndBookmakerMap.keys.first()]
                        }
                        2 -> {
                            textViewHorizontal3.text =
                                priceAndBookmakerMap.keys.first().toString()
                            textViewHorizontalBottom3.text =
                                priceAndBookmakerMap[priceAndBookmakerMap.keys.first()]
                        }

                    }
                }
            }
        }

    }

    fun getParhaatKertoimet(kaikkikertoimet: MutableMap<String, MutableMap<String, Double>>): MutableMap<String, MutableMap<Double, String>> {
        val resultMap: MutableMap<String, MutableMap<Double, String>> = mutableMapOf()

        for ((bookmaker, oddsMap) in kaikkikertoimet) {
            for ((eventName, price) in oddsMap) {
                if (!resultMap.containsKey(eventName)) {
                    resultMap[eventName] = mutableMapOf(Pair(price, bookmaker))
                } else {
                    val nestedMap = resultMap[eventName]!!
                    if (!nestedMap.containsKey(price) || nestedMap[price]!!.length > bookmaker.length) {
                        nestedMap[price] = bookmaker
                    }
                }
            }
        }
        var parhaatKertoimet: MutableMap<String, MutableMap<Double, String>> = mutableMapOf()
        resultMap.forEach { (event, oddsMap) ->
            oddsMap.forEach { (price, bookmaker) ->
                run {
                    if (!parhaatKertoimet.containsKey(event)) {
                        parhaatKertoimet[event] = mutableMapOf(Pair(price, bookmaker))
                    } else {
                        var currentBestPrice = parhaatKertoimet[event]!!.keys.first()
                        if (price > currentBestPrice) {
                            parhaatKertoimet[event] = mutableMapOf(Pair(price, bookmaker))
                        }
                    }
                }

            }
        }
        return parhaatKertoimet
    }

    fun getMapOfBookmakersAndPricesForAnEventType(
        event_bookmakers: List<Bookmakers>,
        market: String
    ): MutableMap<String, MutableMap<String, Double>> {
        val mapOfOdds: MutableMap<String, MutableMap<String, Double>> = mutableMapOf()
        for (bookmaker in event_bookmakers) {
            var foundMatchingMarket = false
            for (marketti in bookmaker.markets) {
                if (marketti.key == market) {
                    foundMatchingMarket = true
                    mapOfOdds[bookmaker.title!!] = mutableMapOf()
                    for (outcome in marketti.outcomes) {
                        mapOfOdds[bookmaker.title]!![outcome.name!!] = outcome.price!!
                    }
                    break
                }
            }
            if (foundMatchingMarket) {
                continue
            }
        }
        //for ((bookmaker, oddsMap) in mapOfOdds) {
        //    Log.d("TESTI", "Vedonvälittäjä on: $bookmaker ja kertoimet ovat: ")
        //    for ((outcome, price) in oddsMap) {
        //        Log.d("TESTI", "$outcome: $price")
        //    }
        //}
        return mapOfOdds
    }
}