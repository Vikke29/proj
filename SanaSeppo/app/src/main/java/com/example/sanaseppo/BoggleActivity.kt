package com.example.sanaseppo

import android.app.Dialog
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sanaseppo.R.*
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import kotlin.random.Random

class BoggleActivity : AppCompatActivity() {
    private lateinit var foundWordsAdapter: FoundWordsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var longestWordAdapter: LongestWordAdapter
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var boggleAdapter: BoggleAdapter

    private val listOfSelectedButtons: MutableList<Button> = mutableListOf()
    private val listOfFoundWords: MutableList<String> = mutableListOf()

    private lateinit var loadingProgressDialog: Dialog
    private lateinit var pauseDialog: Dialog

    private var points = 0
    private var maxPoints = 0

    private var listOfNeedlessButtonCoordinates: MutableList<Pair<Int, Int>> = mutableListOf()
    private var hintListOfNeedlessButtonCoordinates: MutableList<Pair<Int, Int>> = mutableListOf()

    private var secondsLeft: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_boggle)

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


        var boggleSize: Int? = null
        var longestWordHunt = false
        val positionInt = intent.getIntExtra(Constants.GAME_MODE, 0)
        when (positionInt) {
            0 -> {
                boggleSize = 4
                secondsLeft = 90
            }
            1 -> {
                boggleSize = 5
                longestWordHunt = true
                secondsLeft = 120
            }
        }

        val board = getBoggleBoard(boggleSize!!, longestWordHunt)
        val correctWords = solveBoggle(board)


        if (longestWordHunt) {
            val longestWord = getLongestWord(correctWords)
            hideFoundWordsRecyclerView()
            hideWordLengthsRecyclerView()
            setHintsTable(longestWord.length)
            setUpLongestWordRecyclerview(longestWord)
        } else {
            setUpFoundWordsRecyclerView()
            setUpWordLengthsRecyclerView(getListOfWordLengths(correctWords))

        }
        setUpProgressBar(correctWords)
        setUpBoggleButtonsRecyclerView(board, correctWords, longestWordHunt)

        val showHintButton: Button = findViewById(R.id.show_hint_button)
        showHintButton.setOnClickListener {
            longestWordAdapter.showHint()
            showNeedlessButtonsHint()
        }
        val pauseButton: ImageButton = findViewById(R.id.image_button_pause)
        pauseButton.setOnClickListener {
            if (!pauseDialog.isShowing){
                showPauseDialog()
                countDownTimer.cancel()
            }
        }

        startTimer()

        pauseDialog = Dialog(this)
        pauseDialog.setContentView(R.layout.pause_dialog)

        pauseDialog.setOnCancelListener {
            startTimer()
        }
    }
    private fun showNeedlessButtonsHint(){
        val item = listOfNeedlessButtonCoordinates.firstOrNull { !hintListOfNeedlessButtonCoordinates.contains(it) }
        if (item != null) {
            hintListOfNeedlessButtonCoordinates.add(item)

        }
        boggleAdapter.notifyDataSetChanged()
    }

    private fun getListOfWordLengths(wordList: List<String>): List<Pair<Int, Int>>{
        return wordList.groupingBy { it.length }.eachCount().toList()
    }

    private fun hideWordLengthsRecyclerView() {
        val wordLengthsCardView: CardView = findViewById(R.id.word_lengths_card_view)
        wordLengthsCardView.visibility = View.GONE
    }

    private fun startTimer(){
        countDownTimer = object : CountDownTimer((secondsLeft * 1000).toLong(), 1000){
            override fun onTick(p0: Long) {
                secondsLeft = (p0 / 1000).toInt()
                val timeLeftTextView: TextView = findViewById(R.id.time_left_text_view)
                var secondsLeftHelper = secondsLeft
                var minutesLeft = 0
                while(secondsLeftHelper - 59 > 0){
                    minutesLeft += 1
                    secondsLeftHelper -= 60
                }
                if (secondsLeftHelper.toString().length == 1){
                    timeLeftTextView.text = "$minutesLeft:0$secondsLeftHelper"
                }else{
                    timeLeftTextView.text = "$minutesLeft:$secondsLeftHelper"
                }
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }

        }
        countDownTimer.start()

    }


    private fun setHintsTable(length: Int) {
        val hintsLinearLayout: LinearLayout = findViewById(R.id.hints_linear_layout)
        hintsLinearLayout.visibility = View.VISIBLE
        val wordLengthTextView: TextView = findViewById(R.id.hint_length)
        wordLengthTextView.text = "Sanan pituus: ${length.toString()}-kirjainta"
    }

    private fun hideFoundWordsRecyclerView() {
        val foundWordsCardView: CardView = findViewById(R.id.found_words_card_view)
        foundWordsCardView.visibility = View.GONE
    }

    private fun getLongestWord(listOfWords: List<String>): String {
        return listOfWords.maxByOrNull { it.length } ?: ""
    }


    private fun solveBoggle(boggleBoard: Array<Array<Char>>): List<String> {

        val inputStream = resources.openRawResource(R.raw.sanalista)
        val dictionary = inputStream.bufferedReader().useLines { lines ->
            lines.toSet()
        }

        val convertedBoard: Array<CharArray> = boggleBoard.map { it.toCharArray() }.toTypedArray()

        val allWords = BoggleSolver(convertedBoard, dictionary).solve()

        for (word in allWords) {
            Log.d("VALINTA", word)
        }
        return allWords.toList()


    }

    private fun setUpProgressBar(listOfWords: List<String>) {
        progressBar = findViewById(R.id.boggle_progress_bar)
        for (word in listOfWords) {
            maxPoints += getPoints(word.length)
        }
        setProgressText()
        progressBar.max = maxPoints
        progressBar.progress = points

    }

    private fun setProgressText() {
        val progressTextView: TextView = findViewById(R.id.boggle_progress_bar_text_view)
        progressTextView.text = "$points/$maxPoints"
    }

    private fun showProgressDialog() {
        loadingProgressDialog = Dialog(this)
        loadingProgressDialog.setContentView(R.layout.progress_dialog)

        loadingProgressDialog.show()
    }

      private fun showPauseDialog(){
        val continueButton: Button = pauseDialog.findViewById(R.id.resume_button_pause)
        val exitButton: Button = pauseDialog.findViewById(R.id.exit_button_pause)
        val surrenderButton: Button = pauseDialog.findViewById(R.id.surrender_button_pause)

        continueButton.setOnClickListener {
            startTimer()
            pauseDialog.dismiss()
        }
        exitButton.setOnClickListener {
            countDownTimer.cancel()
            finish()
        }
        surrenderButton.setOnClickListener {
            Toast.makeText(this, "Implement surrender functionality", Toast.LENGTH_SHORT).show()
        }

        pauseDialog.show()
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
            else -> if (wordLength >= 20) {
                200
            } else {
                0
            }
        }
    }


    private fun getBoggleBoard(koko: Int, longestWordHunt: Boolean): Array<Array<Char>> {
        val aakkoset = "aehijklmnoprstuv"

        if (longestWordHunt) {
            val board: Array<Array<Char>> = Array(koko) {
                Array(koko) { ' ' }
            }
            val inputStream = resources.openRawResource(R.raw.longwordstemp)
            val listOfLongWords = inputStream.bufferedReader().useLines { lines ->
                lines.toList()
            }
            var randomLongWord: String
            while (true) {
                randomLongWord = listOfLongWords[Random.nextInt(0, listOfLongWords.size)]
                if (randomLongWord.length <= koko * koko) {
                    break
                }
            }

            // Sijoita satunnainen pitkä sana pelilaudalle käyttämällä syvyyshakua
            var wordPlaced = false
            val startPositions = mutableListOf<Pair<Int, Int>>()
            for (startRow in 0 until koko) {
                for (startCol in 0 until koko) {
                    startPositions.add(Pair(startRow, startCol))
                }
            }
            startPositions.shuffle()
            for (startPosition in startPositions) {
                if (placeWord(
                        board,
                        startPosition.first,
                        startPosition.second,
                        randomLongWord,
                        0
                    )
                ) {
                    wordPlaced = true
                    break
                }
            }

            // Täytä tyhjät ruudut satunnaisilla kirjaimilla
            for (i in 0 until koko) {
                for (j in 0 until koko) {
                    if (board[i][j] == ' ') {
                        Log.d("VALINTA", "Kohta ($i, $j) on tyhjä")
                        listOfNeedlessButtonCoordinates.add(Pair(i, j))
                        board[i][j] = aakkoset[Random.nextInt(0, aakkoset.length)]
                    }

                }

            }
            return board
        } else {
            val board: Array<Array<Char>> = Array(koko) {
                Array(koko) {
                    aakkoset[Random.nextInt(0, aakkoset.length)]
                }

            }
            return board
        }
    }

    private fun placeWord(
        board: Array<Array<Char>>,
        row: Int,
        col: Int,
        word: String,
        index: Int
    ): Boolean {
        if (index == word.length) return true
        if (row !in board.indices || col !in board[row].indices || board[row][col] != ' ') return false

        board[row][col] = word[index]
        val nextIndex = index + 1

        // Kokeile sijoittaa seuraava kirjain kaikkiin suuntiin satunnaisessa järjestyksessä
        val nextPositions = listOf(
            Pair(row - 1, col),
            Pair(row + 1, col),
            Pair(row, col - 1),
            Pair(row, col + 1)
        ).shuffled()
        for (nextPosition in nextPositions) {
            if (placeWord(board, nextPosition.first, nextPosition.second, word, nextIndex)) {
                return true
            }
        }

        // Peruuta muutos ja palauta false
        board[row][col] = ' '
        return false
    }

    private fun setUpFoundWordsRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.found_words_recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        foundWordsAdapter = FoundWordsAdapter(this, listOfFoundWords)
        recyclerView.adapter = foundWordsAdapter
    }
    private fun setUpWordLengthsRecyclerView(list: List<Pair<Int, Int>>){
        val recyclerView: RecyclerView = findViewById(R.id.word_lengths_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val wordLengthsAdapter = WordLengthsAdapter(this, list.sortedBy { it.first })
        recyclerView.adapter = wordLengthsAdapter
    }

    private fun setUpLongestWordRecyclerview(longestWord: String) {
        val recyclerView: RecyclerView = findViewById(R.id.longest_word_recycler_view)
        recyclerView.visibility = View.VISIBLE

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        longestWordAdapter = LongestWordAdapter(this, longestWord)
        recyclerView.adapter = longestWordAdapter
    }

    private fun setUpBoggleButtonsRecyclerView(
        board: Array<Array<Char>>,
        correctWords: List<String>,
        longestWordHuntGameMode: Boolean
    ) {
        val recyclerView: RecyclerView = findViewById(R.id.boggle_buttons_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, board[0].size)

        val spacing = resources.getDimensionPixelSize(R.dimen.boggle_button_spacing)

        val spacingItemDecoration = SpacingItemDecoration(
            Spacing(
                horizontal = spacing,
                vertical = spacing,
                edges = Rect(0, 0, 0, 0)
            )
        )

        val lista: List<String> = board.flatMap { row ->
            row.map { it.toString().uppercase() }
        }
        //spacingItemDecoration.isSpacingDrawingEnabled = true
        //spacingItemDecoration.drawingConfig =
        //    SpacingItemDecoration.DrawingConfig(horizontalColor = Color.MAGENTA)
        recyclerView.invalidateItemDecorations()
        recyclerView.addItemDecoration(spacingItemDecoration)
        boggleAdapter = BoggleAdapter(lista, this, hintListOfNeedlessButtonCoordinates)
        recyclerView.adapter = boggleAdapter

        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            var lastSelectedButton: Button? = null
            var lastSelectedPosition: Pair<Int, Int>? = null

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                // Hae valittu nappula
                val childView = rv.findChildViewUnder(e.x, e.y)
                val selectedButton = childView?.findViewById<Button>(R.id.letter_button)
                val selectedPosition =
                    childView?.let { rv.getChildAdapterPosition(it) }?.let { position ->
                        position / board[0].size to position % board[0].size
                    }

                val buttonRegion = Region()
                selectedButton?.let { button ->
                    val buttonRect = Rect()
                    button.getDrawingRect(buttonRect)
                    rv.offsetDescendantRectToMyCoords(button, buttonRect)
                    val cornerRadius =
                        rv.context.resources.getDimension(R.dimen.boggle_button_corner_radius)

                    buttonRegion.setPath(
                        Path().apply {
                            addRoundRect(
                                RectF(buttonRect),
                                cornerRadius,
                                cornerRadius,
                                Path.Direction.CW
                            )
                        },
                        Region(buttonRect)
                    )

                }



                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (buttonRegion.contains(e.x.toInt(), e.y.toInt())) {
                            selectedButton?.isSelected = true
                            lastSelectedButton = selectedButton
                            lastSelectedPosition = selectedPosition
                            setButtonBackGroundColor(selectedButton!!, "#FBC02D")
                            listOfSelectedButtons.add(selectedButton)
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        // Käsittele sormen liike nappulan päällä
                        if (selectedButton != lastSelectedButton && buttonRegion.contains(
                                e.x.toInt(),
                                e.y.toInt()
                            )
                        ) {
                            // Tarkista, onko nappula vierekkäin tai viistossa suhteessa edelliseen valittuun nappulaan
                            if (lastSelectedPosition != null && selectedPosition != null) {
                                val rowDiff =
                                    kotlin.math.abs(lastSelectedPosition!!.first - selectedPosition.first)
                                val colDiff =
                                    kotlin.math.abs(lastSelectedPosition!!.second - selectedPosition.second)
                                if (rowDiff <= 1 && colDiff <= 1) {
                                    lastSelectedButton?.isSelected = false
                                    if (selectedButton != null) {
                                        selectedButton.isSelected = true
                                    }

                                    if (selectedButton != null) {
                                        if (selectedButton !in listOfSelectedButtons) {
                                            listOfSelectedButtons.add(selectedButton)
                                            setButtonBackGroundColor(
                                                selectedButton,
                                                "#FBC02D"
                                            )

                                        } else {
                                            if (listOfSelectedButtons[listOfSelectedButtons.size - 2] == selectedButton) {
                                                setButtonBackGroundColor(
                                                    listOfSelectedButtons.last(),
                                                    "#D3D3D3"
                                                )

                                                listOfSelectedButtons.removeLast()
                                            }
                                        }

                                    }
                                    lastSelectedButton = selectedButton
                                    lastSelectedPosition = selectedPosition
                                }


                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        selectedButton?.isSelected = false
                        val constructedWord = constructWord()
                        if (constructedWord in listOfFoundWords) {
                            showAnimation(true, true)
                        } else if (constructedWord in correctWords) {
                            if (!longestWordHuntGameMode) {
                                listOfFoundWords.add(constructedWord)
                                foundWordsAdapter.notifyDataSetChanged()
                            }
                            points += getPoints(constructedWord.length)
                            setProgressText()
                            progressBar.progress = points
                            showAnimation(true, false)
                        } else {
                            showAnimation(false, false)
                        }
                        listOfSelectedButtons.clear()
                    }

                }

                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun setButtonBackGroundColor(selectedButton: Button, colorString: String) {
        Log.d("TAGI", "Button height: ${selectedButton.height} ACTIVITY")
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(Color.parseColor(colorString))
        drawable.setStroke(12, Color.BLACK)

        val radius = selectedButton.height / 8f
        drawable.cornerRadius = radius

        selectedButton.background = drawable
    }

    private fun constructWord(): String {
        var word = ""
        for (button in listOfSelectedButtons) {
            word += button.text.toString().lowercase()
        }
        return word
    }

    private fun showAnimation(correct: Boolean, alreadyFound: Boolean) {
        for (button in listOfSelectedButtons) {
            val secondDrawableFile = GradientDrawable()
            secondDrawableFile.shape = GradientDrawable.RECTANGLE
            secondDrawableFile.setColor(Color.parseColor("#D32F2F"))
            secondDrawableFile.setStroke(12, Color.BLACK)

            val radius = button.height / 8f
            secondDrawableFile.cornerRadius = radius
            if (correct) {
                if (alreadyFound) {
                    secondDrawableFile.setColor(Color.parseColor("#1976D2"))
                } else {
                    secondDrawableFile.setColor(Color.parseColor("#88F32A"))

                }
            }
            val originalDrawableFile = GradientDrawable()
            originalDrawableFile.shape = GradientDrawable.RECTANGLE
            originalDrawableFile.setColor(Color.parseColor("#D3D3D3"))
            originalDrawableFile.setStroke(12, Color.BLACK)
            originalDrawableFile.cornerRadius = radius

            val transitionDrawable = TransitionDrawable(
                arrayOf(
                    secondDrawableFile,
                    originalDrawableFile
                )
            )
            button.background = transitionDrawable
            transitionDrawable.startTransition(750)

        }
    }

    override fun onBackPressed() {
        if (pauseDialog != null){
            if (!pauseDialog!!.isShowing){
                showPauseDialog()
                countDownTimer.cancel()
            }else{
                startTimer()
            }
        }else{
            showPauseDialog()
            countDownTimer.cancel()
        }

    }
}
