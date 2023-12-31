package com.example.sanaseppo

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.stream.IntStream.range
import kotlin.math.sqrt


class BoggleAdapter(
    private val letters: List<String>,
    private val context: Context,
    private val listOfNeedlessCoordinates: List<Pair<Int, Int>>
) : RecyclerView.Adapter<BoggleAdapter.BoggleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoggleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_boggle, parent, false)

        val spacing = context.resources.getDimensionPixelSize(R.dimen.boggle_button_spacing)

        val amountOfRows = getAmountOfRows(letters)

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val buttonSize =
            (screenWidth - ((amountOfRows - 1) * spacing)) / amountOfRows

        val button: Button = view.findViewById(R.id.letter_button)
        button.layoutParams.width = buttonSize
        button.layoutParams.height = buttonSize

        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(Color.parseColor("#D3D3D3"))
        drawable.setStroke(12, Color.BLACK)

        button.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val radius = button.height / 8f
                drawable.cornerRadius = radius

                // Poista kuuntelija, jotta se ei suoritu uudelleen
                button.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        button.background = drawable

        return BoggleViewHolder(view)
    }

    private fun getAmountOfRows(stringList: List<String>): Int {
        return sqrt(stringList.size.toDouble()).toInt()

    }

    override fun onBindViewHolder(holder: BoggleViewHolder, position: Int) {
        val letter = letters[position]
        holder.bind(letter)
        Log.d("TESTI", "Alku -----------------------------------")
        for(index in listOfNeedlessCoordinates){
            Log.d("TESTI", "${index.first}, ${index.second}")
        }
        if (listOfNeedlessCoordinates.contains(
                Pair(
                    position / getAmountOfRows(letters),
                    position % getAmountOfRows(letters)
                )
            )
        ) {
            holder.setGrayBackGroundDrawable("#717273")
        }else{
            holder.setGrayBackGroundDrawable("#D3D3D3")
        }
    }

    override fun getItemCount(): Int {
        return letters.size
    }

    inner class BoggleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectedButton: Button = itemView.findViewById(R.id.letter_button)

        fun bind(letter: String) {
            selectedButton.text = letter

        }

        fun setGrayBackGroundDrawable(colorString: String) {
            Log.d("TAGI", "Button height: ${selectedButton.height} adapter")
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(Color.parseColor(colorString))
            drawable.setStroke(12, Color.BLACK)

            selectedButton.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val radius = selectedButton.height / 8f
                    drawable.cornerRadius = radius

                    // Poista kuuntelija, jotta se ei suoritu uudelleen
                    selectedButton.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

            selectedButton.background = drawable


        }


    }
}
