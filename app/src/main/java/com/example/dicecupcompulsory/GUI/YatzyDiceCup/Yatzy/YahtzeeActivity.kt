package com.example.dicecupcompulsory.GUI.YatzyDiceCup.Yatzy

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.contains
import com.example.dicecupcompulsory.Model.BEDiceRoll
import com.example.dicecupcompulsory.R
import com.example.dicecupcompulsory.R.color.grey
import kotlinx.android.synthetic.main.diceroll_layout.*
import kotlinx.android.synthetic.main.diceroll_layout.glDices
import kotlinx.android.synthetic.main.yahtzee_layout.*
import java.util.*
import kotlin.collections.ArrayList

class YahtzeeActivity : AppCompatActivity() {

    var MAX_AMOUNT_OF_DICES = 5

    val dicePictures = arrayOf(0, R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    val firstList = arrayListOf(1,1,1,1,1,1,1)
    val mGenerator = Random()

    var currentDicesInMain = ArrayList<Int>()
    var currentDicesInSaved = ArrayList<Int>()
    var diceAmount = MAX_AMOUNT_OF_DICES
    var turnsLeft = 3

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntegerArrayList("currentDicesInMain", currentDicesInMain)
        outState.putIntegerArrayList("currentDicesInSaved", currentDicesInSaved)
        outState.putInt("turnsLeft", turnsLeft)
        outState.putInt("diceAmount", diceAmount)

        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yahtzee_layout)

        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val amount = extras.getInt("diceAmount")

            MAX_AMOUNT_OF_DICES = amount
            diceAmount = MAX_AMOUNT_OF_DICES
        }

        if (savedInstanceState != null) {
            val currentDicesListInMain = savedInstanceState.getIntegerArrayList("currentDicesInMain") as ArrayList<Int>
            val currentDicesListInSaved = savedInstanceState.getIntegerArrayList("currentDicesInSaved") as ArrayList<Int>
            val turns = savedInstanceState.getInt("turnsLeft")
            val dices = savedInstanceState.getInt("diceAmount")

            currentDicesInMain = currentDicesListInMain
            currentDicesInSaved = currentDicesListInSaved
            turnsLeft = turns
            diceAmount = dices

            //Makes the dices show the dices that were last rolled.
            println(diceAmount)
            createDices(diceAmount, currentDicesInMain)
            createDicesSaved(MAX_AMOUNT_OF_DICES-diceAmount, currentDicesListInSaved)

            if(turns == 0)
            {
                gameEnded()
            }
        }
        else {
            //Sets the first dices
            for (i in 1..diceAmount)
            {
                currentDicesInMain.add(1)
            }
            createDices(diceAmount, firstList)
        }
    }

    //Takes an amount and a list of indexes for the picture list and adds them to the main gridlayout
    private fun createDices(dicesToBeAdded: Int, dicePicture: ArrayList<Int>) {

        for (i in 1..dicesToBeAdded) {
            val imgView = ImageButton(this)

            val lp = LinearLayout.LayoutParams(300, 300)
            lp.setMargins(5,5,5,5)
            imgView.layoutParams = lp

            imgView.setBackgroundResource(dicePictures[dicePicture[i-1]])

            //Sets the onClickListener
            imgView.setOnClickListener { onClickDices(glDices, glSavedDices, imgView)}

            glDices.addView(imgView)
        }
    }

    //Takes an amount and a list of indexes for the picture list and adds them to the main gridlayout
    private fun createDicesSaved(dicesToBeAdded: Int, dicePicture: ArrayList<Int>) {

        for (i in 1..dicesToBeAdded) {
            val imgView = ImageButton(this)

            val lp = LinearLayout.LayoutParams(150, 150)
            lp.setMargins(2,0,2,0)
            imgView.layoutParams = lp

            imgView.setBackgroundResource(dicePictures[dicePicture[i-1]])

            //Sets the onClickListener
            imgView.setOnClickListener { onClickDices(glDices, glSavedDices, imgView)}

            glSavedDices.addView(imgView)
        }
    }

    fun onClickDices(grid: GridLayout, otherGrid: GridLayout, child: ImageButton) {

        //Makes sure you cannot click the dices before you have rolled them
        if(turnsLeft != 3)
        {

            //Checks which grid contains the child
            if(grid.contains(child))
            {
                val index = grid.indexOfChild(child)

                grid.removeView(child)

                val lp = LinearLayout.LayoutParams(150, 150)
                lp.setMargins(2,0,2,0)
                child.layoutParams = lp

                otherGrid.addView(child)

                val value = currentDicesInMain[index]
                currentDicesInMain.removeAt(index)

                currentDicesInSaved.add(value)

                diceAmount--
            }
            else if(otherGrid.contains(child))
            {
                val index = otherGrid.indexOfChild(child)

                otherGrid.removeView(child)

                val lp = LinearLayout.LayoutParams(300, 300)
                lp.setMargins(5,5,5,5)
                child.layoutParams = lp

                grid.addView(child)

                val value = currentDicesInSaved[index]
                currentDicesInSaved.removeAt(index)

                currentDicesInMain.add(value)

                diceAmount++
            }
        }
    }

    //Gets a random number for each dice, and changes the pictures of the dices to reflect the numbers
    fun onClickRoll(view: View) {

        //To roll the user need to have more than 0 turns left
        if(turnsLeft > 0)
        {
            currentDicesInMain.clear()

            val dicesToRool = diceAmount-1

            for (i in 0..dicesToRool)
            {
                val randomNumber = mGenerator.nextInt(6)+1
                println(randomNumber)

                val currentImgView = glDices.getChildAt(i)

                with (currentImgView as ImageButton)
                {
                    currentImgView.setBackgroundResource(dicePictures[randomNumber])
                }

                currentDicesInMain.add(randomNumber)
            }

            turnsLeft--

            //If the user has 0 rolls left after their roll, run this code
            if(turnsLeft == 0)
            {
                gameEnded()
            }
        }
    }

    fun gameEnded() {
        val children = glDices.children
        val savedChildren = glSavedDices.children

        //Sets all imagebuttons in the grids to non-clickable
        children.forEach { i ->
            with(i as ImageButton)
            {
                i.isClickable = false
            }
        }
        savedChildren.forEach { i ->
            with(i as ImageButton)
            {
                i.isClickable = false
            }
        }

        //Changes the look of the roll button
        val rollBtn = findViewById<Button>(R.id.btnRoll)
        rollBtn.isClickable = false
        rollBtn.setBackgroundColor(Color.parseColor("#535353"))
    }

    //Resets the turns and the grids
    fun onClickReset(view: View) {
        glDices.removeAllViews()
        glSavedDices.removeAllViews()

        diceAmount = MAX_AMOUNT_OF_DICES

        currentDicesInMain.clear()
        currentDicesInMain.addAll(firstList)
        currentDicesInSaved.clear()

        createDices(diceAmount, firstList)
        turnsLeft = 3

        val rollBtn = findViewById<Button>(R.id.btnRoll)
        rollBtn.isClickable = true
        rollBtn.setBackgroundColor(Color.parseColor("#0004FF"))
    }

    //Sends the user back to the MainActivity
    fun onClickBack(view: View) {
        finish()
    }
}