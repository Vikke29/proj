package com.example.oddsprofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oddsprofit.R.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

const val EVENT_ID = "event id"

class MainActivity : AppCompatActivity() {
    private val newEventActivityRequestCode = 1

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        fetchExampleData()

        getAllLigas("https://api.the-odds-api.com/v4/sports/?apiKey=b270efc4090b79eca5cca46498dcf056")
        Log.d("TESTI", "onCreate callattiin nyt")

        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        val evCalculatorTextView: TextView = drawerLayout.findViewById(R.id.text_view_calculator)
        evCalculatorTextView.setOnClickListener {
            val intent = Intent(this, EvCalculatorActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    fun setUpNavigationDrawerRecyclerView(listOfAllSports: List<SportModel>){
        val sportsAdapter = SportsAdapter { sport -> secondAdapterOnClick(sport)}
        val concatAdapter = ConcatAdapter(sportsAdapter)

        val recyclerView: RecyclerView = findViewById(R.id.navigationDrawer_recyclerview)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        sportsAdapter.submitList(listOfAllSports)
        concatAdapter.notifyDataSetChanged()
    }

    private fun secondAdapterOnClick(sport: SportModel) {
        val detailsAdapter = DetailsAdapter(sport.listOfLeagues!!.toList())
        val concatAdapter = ConcatAdapter(detailsAdapter)
        val recyclerView: RecyclerView = findViewById(R.id.sport_details_recyclerview)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        detailsAdapter.submitList(sport.listOfLeagues)
    }


    fun setUpRecyclerView(listOfEvents: List<ResponseDataClass>){

        val eventsAdapter = EventsAdapter {event -> adapterOnClick(event)}
        val concatAdapter = ConcatAdapter(eventsAdapter)


        val recyclerView: RecyclerView = findViewById(R.id.events_recyclerview)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventsAdapter.submitList(listOfEvents)
        concatAdapter.notifyDataSetChanged()
    }
    private fun getAllSports(allLeagues: List<LeaguesResponseDataClass>) {
        val listOfAllSports: MutableList<SportModel> = mutableListOf()
        for (liiga in allLeagues){
            val index = listOfAllSports.indexOfFirst { it.sportName == liiga.group }
            if (index != -1) {
                // if a matching SportModel is found, add liiga to its listOfLeagues
                listOfAllSports[index].listOfLeagues!!.add(liiga)
            } else {
                val newSportModel = SportModel(sportName = liiga.group, listOfLeagues = mutableListOf(liiga), false)
                listOfAllSports.add(newSportModel)
            }

        }
        setUpNavigationDrawerRecyclerView(listOfAllSports)


    }

    private fun getAllLigas(osoite: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                launch(Dispatchers.IO) {
                    try {
                        val url =
                            URL(osoite)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"

                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            Log.d("TESTI", "RESPONSECODE ON OK")
                            val inputStream = connection.inputStream
                            val inputStreamReader = InputStreamReader(inputStream)
                            val bufferedReader = BufferedReader(inputStreamReader)
                            val stringBuilder = StringBuilder()

                            var line: String?
                            while (bufferedReader.readLine().also { line = it } != null) {
                                stringBuilder.append(line)
                            }

                            val json = stringBuilder.toString()

                            // update the UI with the JSON data
                            launch(Dispatchers.Main) {
                                val responseData = Gson().fromJson(json, Array<LeaguesResponseDataClass>::class.java).toList()

                                organiseLiigatJsonData(json)
                            }
                        } else {
                        }

                        connection.disconnect()
                    } catch (e: Exception) {
                        println("Error: ${e.message}")
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }


    private fun fetchExampleData(){
        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                launch(Dispatchers.IO) {
                    try {
                        val url =
                            URL("https://api.the-odds-api.com/v4/sports/soccer_uefa_europa_league/odds/?apiKey=b270efc4090b79eca5cca46498dcf056&regions=eu&&markets=h2h,spreads,totals&oddsFormat=decimal")
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"

                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            Log.d("TESTI", "RESPONSECODE ON OK")
                            val inputStream = connection.inputStream
                            val inputStreamReader = InputStreamReader(inputStream)
                            val bufferedReader = BufferedReader(inputStreamReader)
                            val stringBuilder = StringBuilder()

                            var line: String?
                            while (bufferedReader.readLine().also { line = it } != null) {
                                stringBuilder.append(line)
                            }

                            val json = stringBuilder.toString()

                            // update the UI with the JSON data
                            launch(Dispatchers.Main) {
                                organiseJsonData(json)
                            }
                        } else {
                        }

                        connection.disconnect()
                    } catch (e: Exception) {
                        println("Error: ${e.message}")
                    }
                }
            }
        }

    }
    fun organiseLiigatJsonData(json: String) {
        val responseData = Gson().fromJson(json, Array<LeaguesResponseDataClass>::class.java).toList()
        for (liiga in responseData){
            Log.d("KOKEILU", responseData.size.toString())
        }
        getAllSports(responseData)
    }

    fun organiseJsonData(json: String){
        val responseData = Gson().fromJson(json, Array<ResponseDataClass>::class.java).toList()
        Log.d("TESTI", responseData[0].homeTeam!!)
        setUpRecyclerView(responseData)
        Log.d("TESTI", "responseData size: ${responseData.size}")
        responseData.forEach { response -> Log.d("TESTI", response.homeTeam ?: "null") }

    }

    private fun adapterOnClick(event: ResponseDataClass) {
        val intent = Intent(this, EventDetailsActivity::class.java)
        intent.putExtra("EVENT_DATA", event)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}
