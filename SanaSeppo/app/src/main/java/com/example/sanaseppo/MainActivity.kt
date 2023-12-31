package com.example.sanaseppo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class MainActivity : AppCompatActivity() {

    var currentCarouselPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setUpCarousel()
        val startButton: Button = findViewById(R.id.button_start)

        startButton.setOnClickListener {
            val intent = Intent(this, BoggleActivity::class.java)
            intent.putExtra(Constants.GAME_MODE, currentCarouselPosition)
            startActivity(intent)
        }
    }

    private fun setUpCarousel() {
        val carousel: ImageCarousel = findViewById(R.id.carousel)

        carousel.registerLifecycle(lifecycle)

        val list = mutableListOf<CarouselItem>()

        list.add(
            CarouselItem(
                imageDrawable = R.drawable.traditional_mode_image,
                caption = resources.getString(R.string.perinteinen)
            )
        )
        list.add(
            CarouselItem(
                imageDrawable = R.drawable.longest_hunt_mode_image,
                caption = resources.getString(R.string.pesin_sana)
            )
        )
        carousel.setData(list)

        val listOfMissionBriefingTexts = mutableListOf<String>()
        listOfMissionBriefingTexts.add(resources.getString(R.string.mission_briefing_standard))
        listOfMissionBriefingTexts.add(resources.getString(R.string.mission_briefing_longest_word))

        carousel.onScrollListener = object : CarouselOnScrollListener {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                super.onScrollStateChanged(recyclerView, newState, position, carouselItem)
                currentCarouselPosition = position
                val missionBriefingTextView: TextView =
                    findViewById(R.id.mission_briefing_text_view)
                missionBriefingTextView.text = listOfMissionBriefingTexts[position]
            }
        }
    }
}